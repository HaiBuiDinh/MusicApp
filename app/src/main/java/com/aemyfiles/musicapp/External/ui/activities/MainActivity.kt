package com.aemyfiles.musicapp.External.ui.activities

import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.aemyfiles.musicapp.Domain.MusicApplication
import com.aemyfiles.musicapp.External.FragmentUtils
import com.aemyfiles.musicapp.External.notification.CreateNotification
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.ui.fragment.HomeFragment
import com.aemyfiles.musicapp.External.ui.fragment.LibraryFragment
import com.aemyfiles.musicapp.External.ui.fragment.SettingFragment
import com.aemyfiles.musicapp.External.utils.BroadcastUtils
import com.aemyfiles.musicapp.External.utils.PermissionUtils
import com.aemyfiles.musicapp.Presenter.controller.MainController
import com.aemyfiles.musicapp.Presenter.MusicViewModelFactory
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE: Int = 200
        const val UPDATE_LAYOUT: String = "Update_Layout"
        const val HOME_FRAGMENT = 0
        const val LIBRARY_FRAGMENT = 1
        const val SETTING_FRAGMENT = 2
    }

    val mViewModel: MainController by viewModels {
        MusicViewModelFactory((application as MusicApplication).repository)
    }

    lateinit var mMediaPlayService: MediaPlayService

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            mMediaPlayService = (binder as MediaPlayService.MyBinder).getService()
            initView()
            if (PermissionUtils.isPermissionsAllowed(this@MainActivity))HomeFragment(mViewModel, mMediaPlayService).apply { FragmentUtils.enterFragment(this@MainActivity, this, false, null) }
            else PermissionUtils.askForPermissions(this@MainActivity)
            if ( mMediaPlayService.mPlayer.mQueue.size > 0) updateControlPlayer()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService()
        registerReceiver()
        mViewModel.isSyncFinish.observe(this, {
            if(it)HomeFragment(mViewModel, mMediaPlayService).apply { FragmentUtils.enterFragment(this@MainActivity, this, false, null) }
        })

    }

    private fun bindService() {
        val intent = Intent(applicationContext, MediaPlayService::class.java)
        startService(intent)
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
    }

    private fun unBindService() {
        unbindService(mServiceConnection)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("hai.bui1", "onReceive: main ")
            updateControlPlayer()
        }
    }

    private fun updateControlPlayer() {
        if (!control_playing.isVisible) {
            control_playing.visibility = View.VISIBLE
        }
        song_name_main.text = mMediaPlayService.mPlayer.mQueue[mMediaPlayService.mPlayer.mCurrentPosSong].display_name
        song_artis_main.text = mMediaPlayService.mPlayer.mQueue[mMediaPlayService.mPlayer.mCurrentPosSong].artist_name
        song_artis_main.isSelected = true
        song_artis_main.isSelected = true
        if (mMediaPlayService.mPlayer.isPlaying()) {
            btn_main_play.setImageResource(R.drawable.ic_pause)
            rotate(thumbnail_playing)
        } else {
            btn_main_play.setImageResource(R.drawable.ic_play)
            thumbnail_playing.clearAnimation()
        }
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(UPDATE_LAYOUT)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun syncMediaProvider() {
        mViewModel.syncFromProvider(applicationContext)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted, you can perform your operation here
                    syncMediaProvider()
                } else {
                    // permission is denied, you can ask for permission again, if you want
                    PermissionUtils.askForPermissions(this)
                }
                return
            }
        }
    }

    private fun initView() {
        custom_navigation_bar.setNavigationChangeListener{ _, positon ->
            when (positon) {
                HOME_FRAGMENT -> {
                    HomeFragment(mViewModel, mMediaPlayService).apply {FragmentUtils.enterFragment(this@MainActivity, this, false, null)}}
                LIBRARY_FRAGMENT -> {
                    LibraryFragment(mMediaPlayService).apply {FragmentUtils.enterFragment(this@MainActivity, this, false, null)}}
                SETTING_FRAGMENT -> {
                    SettingFragment().apply { FragmentUtils.enterFragment(this@MainActivity, this, false, null)}}
            }
        }

        btn_main_previous.setOnClickListener {
            BroadcastUtils.sendAction(this, CreateNotification.ACTION_PREVIOUS)
        }
        btn_main_play.setOnClickListener {
            if (mMediaPlayService.mPlayer.isPlaying()) {
                BroadcastUtils.sendAction(this, CreateNotification.ACTION_PAUSE)
            } else {
                BroadcastUtils.sendAction(this, CreateNotification.ACTION_PLAY)
            }
        }
        btn_main_next.setOnClickListener {
            BroadcastUtils.sendAction(this, CreateNotification.ACTION_NEXT)
        }

        control_playing.setOnClickListener {
            startActivity(Intent(this, PlayingActivity::class.java))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.statusbar_color)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            window.navigationBarColor = resources.getColor(R.color.navigationbar_color)
        }
    }

    private fun rotate(view: View) {
        val rotateAnim: RotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f)
        rotateAnim.duration = 10000
        rotateAnim.repeatCount = Animation.INFINITE
        rotateAnim.interpolator = LinearInterpolator()
        view.startAnimation(rotateAnim)
    }

    override fun onDestroy() {
        unBindService()
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}