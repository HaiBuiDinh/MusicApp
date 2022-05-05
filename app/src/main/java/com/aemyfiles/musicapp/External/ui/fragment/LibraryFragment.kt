package com.aemyfiles.musicapp.External.ui.fragment

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.aemyfiles.musicapp.Domain.MusicApplication
import com.aemyfiles.musicapp.External.ui.adapter.PageAdapter
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.Presenter.MusicViewModelFactory
import com.aemyfiles.musicapp.Presenter.controller.LibraryController
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.fragment_library_layout.*


class LibraryFragment(private val mService: MediaPlayService): AbsFragment<LibraryController>() {
    override fun initController(): LibraryController {
        val libraryController by viewModels<LibraryController> {
            MusicViewModelFactory((activity!!.application as MusicApplication).libraryRepository)
        }
        return libraryController
    }

    override val resourceId: Int
        get() = R.layout.fragment_library_layout

    override fun updateContentView(isServiceConnected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun initUI(view: View?) {
        val mAdapter = PageAdapter(childFragmentManager, controller as LibraryController, mService)
        viewPager.adapter = mAdapter
        viewPager.offscreenPageLimit = tabLayout.tabCount
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onDestroy() {
        Log.d("hai.bui1", "onDestroy: ")
        super.onDestroy()
    }

}