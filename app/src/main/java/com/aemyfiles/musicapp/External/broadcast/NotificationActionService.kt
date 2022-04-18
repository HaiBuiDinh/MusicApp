package com.aemyfiles.musicapp.External.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.aemyfiles.musicapp.External.notification.CreateNotification
import com.aemyfiles.musicapp.External.services.AudioService

class NotificationActionService(private val mService: AudioService): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.let {
            when(intent.action) {
                CreateNotification.ACTION_PREVIOUS -> {
                    Log.d("hai.bui1", "onReceive: previous")
                    mService.onTrackPrevious()
                }
                CreateNotification.ACTION_PLAY -> {
                    Log.d("hai.bui1", "onReceive: play")
                    mService.onTrackPlay(false)
                }
                CreateNotification.ACTION_NEXT -> {
                    Log.d("hai.bui1", "onReceive: next")
                    mService.onTrackNext()
                }
                CreateNotification.ACTION_PAUSE -> {
                    Log.d("hai.bui1", "onReceive: pause")
                    mService.onTrackPause()
                }
                CreateNotification.ACTION_STOP_SERVICE -> {
                    Log.d("hai.bui1", "onReceive: stop")
                    mService.onStopService()
                }
            }
        }
    }
}