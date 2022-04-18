package com.aemyfiles.musicapp.External.services

import android.app.ActivityManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.aemyfiles.musicapp.External.activities.MainActivity
import com.aemyfiles.musicapp.External.broadcast.NotificationActionService
import com.aemyfiles.musicapp.External.notification.CreateNotification
import com.aemyfiles.musicapp.External.notification.Playable
import com.aemyfiles.musicapp.External.utils.MyPlayer

class AudioService : Service(), Playable {

    lateinit var mPlayer: MyPlayer
    lateinit var mBinder: Binder
    lateinit var mReceiver: BroadcastReceiver

    override fun onCreate() {
        mPlayer = MyPlayer(applicationContext)
        mBinder = MyBinder()
        registerReceiver()
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

    private fun registerReceiver() {
        mReceiver = NotificationActionService(this)

        val intentFilter = IntentFilter()
        intentFilter.addAction(CreateNotification.ACTION_PLAY)
        intentFilter.addAction(CreateNotification.ACTION_PREVIOUS)
        intentFilter.addAction(CreateNotification.ACTION_NEXT)
        intentFilter.addAction(CreateNotification.ACTION_PAUSE)
        intentFilter.addAction(CreateNotification.ACTION_STOP_SERVICE)
        this.applicationContext.registerReceiver(mReceiver, intentFilter)
    }

    override fun onTrackPrevious() {
        mPlayer.playPreviousSong()
        CreateNotification().createNotification(applicationContext, this)
        sendBroadcast(Intent(MainActivity.UPDATE_LAYOUT))
    }

    override fun onTrackPlay(isPlayNewSong: Boolean) {
        if (isPlayNewSong) {
            mPlayer.playCurrentSong()
        } else mPlayer.play()
        CreateNotification().createNotification(applicationContext, this)
        sendBroadcast(Intent(MainActivity.UPDATE_LAYOUT))
    }

    override fun onTrackPause() {
        mPlayer.pause()
        CreateNotification().createNotification(applicationContext, this)
        sendBroadcast(Intent(MainActivity.UPDATE_LAYOUT))
    }

    override fun onTrackNext() {
        mPlayer.playNextSong()
        CreateNotification().createNotification(applicationContext, this)
        sendBroadcast(Intent(MainActivity.UPDATE_LAYOUT))
    }

    override fun onStopService() {
        if (isAppRunning(applicationContext, applicationContext.packageName)) {

        } else {
            stopSelf()
        }
    }


    fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val procInfos: List<ActivityManager.RunningAppProcessInfo> = activityManager.runningAppProcesses
        procInfos?.let {
            it.forEach {
                if (it.equals(packageName)) return true
            }
        }
        return false;
    }
}