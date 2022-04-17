package com.aemyfiles.musicapp.External.activities

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemyfiles.musicapp.Domain.AudioDatabase
import com.aemyfiles.musicapp.Domain.AudioInfo
import com.aemyfiles.musicapp.Domain.AudioRepository
import com.aemyfiles.musicapp.External.adapter.ShowListSongAdapter
import com.aemyfiles.musicapp.External.notification.CreateNotification
import com.aemyfiles.musicapp.External.notification.Playable
import com.aemyfiles.musicapp.External.services.AudioService
import com.aemyfiles.musicapp.External.utils.MediaManager
import com.aemyfiles.musicapp.External.utils.Permission
import com.aemyfiles.musicapp.Presenter.AudioViewModel
import com.aemyfiles.musicapp.Presenter.AudioViewModelFactory
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ShowListSongAdapter.AdapterCallBack, Playable {
    companion object {
        const val REQUEST_CODE: Int = 200
    }
    lateinit var mViewModel: AudioViewModel
    lateinit var mAudioService: AudioService
    lateinit var mAdapter: ShowListSongAdapter
    private var mListSongs = ArrayList<AudioInfo>()
    private val mHanlder: Handler = Handler()
    lateinit var notificationManager: NotificationManager
    private var isPlaying: Boolean = false

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            mAudioService = (binder as AudioService.MyBinder).getService()
            initView()
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val audioRepository = AudioRepository(AudioDatabase(this))
        val factory = AudioViewModelFactory(audioRepository)
        mViewModel = ViewModelProvider(this, factory).get(AudioViewModel::class.java)
        bindService()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.extras!!.getString("actionname")

            when(action) {
                CreateNotification.ACTION_PREVIOUS -> onTrackPrevious()
                CreateNotification.ACTION_PLAY -> {
                    if (isPlaying) onTrackPause() else onTrackPlay()
                }
                CreateNotification.ACTION_NEXT -> onTrackNext()
            }
        }

    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel: NotificationChannel = NotificationChannel(CreateNotification.CHANEL_ID, "Music App", NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.let { it.createNotificationChannel(channel) }
        }
    }

    private fun bindService() {
        val intent = Intent(applicationContext, AudioService::class.java)
        startService(intent)
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
    }

    private fun unBindService() {
        unbindService(mServiceConnection)
    }

    private fun initView() {
        mAdapter = ShowListSongAdapter(mAudioService, this)
        recycler_view_main.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = mAdapter
        }

        button_get_data.setOnClickListener {
            if (Permission.isPermissionsAllowed(this)) getData()
            else Permission.askForPermissions(this)
        }
        mViewModel.getAllAudio().observe(this, Observer {
            mAdapter.setData(it)
            mListSongs = it as ArrayList<AudioInfo>

            if (mAudioService.mPlayer.mQueue.isEmpty()) {
                mAudioService.mPlayer.mQueue.addAll(it)
            }
        })

        play.setOnClickListener {
            if(mAudioService.mPlayer.isPlaying()) {
                mAudioService.mPlayer.pause()
                play.setImageResource(R.drawable.ic_play)
            } else {
                mAudioService.mPlayer.play()
                play.setImageResource(R.drawable.ic_pause)
            }
        }

        next.setOnClickListener {
            play.setImageResource(R.drawable.ic_pause)
            mAudioService.mPlayer.playNextSong()
            mAdapter.notifyDataSetChanged()
        }

        previous.setOnClickListener {
            play.setImageResource(R.drawable.ic_pause)
            mAudioService.mPlayer.playPreviousSong()
            mAdapter.notifyDataSetChanged()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) mAudioService.mPlayer.seekTo(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        mHanlder.removeCallbacks(mRunnable)
        mHanlder.postDelayed(mRunnable, 15)
    }

    private var mRunnable: Runnable = object : Runnable {
        override fun run() {
            if (mAudioService.mPlayer.isPlaying()) {
                seekBar.progress = mAudioService.mPlayer.currentPosition()
                seekBar.max = mAudioService.mPlayer.duration()
                tv_time_left.setText("${mAudioService.mPlayer.currentPosition() / 60000}:${(mAudioService.mPlayer.currentPosition() % 60000) / 1000}")
                tv_time_right.setText("${mAudioService.mPlayer.duration() / 60000}:${(mAudioService.mPlayer.duration() % 60000) / 1000}")
            }
            mHanlder.postDelayed(this, 15)
        }
    }


    private fun getData() {
        val listSong = MediaManager.getDataFromMedia(this)
        listSong?.let {
            val allSong = it.toMutableList()
            allSong.forEach { it -> mViewModel.insert(it) }
        }
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
                    getData()
                } else {
                    // permission is denied, you can ask for permission again, if you want
                    Permission.askForPermissions(this)
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {getData()}
    }

    override fun onClickSong() {
        play?.setImageResource(R.drawable.ic_pause)
    }

    override fun onTrackPrevious() {
        TODO("Not yet implemented")
        //CreateNotification().createNotification(this, audioInfo, 1)
    }

    override fun onTrackPlay() {
        TODO("Not yet implemented")
    }

    override fun onTrackPause() {
        TODO("Not yet implemented")
    }

    override fun onTrackNext() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        unBindService()
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.cancelAll()
        unregisterReceiver(broadcastReceiver)
    }

}