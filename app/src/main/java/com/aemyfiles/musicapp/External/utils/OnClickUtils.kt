package com.aemyfiles.musicapp.External.utils

import android.content.Context
import android.content.Intent
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import com.aemyfiles.musicapp.External.ui.activities.PlayingActivity
import com.aemyfiles.musicapp.External.notification.CreateNotification
import com.aemyfiles.musicapp.External.services.MediaPlayService

class OnClickUtils {
    companion object {
        fun onClickDetail() {

        }

        fun onClickSong(context:Context, listSongs: List<SongInfo>, position: Int, musicService: MediaPlayService, needStartPlayActivity: Boolean) {
            musicService.mPlayer.mQueue.clear()
            //TO-DO: kiểm tra xem mQueue của mPlayer có khác không rồi hãy add
            musicService.mPlayer.mQueue.addAll(listSongs)
            musicService.mPlayer.mCurrentPosSong = position
            val intent = Intent().setAction(CreateNotification.ACTION_PLAY)
            intent.putExtra("isPlayNewSong", true)
            context.sendBroadcast(intent)
            if(needStartPlayActivity){
                context.startActivity(Intent(context, PlayingActivity::class.java))
            }
        }
    }
}