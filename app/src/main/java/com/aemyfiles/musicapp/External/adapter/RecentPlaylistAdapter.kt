package com.aemyfiles.musicapp.External.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aemyfiles.musicapp.Domain.entity.RecentInfo
import com.aemyfiles.musicapp.Presenter.controller.HomeController
import com.aemyfiles.musicapp.R

class RecentPlaylistAdapter(): RecyclerView.Adapter<RecentPlaylistAdapter.ViewHolder>() {
    private var mListPlayList = ArrayList<RecentInfo>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val thumbPlaylist = itemView.findViewById<ImageView>(R.id.thumbnail_playlist)
        val titlePlaylist = itemView.findViewById<TextView>(R.id.title_playlist)
        val totalSong = itemView.findViewById<TextView>(R.id.total_song_in_playlist)

    }

    fun setData(listPlayList: ArrayList<RecentInfo>) {
        listPlayList?.let {
            mListPlayList = listPlayList
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_playlist_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playList = mListPlayList[position]
        holder.itemView.clipToOutline = true
        holder.titlePlaylist.text = playList.name_entity
        holder.totalSong.text = "${playList.total} songs"
    }

    override fun getItemCount(): Int {
        return if (mListPlayList == null) 0 else mListPlayList.size
    }
}