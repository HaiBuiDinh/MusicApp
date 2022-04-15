package com.aemyfiles.musicapp.External.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.aemyfiles.musicapp.Domain.AudioInfo
import com.aemyfiles.musicapp.External.utils.MyPlayer

class AudioService : Service() {

    lateinit var mPlayer: MyPlayer
    lateinit var mBinder: Binder

    override fun onCreate() {
        mPlayer = MyPlayer(applicationContext)
        mBinder = MyBinder()
        super.onCreate()
    }


    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mPlayer.stop()
        return super.onUnbind(intent)
    }


    inner class MyBinder : Binder() {
        fun getService(): AudioService {
            return this@AudioService
        }
    }
}