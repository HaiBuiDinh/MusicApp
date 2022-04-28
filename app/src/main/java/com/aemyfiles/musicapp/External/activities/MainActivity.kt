package com.aemyfiles.musicapp.External.activities

import android.app.NotificationChannel
import android.app.NotificationManager
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
import androidx.fragment.app.Fragment
import com.aemyfiles.musicapp.Domain.MusicApplication
import com.aemyfiles.musicapp.External.fragment.HomeFragment
import com.aemyfiles.musicapp.External.fragment.LibraryFragment
import com.aemyfiles.musicapp.External.fragment.SettingFragment
import com.aemyfiles.musicapp.External.notification.CreateNotification
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.utils.Permission
import com.aemyfiles.musicapp.Presenter.MainController
import com.aemyfiles.musicapp.Presenter.MusicViewModelFactory
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_song.*

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
            if (Permission.isPermissionsAllowed(this@MainActivity))HomeFragment(mViewModel, mMediaPlayService).apply { loadFragment(this) }
            else Permission.askForPermissions(this@MainActivity)
            if ( mMediaPlayService.mPlayer.mQueue.size > 0) updateControlPlayer()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        bindService()
        registerReceiver()
        mViewModel.isSyncFinish.observe(this, {
            if(it)HomeFragment(mViewModel, mMediaPlayService).apply { loadFragment(this) }
        })

    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
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
                    Permission.askForPermissions(this)
                }
                return
            }
        }
    }

    private fun initView() {
        custom_navigation_bar.setNavigationChangeListener{ _, positon ->
            when (positon) {
                HOME_FRAGMENT -> {
                    HomeFragment(mViewModel, mMediaPlayService).apply {loadFragment(this)}}
                LIBRARY_FRAGMENT -> {
                    LibraryFragment(mMediaPlayService).apply {loadFragment(this)}}
                SETTING_FRAGMENT -> {
                    SettingFragment().apply { loadFragment(this)}}
            }
        }

        btn_main_previous.setOnClickListener {
            val intent = Intent().setAction(CreateNotification.ACTION_PREVIOUS)
            sendBroadcast(intent)
        }
        btn_main_play.setOnClickListener {
            if (mMediaPlayService.mPlayer.isPlaying()) {
                val intent_play = Intent().setAction(CreateNotification.ACTION_PAUSE)
                sendBroadcast(intent_play)
            } else {
                val intent_pause = Intent().setAction(CreateNotification.ACTION_PLAY)
                sendBroadcast(intent_pause)
            }
        }
        btn_main_next.setOnClickListener {
            val intent = Intent().setAction(CreateNotification.ACTION_NEXT)
            sendBroadcast(intent)
        }

        control_playing.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("song name", mMediaPlayService.mPlayer.mQueue[mMediaPlayService.mPlayer.mCurrentPosSong].display_name)
            intent.putExtra("song path", mMediaPlayService.mPlayer.mQueue[mMediaPlayService.mPlayer.mCurrentPosSong].path)
            startActivity(Intent(this, SongActivity::class.java))
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