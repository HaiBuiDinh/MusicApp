package com.aemyfiles.musicapp.External.ui.adapter

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.ItemType
import com.aemyfiles.musicapp.External.FragmentUtils
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.thumbnail.ThumbnailManager
import com.aemyfiles.musicapp.External.ui.fragment.DetailFragment
import com.aemyfiles.musicapp.Presenter.AlbumViewModel
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.item_album_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

class ShowListAlbumAdapter(
    private val mViewModel: AlbumViewModel,
    val mActivity: FragmentActivity,
    val mService: MediaPlayService
) : RecyclerView.Adapter<ShowListAlbumAdapter.ViewHolder>() {

    private var mListAlbum: List<AlbumInfo> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setData(listAlbum: List<AlbumInfo>) {
        listAlbum?.let {
            mListAlbum = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_album_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val albumInfo: AlbumInfo = mListAlbum[position]

        holder.itemView.tv_title_album.text = albumInfo.album_name
        holder.itemView.tv_total_song.text = "${albumInfo.total_song} songs"
        holder.itemView.im_thumbnail_album.clipToOutline = true
        if (albumInfo.thumbnail.equals("")) {
            albumInfo.thumbnail = "NOTHUMBNAIL"
            GlobalScope.launch(Dispatchers.IO) {
                val listSong = mViewModel.getListSongByAlbumId(albumInfo.album_id)
                withContext(Dispatchers.Main) {
                    var isDone = AtomicBoolean(false)
                    val thumbnailCallback = object : ThumbnailManager.ThumbnailCallback {
                        override fun onSuccess(albumId: Int, path: String?, bitmap: Bitmap) {
                            if (!isDone.get()) {
                                albumInfo.thumbnail = path!!
                                isDone.set(true)
                                GlobalScope.launch(Dispatchers.IO) {
                                    mViewModel.updateAlbum(albumInfo)
                                }
                            }
                        }
                    }
                    for (it in listSong) {
                        if (isDone.get()) break
                        ThumbnailManager.getInstance().loadThumbnail(
                            ThumbnailManager.ThumbnailInfo(
                                it.id,
                                it.path,
                                320,
                                ItemType.ALBUM_TYPE,
                                thumbnailCallback
                            )
                        )
                    }
                }
            }
            Log.d("long.vt", "ShowListALbumAdapter onBindViewHolder: " + albumInfo.thumbnail)
        }
        if (albumInfo.thumbnail.equals("NOTHUMBNAIL"))
            holder.itemView.im_thumbnail_album.setImageResource(R.drawable.ic_groove)
        else ThumbnailManager.getInstance().loadThumbnail(
            ThumbnailManager.ThumbnailInfo(
                holder.itemView.im_thumbnail_album,
                albumInfo.album_id,
                albumInfo.thumbnail,
                320,
                ItemType.ALBUM_TYPE
            )
        )
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var bundle = Bundle();
                bundle.putInt(DetailFragment.ID, mListAlbum[position].album_id)
                bundle.putSerializable(DetailFragment.TYPE, ItemType.ALBUM_TYPE)
                bundle.putString(DetailFragment.TITLE, mListAlbum[position].album_name)
                bundle.putString(DetailFragment.THUMBNAIL, mListAlbum[position].thumbnail)
                FragmentUtils.enterFragment(mActivity, DetailFragment(mService), true, bundle)
            }
        })
    }

    override fun getItemCount(): Int {
        return if (mListAlbum == null) 0 else mListAlbum.size
    }
}