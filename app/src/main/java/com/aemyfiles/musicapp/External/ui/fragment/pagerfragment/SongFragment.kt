package com.aemyfiles.musicapp.External.ui.fragment.pagerfragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemyfiles.musicapp.External.ui.activities.MainActivity
import com.aemyfiles.musicapp.External.ui.adapter.ShowListSongAdapter
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.ui.fragment.AbsFragment
import com.aemyfiles.musicapp.Presenter.controller.LibraryController
import com.aemyfiles.musicapp.Presenter.dagger2.DaggerAppComponent
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.fragment_all_song_layout.view.*

class SongFragment(val mService:MediaPlayService) : AbsFragment<LibraryController>(){

    lateinit var mAdapter: ShowListSongAdapter

    override fun initController() {
        val component = DaggerAppComponent.builder().application(activity!!.application).activity(activity!!).build()
        component.inject(this)
    }

    override val resourceId: Int
        get() = R.layout.fragment_all_song_layout

    override fun initUI(view: View?) {
        mAdapter = ShowListSongAdapter(mService)
        view!!.recycler_all_song?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        mController.getAllSong().observe(this, Observer {
            mAdapter.setData(it)
        })
        registerReceiver()
    }

    override fun onDestroy() {
        activity!!.unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    override fun updateContentView(isServiceConnected: Boolean) {
        mAdapter.notifyDataSetChanged()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("hai.bui1", "onReceive: all song ")
            updateContentView(false)
        }
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(MainActivity.UPDATE_LAYOUT)
        activity!!.registerReceiver(broadcastReceiver, intentFilter)
    }
}