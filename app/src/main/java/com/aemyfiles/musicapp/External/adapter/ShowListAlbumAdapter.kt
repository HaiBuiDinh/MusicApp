package com.aemyfiles.musicapp.External.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.External.utils.ItemType
import com.aemyfiles.musicapp.External.utils.ThumbnailManager
import com.aemyfiles.musicapp.Presenter.AlbumViewModel
import com.aemyfiles.musicapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowListAlbumAdapter(private val mViewModel: AlbumViewModel) : RecyclerView.Adapter<ShowListAlbumAdapter.ViewHolder>(){

    private var mListAlbum : List<AlbumInfo> = ArrayList()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.title_album)
        val imageView: ImageView = itemView.findViewById(R.id.thumbnail_album)
        val textViewTotal: TextView = itemView.findViewById(R.id.total_song)
    }

    fun setData(listAlbum: List<AlbumInfo>) {
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

        holder.textView.text = albumInfo.album_name
        holder.textViewTotal.text = "${albumInfo.total_song} songs"
        holder.imageView.clipToOutline = true
        if (albumInfo.thumbnail.equals("")) {
            albumInfo.thumbnail = "NOTHUMBNAIL"
            GlobalScope.launch(Dispatchers.IO) {
                val listSong = mViewModel.getListSongByAlbumId(albumInfo.album_id)
                withContext(Dispatchers.Main) {
                    val thumbnailCallback = object : ThumbnailManager.ThumbnailCallback {
                        override fun onSuccess(albumId: Int, path: String?) {
                            if (albumInfo.thumbnail.equals("NOTHUMBNAIL")) {
                                albumInfo.thumbnail = path!!
                                GlobalScope.launch(Dispatchers.IO) {
                                    mViewModel.updateAlbum(albumInfo)
                                }
                            }
                        }
                    }
                    for (it in listSong) {
                        if (albumInfo.thumbnail.equals("NOTHUMBNAIL"))
                            ThumbnailManager.getInstance().loadThumbnail(
                                ThumbnailManager.ThumbnailInfo(
                                    it.id,
                                    it.path,
                                    320,
                                    ItemType.ALBUM_TYPE,
                                    thumbnailCallback
                                )
                            )
                        else break
                    }
                }
            }
            Log.d("long.vt", "ShowListALbumAdapter onBindViewHolder: " + albumInfo.thumbnail)
        }
        if (albumInfo.thumbnail.equals("NOTHUMBNAIL"))
            holder.imageView.setImageResource(R.drawable.ic_groove)
        else ThumbnailManager.getInstance().loadThumbnail(
            ThumbnailManager.ThumbnailInfo(
                holder.imageView,
                albumInfo.album_id,
                albumInfo.thumbnail,
                320,
                ItemType.ALBUM_TYPE
            )
        )
    }

    override fun getItemCount(): Int {
        return if(mListAlbum == null) 0 else mListAlbum.size
    }
}