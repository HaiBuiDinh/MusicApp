package com.aemyfiles.musicapp.External.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemyfiles.musicapp.Domain.AudioDatabase
import com.aemyfiles.musicapp.Domain.AudioInfo
import com.aemyfiles.musicapp.Domain.AudioRepository
import com.aemyfiles.musicapp.External.adapter.ShowListSongAdapter
import com.aemyfiles.musicapp.External.utils.MediaManager
import com.aemyfiles.musicapp.External.utils.Permission
import com.aemyfiles.musicapp.Presenter.AudioViewModel
import com.aemyfiles.musicapp.Presenter.AudioViewModelFactory
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.Runnable

class MainActivity : AppCompatActivity(), ShowListSongAdapter.AudioClickListener {
    lateinit var audioViewModel: AudioViewModel
    lateinit var player: MediaPlayer
    private var listSongs = ArrayList<AudioInfo>()
    private var mPosition: Int = -1;
//    private val mHanlder: Handler = Handler()

    companion object {
        const val REQUEST_CODE: Int = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val audioRepository = AudioRepository(AudioDatabase(this))
        val factory = AudioViewModelFactory(audioRepository)
        audioViewModel = ViewModelProvider(this, factory).get(AudioViewModel::class.java)
        initView()
    }

    private fun initView() {
        player = MediaPlayer()
        player.setOnCompletionListener {
            mPosition = if (mPosition == listSongs.size - 1) 0 else mPosition + 1
            playSongUsePath(listSongs[mPosition].path)
        }
        val mAdapter = ShowListSongAdapter(this)
        recycler_view_main.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = mAdapter
        }

        button_get_data.setOnClickListener {
            if (Permission.isPermissionsAllowed(this)) getData()
            else Permission.askForPermissions(this)
        }
        audioViewModel.getAllAudio().observe(this, Observer {
            mAdapter.setData(it)
            listSongs = it as ArrayList<AudioInfo>
        })
        play.setOnClickListener{
            if (player.isPlaying) {
                player.pause()
                play.setImageResource(R.drawable.ic_play)
                Toast.makeText(this, "pause", Toast.LENGTH_SHORT).show()

            } else {
                player.start()
                play.setImageResource(R.drawable.ic_pause)
                Toast.makeText(this, "play", Toast.LENGTH_SHORT).show()
            }

        }

        next.setOnClickListener {
            mPosition = if (mPosition == listSongs.size - 1) 0 else mPosition + 1
            playSongUsePath(listSongs[mPosition].path)
            Toast.makeText(this, "next ${listSongs[mPosition].display_name}", Toast.LENGTH_SHORT).show()
        }

        previous.setOnClickListener {
            mPosition = if (mPosition == 0) listSongs.size - 1 else mPosition - 1
            playSongUsePath(listSongs[mPosition].path)
            Toast.makeText(this, "previous ${listSongs[mPosition].display_name}", Toast.LENGTH_SHORT).show()
        }

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                player.seekTo(i)
                if (player.isPlaying) play.setImageResource(R.drawable.ic_pause)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun playSongUsePath(path: String) {
        try {
            player.reset()
            player.setDataSource(path)
            player.prepare()
            player.start()
            //updateSeekbar()
            seekBar.max = player.duration
            tv_time_left.setText("00:00")
            tv_time_right.setText("${player.duration/60000}:${(player.duration%60000)/1000}")
        } catch (e: java.lang.Exception) {

        }
    }

    private fun getData() {
        val listSong = MediaManager.getDataFromMedia(this)
        listSong?.let {
            val allSong = it.toMutableList()
            allSong.forEach { it -> audioViewModel.insert(it) }
        }
        Log.d("hai.bui1", "onCreate: ")
    }

    private fun updateSeekbar() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    if (player.isPlaying) {
                        seekBar.progress = player.currentPosition
                        handler.postDelayed(this, 15)
                    }
                } catch (e: java.lang.Exception) {

                }
            }
        }, 15)
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
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            //save image to shared preference
            getData()
        }
    }

    override fun onClickSong(audioInfo: AudioInfo, position: Int) {
        Toast.makeText(this, "name: ${audioInfo.display_name}", Toast.LENGTH_SHORT).show()
        mPosition = position
        try {
            player.reset()
            player.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player.setDataSource(audioInfo.path)
            player.prepare()

        } catch (e: Exception) {
            Log.d("hai.bui1", "onClickSong: ")
        }
        player.start()
        //updateSeekbar()
        seekBar.max = player.duration
        tv_time_left.setText("00:00")
        tv_time_right.setText("${player.duration/60000}:${(player.duration%60000)/1000}")
        play.setImageResource(R.drawable.ic_pause)

        lifecycleScope.launch {
            while (true) {
                delay(1000)
                seekBar.progress = player.currentPosition
            }
        }
    }


}