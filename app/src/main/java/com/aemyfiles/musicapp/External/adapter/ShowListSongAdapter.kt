package com.aemyfiles.musicapp.External.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aemyfiles.musicapp.Domain.AudioInfo
import com.aemyfiles.musicapp.R

class ShowListSongAdapter(private val audioClickListener: AudioClickListener) : RecyclerView.Adapter<ShowListSongAdapter.ViewHolder>(){

    private var mSongs: List<AudioInfo> = ArrayList<AudioInfo>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_song_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song: AudioInfo = mSongs[position]
        holder.textView.text = song.display_name
        holder.itemView.setOnClickListener { audioClickListener.onClickSong(song, position) }
    }

    override fun getItemCount(): Int {
        return if (mSongs == null) 0 else mSongs.size;
    }

    fun setData(songs : List<AudioInfo>) {
        if (songs == null) return
        mSongs = songs
        notifyDataSetChanged()
    }

    interface AudioClickListener {
        fun onClickSong(audioInfo: AudioInfo, position: Int)
    }
}