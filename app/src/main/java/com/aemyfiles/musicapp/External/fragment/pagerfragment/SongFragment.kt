package com.aemyfiles.musicapp.External.fragment.pagerfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemyfiles.musicapp.External.adapter.ShowListSongAdapter
import com.aemyfiles.musicapp.External.repository.LibraryRepository
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.Presenter.controller.LibraryController
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.fragment_all_song_layout.*
import kotlinx.android.synthetic.main.fragment_all_song_layout.view.*

class SongFragment(private val libraryController: LibraryController, private val mService: MediaPlayService): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = layoutInflater.inflate(R.layout.fragment_all_song_layout, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        val mAdapter = ShowListSongAdapter(mService)
        view.recycler_all_song?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        libraryController.getAllSong().observe(this, Observer {
            mAdapter.setData(it)
        })
    }
}