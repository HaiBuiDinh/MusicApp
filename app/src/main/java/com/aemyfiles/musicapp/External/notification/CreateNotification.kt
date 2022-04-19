package com.aemyfiles.musicapp.External.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
import com.aemyfiles.musicapp.External.services.AudioService
import com.aemyfiles.musicapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.NotificationTarget
import com.bumptech.glide.request.transition.Transition

class CreateNotification {
    companion object {
        const val CHANEL_ID: String = "chanel1"
        const val ACTION_PREVIOUS: String = "actionprevious"
        const val ACTION_PLAY: String = "actionplay"
        const val ACTION_NEXT: String = "actionnext"
        const val ACTION_PAUSE: String = "actionpause"
        const val ACTION_STOP_SERVICE: String = "actionstopservice"

        private var mNotificationBuilder: NotificationCompat.Builder? = null
        private var mNotification: Notification? = null
        private var mNotificationManager: NotificationManager? = null
    }

    @SuppressLint("RemoteViewLayout")
    fun createNotification(context: Context, service: AudioService) {
        mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANEL_ID,
                "WTF",
                NotificationManager.IMPORTANCE_LOW
            )
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.enableVibration(false)
            mNotificationManager!!.createNotificationChannel(
                channel
            )
        }

        mNotificationBuilder =
            NotificationCompat.Builder(
                context,
                CHANEL_ID
            )

        val notificationLayout =
            RemoteViews(context.packageName, R.layout.collaps_notification_layout)
        val notificationLayoutExpand =
            RemoteViews(context.packageName, R.layout.expand_notification_layout)

        val intentPrevious = Intent(ACTION_PREVIOUS)
        val intentPlay = Intent(ACTION_PLAY)
        val intentNext = Intent(ACTION_NEXT)
        val intentPause = Intent(ACTION_PAUSE)
        val intentStopService = Intent(ACTION_STOP_SERVICE)

        notificationLayout.setOnClickPendingIntent(R.id.noti_collaps_previous, PendingIntent.getBroadcast(context, R.id.noti_collaps_previous, intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT))
        notificationLayout.setOnClickPendingIntent(R.id.noti_collaps_play, PendingIntent.getBroadcast(context, 0, if (service.mPlayer.isPlaying()) intentPause else intentPlay, PendingIntent.FLAG_UPDATE_CURRENT))
        notificationLayout.setOnClickPendingIntent(R.id.noti_collaps_next, PendingIntent.getBroadcast(context, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT))
        notificationLayout.setOnClickPendingIntent(R.id.noti_collaps_delete, PendingIntent.getBroadcast(context, 0, intentStopService, PendingIntent.FLAG_UPDATE_CURRENT))
        notificationLayout.setTextViewText(R.id.noti_collaps_song_name, service.mPlayer.mQueue[service.mPlayer.mCurrentPosSong].display_name)
        notificationLayout.setTextViewText(R.id.noti_collaps_artis, service.mPlayer.mQueue[service.mPlayer.mCurrentPosSong].artist_name)
        notificationLayout.setImageViewResource(R.id.noti_collaps_play, if (service.mPlayer.isPlaying()) R.drawable.ic_pause else R.drawable.ic_play)
//        notificationLayout.setImageViewBitmap(R.id.noti_collaps_song_thumbnail, )

        notificationLayoutExpand.setOnClickPendingIntent(R.id.noti_expand_previous, PendingIntent.getBroadcast(context, 0, intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT))
        notificationLayoutExpand.setOnClickPendingIntent(R.id.noti_expand_play, PendingIntent.getBroadcast(context, 0, if (service.mPlayer.isPlaying()) intentPause else intentPlay, PendingIntent.FLAG_UPDATE_CURRENT))
        notificationLayoutExpand.setOnClickPendingIntent(R.id.noti_expand_next, PendingIntent.getBroadcast(context, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT))
        notificationLayoutExpand.setOnClickPendingIntent(R.id.noti_expand_delete, PendingIntent.getBroadcast(context, 0, intentStopService, PendingIntent.FLAG_UPDATE_CURRENT))
        notificationLayoutExpand.setTextViewText(R.id.noti_expand_song_name, service.mPlayer.mQueue[service.mPlayer.mCurrentPosSong].display_name)
        notificationLayoutExpand.setTextViewText(R.id.noti_expand_artis, service.mPlayer.mQueue[service.mPlayer.mCurrentPosSong].artist_name)
        notificationLayoutExpand.setImageViewResource(R.id.noti_expand_play, if (service.mPlayer.isPlaying()) R.drawable.ic_pause else R.drawable.ic_play)

        //create notification
        mNotification = mNotificationBuilder!!
            .setAutoCancel(false)
            .setContentTitle("Music app")
            .setDefaults(Notification.DEFAULT_SOUND)
            .setSmallIcon(R.drawable.ic_play)
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayoutExpand)
            .setPriority(Notification.PRIORITY_MAX)
            .setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
            .setNumber(-100)
            .build()
        mNotification!!.flags = mNotification!!.flags or Notification.FLAG_NO_CLEAR

        mNotificationManager!!.notify(1, mNotification)


        service.startForeground(1, mNotification)
    }

}