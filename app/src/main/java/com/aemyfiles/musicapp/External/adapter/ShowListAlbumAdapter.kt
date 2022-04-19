package com.aemyfiles.musicapp.External.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.aemyfiles.musicapp.Domain.AlbumInfo
import com.aemyfiles.musicapp.R

class ShowListAlbumAdapter() : RecyclerView.Adapter<ShowListAlbumAdapter.ViewHolder>(){

    private var mListAlbum = ArrayList<AlbumInfo>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.title_album)
        val imageView: ImageView = itemView.findViewById(R.id.thumbnail_album)
    }

    fun setData(listAlbum: ArrayList<AlbumInfo>) {
        listAlbum?.let{
            mListAlbum = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val albumInfo: AlbumInfo = mListAlbum[position]


    }

    override fun getItemCount(): Int {
        return if(mListAlbum == null) 0 else mListAlbum.size
    }
}