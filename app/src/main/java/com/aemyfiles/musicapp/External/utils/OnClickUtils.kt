package com.aemyfiles.musicapp.External.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.aemyfiles.musicapp.Domain.entity.ItemType
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import com.aemyfiles.musicapp.External.FragmentUtils
import com.aemyfiles.musicapp.External.ui.activities.PlayingActivity
import com.aemyfiles.musicapp.External.notification.CreateNotification
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.ui.fragment.DetailFragment

class OnClickUtils {
    companion object {
        fun onClickDetail(activity:FragmentActivity, id :Int, type:ItemType, title:String, thumbnail:String, detailFragment: DetailFragment) {
            val bundle = Bundle()
            bundle.putInt(DetailFragment.ID, id)
            bundle.putSerializable(DetailFragment.TYPE, type)
            bundle.putString(DetailFragment.TITLE, title)
            bundle.putString(DetailFragment.THUMBNAIL, thumbnail)
            FragmentUtils.enterFragment(activity, detailFragment, true, bundle)
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