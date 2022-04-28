package com.aemyfiles.musicapp.External.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.utils.ItemType
import com.aemyfiles.musicapp.External.utils.ThumbnailManager
import com.aemyfiles.musicapp.R

class ShowListSongAdapter(var mService: MediaPlayService) :
    RecyclerView.Adapter<ShowListSongAdapter.ViewHolder>() {

    private var mSongs: List<SongInfo> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_song_name)
        val imageView: ImageView = itemView.findViewById(R.id.thumbnail_song)
        val artis: TextView = itemView.findViewById(R.id.tv_song_artis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song: SongInfo = mSongs[position]
        ThumbnailManager.getInstance().loadThumbnail(ThumbnailManager.ThumbnailInfo(holder.imageView, song.id, song.path, 320, ItemType.SONG_TYPE))
        holder.textView.text = song.display_name
        holder.imageView.clipToOutline = true
        var context = holder.textView.context
        holder.textView.setTextColor(context.getColor(if (isPlaying(song)) android.R.color.holo_blue_light else android.R.color.black))
        holder.artis.text = song.artist_name
        holder.itemView.setOnClickListener {
            mService.mPlayer.mQueue.clear()
            //TO-DO: kiểm tra xem mQueue của mPlayer có khác không rồi hãy add
            mService.mPlayer.mQueue.addAll(mSongs)
            mService.mPlayer.mCurrentPosSong = position
            mService.onTrackPlay(true)
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