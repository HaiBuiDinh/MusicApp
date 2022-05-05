package com.aemyfiles.musicapp.External.ui.fragment.pagerfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.ui.adapter.ShowListAlbumAdapter
import com.aemyfiles.musicapp.Presenter.AlbumViewModel
import com.aemyfiles.musicapp.Presenter.controller.LibraryController
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.fragment_all_album_layout.view.*

class AlbumFragment(val mController: LibraryController, val mService: MediaPlayService): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = layoutInflater.inflate(R.layout.fragment_all_album_layout, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        val mAdapter = ShowListAlbumAdapter(mController as AlbumViewModel, activity!!, mService)
        view.recycler_all_album.setHasFixedSize(true)
        view.recycler_all_album?.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = mAdapter
        }

        mController.getListAlBum().observe(this@AlbumFragment, {
            if(it.isNotEmpty()){
                mAdapter.setData(it)
            }
        })
    }
}