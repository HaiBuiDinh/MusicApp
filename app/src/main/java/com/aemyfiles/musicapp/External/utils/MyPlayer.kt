package com.aemyfiles.musicapp.External.utils

import android.content.Context
import android.media.MediaPlayer
import com.aemyfiles.musicapp.Domain.entity.SongInfo

class MyPlayer(val context: Context) {
    private var mPlayer: MediaPlayer = MediaPlayer()
    var mQueue: ArrayList<SongInfo> = ArrayList()
    var mCurrentPosSong: Int = -1;

    fun setDataSource(path: String) {
        mPlayer.setDataSource(path)
    }

    fun prepare() {
        mPlayer.prepare()
    }

    fun play() {
        mPlayer.start()
    }

    fun stop() {
        mPlayer.stop()
    }

    fun pause() {
        mPlayer.pause()
    }

    fun seekTo(sec: Int) {
        mPlayer.seekTo(sec) //seek to sec
    }
    
    fun reset() {
        mPlayer.reset()
    }

    fun isPlaying(): Boolean {
        return mPlayer.isPlaying
    }

    fun duration(): Int {
        return mPlayer.duration
    }

    fun currentPosition(): Int {
        return mPlayer.currentPosition
    }

    fun playNextSong() {
        mCurrentPosSong = if (mCurrentPosSong == mQueue.size - 1) 0 else mCurrentPosSong + 1
        playCurrentSong()
    }

    fun playPreviousSong() {
        mCurrentPosSong = if (mCurrentPosSong == 0) mQueue.size - 1 else mCurrentPosSong - 1
        playCurrentSong()
    }

    fun playCurrentSong() {
        var songInfo = mQueue[mCurrentPosSong]
        songInfo?.let {
            try {
                reset()
                setDataSource(it.path)
                prepare()
                play()
            } catch (e: java.lang.Exception) {

            }
        }
    }

}