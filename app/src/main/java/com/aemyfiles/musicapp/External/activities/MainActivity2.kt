package com.aemyfiles.musicapp.External.activities

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aemyfiles.musicapp.Domain.MusicApplication
import com.aemyfiles.musicapp.External.fragment.HomeFragment
import com.aemyfiles.musicapp.External.fragment.LibraryFragment
import com.aemyfiles.musicapp.External.fragment.SettingFragment
import com.aemyfiles.musicapp.External.notification.CreateNotification
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.utils.Permission
import com.aemyfiles.musicapp.Presenter.MusicViewModel
import com.aemyfiles.musicapp.Presenter.MusicViewModelFactory
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE: Int = 200
        const val UPDATE_LAYOUT: String = "Update_Layout"
        const val HOME_FRAGMENT = 0
        const val LIBRARY_FRAGMENT = 1
        const val SETTING_FRAGMENT = 2
    }

    val mViewModel: MusicViewModel by viewModels {
        MusicViewModelFactory((application as MusicApplication).repository)
    }

    lateinit var mMediaPlayService: MediaPlayService

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            mMediaPlayService = (binder as MediaPlayService.MyBinder).getService()
//            initView()
//            getAlbum()
            if (Permission.isPermissionsAllowed(this@MainActivity2))HomeFragment(mViewModel, mMediaPlayService).apply { loadFragment(this) }
            else Permission.askForPermissions(this@MainActivity2)

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.hide()
        bindService()
        createNotificationChannel()
        registerReceiver()
        mViewModel.isSyncFinish.observe(this, {
            if(it)HomeFragment(mViewModel, mMediaPlayService).apply { loadFragment(this) }
        })
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


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CreateNotification.CHANEL_ID,
                "Music app",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("hai.bui1", "onReceive: main ")
//            if (mMediaPlayService.mPlayer.isPlaying()) {
//                play.setImageResource(R.drawable.ic_pause)
//            } else {
//                play.setImageResource(R.drawable.ic_play)
//            }
//            mAdapter.notifyDataSetChanged()
        }

    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(MainActivity.UPDATE_LAYOUT)
        this.applicationContext.registerReceiver(broadcastReceiver, intentFilter)
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
            MainActivity.REQUEST_CODE -> {
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



    override fun onDestroy() {
        unBindService()
        super.onDestroy()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.cancelAll()
//        unregisterReceiver(broadcastReceiver)
    }

}