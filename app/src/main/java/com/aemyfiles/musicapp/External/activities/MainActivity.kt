package com.aemyfiles.musicapp.External.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemyfiles.musicapp.Domain.AudioDatabase
import com.aemyfiles.musicapp.Domain.AudioInfo
import com.aemyfiles.musicapp.Domain.AudioRepository
import com.aemyfiles.musicapp.External.adapter.ShowListSongAdapter
import com.aemyfiles.musicapp.Presenter.AudioViewModel
import com.aemyfiles.musicapp.Presenter.AudioViewModelFactory
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ShowListSongAdapter.AudioClickListener {
    lateinit var audioViewModel: AudioViewModel
    lateinit var player: MediaPlayer
    private var listSongs = ArrayList<AudioInfo>()
    private var mPosition: Int = 0;
    private val mHanlder: Handler = Handler()

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
            if (isPermissionsAllowed()) getData()
            else askForPermissions()
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
            tv_time_right.setText("${player.duration/60000}:${player.duration%60000}")
        } catch (e: java.lang.Exception) {

        }
    }

    @SuppressLint("Range")
    private fun getDataFromMedia(): List<AudioInfo> {
        val list = ArrayList<AudioInfo>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DISPLAY_NAME
        )

        val cursor = applicationContext.contentResolver.query(uri, projection, null, null, null)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val dur = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
                if (dur / 1000 < 60 || size / 1000000 < 0) {
                    continue
                }
                val songId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                val artirstId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val date = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED))
                val song = AudioInfo(songId, path, albumId, album, artirstId, artist, dur, size, date, title)
                list.add(song)
            }
            cursor.close()
        }
        return list
    }

    private fun isPermissionsAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
                ActivityCompat.requestPermissions(
                    this as Activity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            return false
        }
        return true
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
                    askForPermissions()
                }
                return
            }
        }
    }

    private fun getData() {
        val listSong = getDataFromMedia()
        listSong?.let {
            val allSong = it.toMutableList()
            allSong.forEach { it -> audioViewModel.insert(it) }
        }
        Log.d("hai.bui1", "onCreate: ")
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
        tv_time_right.setText("${player.duration/60000}:${player.duration%60000}")
        play.setImageResource(R.drawable.ic_pause)
    }

    fun updateSeekbar() {
        mHanlder.postDelayed(runnable, 1000)
    }

    var runnable: Runnable = object : Runnable {
        override fun run() {
            if (player.isPlaying) {
                tv_time_left!!.text = "${player.currentPosition/60000}:${player.currentPosition%60000}"
                seekBar!!.progress = player.currentPosition
                mHanlder.postDelayed(this, 1000)
            }
        }
    }

}