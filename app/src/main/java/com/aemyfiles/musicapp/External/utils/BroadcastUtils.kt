package com.aemyfiles.musicapp.External.utils

import android.content.Context
import android.content.Intent

class BroadcastUtils {
    companion object{
        fun sendAction(context: Context, action:String){
            val intent = Intent().setAction(action)
            context.sendBroadcast(intent)
        }
    }
}