package com.aemyfiles.musicapp.External.ui.fragment.pagerfragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.ui.adapter.ShowListAlbumAdapter
import com.aemyfiles.musicapp.External.ui.fragment.AbsFragment
import com.aemyfiles.musicapp.Presenter.AlbumViewModel
import com.aemyfiles.musicapp.Presenter.controller.LibraryController
import com.aemyfiles.musicapp.Presenter.dagger2.DaggerAppComponent
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.fragment_all_album_layout.view.*

class AlbumFragment(val mService:MediaPlayService) : AbsFragment<LibraryController>(){

    override fun initController() {
        val component = DaggerAppComponent.builder().application(activity!!.application).activity(activity!!).build()
        component.inject(this)
    }

    override val resourceId: Int
        get() = R.layout.fragment_all_album_layout

    override fun updateContentView(isServiceConnected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun initUI(view: View?) {
        val mAdapter = ShowListAlbumAdapter(mController as AlbumViewModel, activity!!, mService)
        view!!.recycler_all_album.setHasFixedSize(true)
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