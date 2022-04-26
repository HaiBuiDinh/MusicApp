package com.aemyfiles.musicapp.External.fragment.pagerfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemyfiles.musicapp.External.adapter.ShowListAlbumAdapter
import com.aemyfiles.musicapp.External.adapter.ShowListSongAdapter
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.Presenter.MusicViewModel
import com.aemyfiles.musicapp.Presenter.controller.LibraryController
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.fragment_all_album_layout.view.*
import kotlinx.android.synthetic.main.fragment_all_song_layout.view.*

class AlbumFragment(): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = layoutInflater.inflate(R.layout.fragment_all_album_layout, container, false)
        return view
    }

//    private fun initView(view: View) {
//        val mAdapter = ShowListAlbumAdapter()
//        view.recycler_all_album?.apply {
//            layoutManager = GridLayoutManager(activity, 2, false)
//            adapter = mAdapter
//        }
//    }
}