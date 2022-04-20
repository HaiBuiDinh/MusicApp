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
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemyfiles.musicapp.Domain.AlbumInfo
import com.aemyfiles.musicapp.Domain.MusicDatabase
import com.aemyfiles.musicapp.Domain.MusicRepository
import com.aemyfiles.musicapp.External.adapter.CenterZoomLayoutManager
import com.aemyfiles.musicapp.External.adapter.ShowListAlbumAdapter
import com.aemyfiles.musicapp.External.adapter.ShowListSongAdapter
import com.aemyfiles.musicapp.External.notification.CreateNotification
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.utils.Permission
import com.aemyfiles.musicapp.Presenter.MusicViewModel
import com.aemyfiles.musicapp.Presenter.AudioViewModelFactory
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE: Int = 200
        const val UPDATE_LAYOUT: String = "Update_Layout"
    }

    lateinit var mViewModel: MusicViewModel
    lateinit var mMediaPlayService: MediaPlayService
    lateinit var mAdapter: ShowListSongAdapter
    lateinit var mRepository: MusicRepository
    lateinit var mAdapterAlbum: ShowListAlbumAdapter
    private val mHanlder: Handler = Handler()

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            mMediaPlayService = (binder as MediaPlayService.MyBinder).getService()
            mRepository = MusicRepository(MusicDatabase(this@MainActivity))
            val factory = AudioViewModelFactory(mRepository)
            mViewModel = ViewModelProvider(this@MainActivity, factory).get(MusicViewModel::class.java)
            initView()
            getAlbum()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        bindService()
        createNotificationChannel()
        registerReceiver()

    }


    private fun bindService() {
        val intent = Intent(applicationContext, MediaPlayService::class.java)
        startService(intent)
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
    }

    private fun unBindService() {
        unbindService(mServiceConnection)
    }

    private fun initView() {
        mAdapter = ShowListSongAdapter(mMediaPlayService)
        mAdapterAlbum = ShowListAlbumAdapter(mViewModel)
        recycler_view_main!!.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = mAdapter
        }

        recyclerView!!.apply {
            layoutManager =
                CenterZoomLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapterAlbum
        }

        mViewModel.getAllAudio().observe(this, Observer {
            if (it.isNotEmpty()) {
                mAdapter.setData(it)
                if (mMediaPlayService.mPlayer.mQueue.isEmpty()) {
                    mMediaPlayService.mPlayer.mQueue.addAll(it)
                }
            } else {
                if (Permission.isPermissionsAllowed(this)) syncMediaProvider()
                else Permission.askForPermissions(this)
            }
        })

        button_get_data.setOnClickListener {
            if (Permission.isPermissionsAllowed(this)) syncMediaProvider()
            else Permission.askForPermissions(this)
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

        previous.setOnClickListener {
            val intent = Intent().setAction(CreateNotification.ACTION_PREVIOUS)
            sendBroadcast(intent)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) mMediaPlayService.mPlayer.seekTo(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        mHanlder.removeCallbacks(mRunnable)
        mHanlder.postDelayed(mRunnable, 15)
    }

    private var mRunnable: Runnable = object : Runnable {
        override fun run() {
            if (mMediaPlayService.mPlayer.isPlaying()) {
                seekBar.progress = mMediaPlayService.mPlayer.currentPosition()
                seekBar.max = mMediaPlayService.mPlayer.duration()
                tv_time_left.text =
                    "${mMediaPlayService.mPlayer.currentPosition() / 60000}:${(mMediaPlayService.mPlayer.currentPosition() % 60000) / 1000}"
                tv_time_right.text =
                    "${mMediaPlayService.mPlayer.duration() / 60000}:${(mMediaPlayService.mPlayer.duration() % 60000) / 1000}"
            }
            mHanlder.postDelayed(this, 15)
        }
    }


    private fun syncMediaProvider() {
        GlobalScope.launch(Dispatchers.IO) {
            mRepository.syncFromProvider(applicationContext)
        }
//        listSong?.let { list ->
//            val allSong = list.toMutableList()
//            val thumbnailCallback = ThumbnailManager.ThumbnailCallback { albumName, path, bitmap ->
//                Log.d("long.vt", "onSuccess: $bitmap")
//                mapSong[albumName!!] = path!!
//            }
//
//            allSong.forEach {
//                mViewModel.insert(it)
//                if (mapSong[it.album_name] == null)
//                    ThumbnailManager.getInstance().loadThumbnail(
//                        ThumbnailManager.ThumbnailInfo(
//                            null,
//                            it.id,
//                            it.path,
//                            it.album_name,
//                            null,
//                            320,
//                            ItemType.SONG_TYPE,
//                            thumbnailCallback
//                        )
//                    )
//            }
//        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            syncMediaProvider()
        }
    }


    override fun onDestroy() {
        unBindService()
        super.onDestroy()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.cancelAll()
//        unregisterReceiver(broadcastReceiver)
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
            if (mMediaPlayService.mPlayer.isPlaying()) {
                play.setImageResource(R.drawable.ic_pause)
            } else {
                play.setImageResource(R.drawable.ic_play)
            }
            mAdapter.notifyDataSetChanged()
        }

    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(UPDATE_LAYOUT)
        this.applicationContext.registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun getAlbum() {
        mViewModel.getListAlBum().observe(this, Observer {
            it?.let { mAdapterAlbum.setData(it as ArrayList<AlbumInfo>) }
            Log.d("hai.bui1", "getAlbum: onChanged" + it.size)
        })
    }

}