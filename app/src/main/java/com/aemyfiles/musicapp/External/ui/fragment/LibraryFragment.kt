package com.aemyfiles.musicapp.External.ui.fragment

import android.util.Log
import android.view.View
import com.aemyfiles.musicapp.External.ui.adapter.PageAdapter
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.Presenter.controller.LibraryController
import com.aemyfiles.musicapp.Presenter.dagger2.DaggerAppComponent
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.fragment_library_layout.*


class LibraryFragment(private val mService: MediaPlayService): AbsFragment<LibraryController>() {

    override fun initController() {
        val component = DaggerAppComponent.builder().application(activity!!.application).activity(activity!!).build()
        component.inject(this)
    }

    override val resourceId: Int
        get() = R.layout.fragment_library_layout

    override fun updateContentView(isServiceConnected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun initUI(view: View?) {
        val mAdapter = PageAdapter(childFragmentManager, mService)
        viewPager.adapter = mAdapter
        viewPager.offscreenPageLimit = tabLayout.tabCount
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onDestroy() {
        Log.d("hai.bui1", "onDestroy: ")
        super.onDestroy()
    }

}