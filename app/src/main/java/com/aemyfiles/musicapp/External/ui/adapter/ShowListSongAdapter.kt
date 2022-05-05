package com.aemyfiles.musicapp.External.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import com.aemyfiles.musicapp.External.utils.OnClickUtils
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.Domain.entity.ItemType
import com.aemyfiles.musicapp.External.thumbnail.ThumbnailManager
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.item_song_layout.view.*

class ShowListSongAdapter(var mService: MediaPlayService) :
    RecyclerView.Adapter<ShowListSongAdapter.ViewHolder>() {

    private var mSongs: List<SongInfo> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song: SongInfo = mSongs[position]
        ThumbnailManager.getInstance().loadThumbnail(
            ThumbnailManager.ThumbnailInfo(holder.itemView.im_thumbnail, song.id, song.path, 320, ItemType.SONG_TYPE))
        holder.itemView.tv_song_name.text = song.display_name
        holder.itemView.im_thumbnail.clipToOutline = true
        var context = holder.itemView.context
        holder.itemView.tv_song_name.setTextColor(context.getColor(if (isPlaying(song)) android.R.color.holo_blue_light else android.R.color.black))
        holder.itemView.tv_song_artis.text = song.artist_name
        holder.itemView.setOnClickListener {
//            mService.mPlayer.mQueue.clear()
//            //TO-DO: kiểm tra xem mQueue của mPlayer có khác không rồi hãy add
//            mService.mPlayer.mQueue.addAll(mSongs)
//            mService.mPlayer.mCurrentPosSong = position
//            mService.onTrackPlay(true)
            OnClickUtils.onClickSong(context, mSongs, position, mService, false)
            notifyDataSetChanged()
        }
    }

    private fun isPlaying(song: SongInfo): Boolean {
        if(mService.mPlayer.mQueue.isEmpty()) return false
        if (mService.mPlayer.mQueue[mService.mPlayer.mCurrentPosSong].id == song.id) return true
        return false
    }

    override fun getItemCount(): Int {
        return if (mSongs == null) 0 else mSongs.size;
    }

    fun setData(songs: List<SongInfo>) {
        if (songs == null) return
        mSongs = songs
        notifyDataSetChanged()
    }

    interface SongListener {
        fun onClickSong()
    }
}