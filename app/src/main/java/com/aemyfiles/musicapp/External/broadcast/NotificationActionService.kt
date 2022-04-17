package com.aemyfiles.musicapp.External.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActionService: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.sendBroadcast(Intent("TRACK-TRACK").putExtra("actionname", intent?.action))
    }
}