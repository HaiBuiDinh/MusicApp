package com.aemyfiles.musicapp.External.activities

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.aemyfiles.musicapp.External.fragment.HomeFragment
import com.aemyfiles.musicapp.External.notification.CreateNotification
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.utils.ItemType
import com.aemyfiles.musicapp.External.utils.Permission
import com.aemyfiles.musicapp.External.utils.ThumbnailManager
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_song.*

class SongActivity : AppCompatActivity() {

    lateinit var mMediaPlayService: MediaPlayService

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            mMediaPlayService = (binder as MediaPlayService.MyBinder).getService()
            initView()
            updateUi()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)
        supportActionBar!!.hide()
        bindService()
        registerReceiver()
        back.setOnClickListener { finish() }
        rotate()
        //biggest_thumbnail_song.clearAnimation()
    }

    private fun rotate() {
        val rotateAnim: RotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f)
        rotateAnim.duration = 10000
        rotateAnim.repeatCount = Animation.INFINITE
        rotateAnim.interpolator = LinearInterpolator()
        biggest_thumbnail_song.startAnimation(rotateAnim)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("hai.bui1", "onReceive: main ")
            updateUi()
        }
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(MainActivity.UPDATE_LAYOUT)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun updateUi() {
        val song = mMediaPlayService.mPlayer.mQueue[mMediaPlayService.mPlayer.mCurrentPosSong]
        biggest_thumbnail_song.clipToOutline = true
        ThumbnailManager.getInstance().loadThumbnail(ThumbnailManager.ThumbnailInfo(biggest_thumbnail_song, song.id, song.path, 320, ItemType.SONG_TYPE))
        song_name.text = song.display_name
        song_artis.text = song.artist_name
    }

    private fun bindService() {
        val intent = Intent(applicationContext, MediaPlayService::class.java)
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
    }

    private fun unBindService() {
        unbindService(mServiceConnection)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
        unBindService()
    }

    private fun initView() {
        previous.setOnClickListener {
            val intent = Intent().setAction(CreateNotification.ACTION_PREVIOUS)
            sendBroadcast(intent)
        }
        play.setOnClickListener {
            if (mMediaPlayService.mPlayer.isPlaying()) {
                val intent_play = Intent().setAction(CreateNotification.ACTION_PAUSE)
                sendBroadcast(intent_play)
            } else {
                val intent_pause = Intent().setAction(CreateNotification.ACTION_PLAY)
                sendBroadcast(intent_pause)
            }
        }
        next.setOnClickListener {
            val intent = Intent().setAction(CreateNotification.ACTION_NEXT)
            sendBroadcast(intent)
        }

    }
}