package com.aemyfiles.musicapp.External.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.aemyfiles.musicapp.Domain.AudioInfo
import com.aemyfiles.musicapp.External.broadcast.NotificationActionService
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.activity_main.view.*

class CreateNotification {
    companion object{
        const val CHANEL_ID: String = "chanel1"
        const val ACTION_PREVIOUS: String = "actionprevious"
        const val ACTION_PLAY: String = "actionplay"
        const val ACTION_NEXT: String = "actionnext"
    }

    lateinit var notification: Notification

    public fun createNotification(context: Context, audioInfo: AudioInfo, position: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(context)
            val mediaSessionCompat: MediaSessionCompat = MediaSessionCompat(context, "tag")

            var icon: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_pause)

            val intentPrevious = Intent(context, NotificationActionService::class.java).setAction(ACTION_PREVIOUS)
            val pendingIntentPrevious: PendingIntent = PendingIntent.getBroadcast(context, 0, intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT)
            val drw_previous: Int = R.drawable.ic_previous

            val intentPlay = Intent(context, NotificationActionService::class.java).setAction(ACTION_PLAY)
            val pendingIntentPlay: PendingIntent = PendingIntent.getBroadcast(context, 0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT)
            val drw_play: Int = R.drawable.ic_play

            val intentNext = Intent(context, NotificationActionService::class.java).setAction(ACTION_NEXT)
            val pendingIntentNext: PendingIntent = PendingIntent.getBroadcast(context, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT)
            val drw_next: Int = R.drawable.ic_next

            //create notification
            //.setLargeIcon() ảnh thu nhỏ của bài hát
            notification = NotificationCompat.Builder(context, CHANEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(audioInfo.display_name)
                    .setContentText(audioInfo.artist_name)
                    .setOnlyAlertOnce(true) //show notification for only first time
                    .setShowWhen(false)
                    .addAction(drw_previous, "Previous", pendingIntentPrevious)
                    .addAction(drw_play, "Play", pendingIntentPlay)
                    .addAction(drw_next, "Next", pendingIntentNext)
                    .setStyle( androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSessionCompat.sessionToken))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build()

            notificationManagerCompat.notify(1, notification)
        }
    }
}