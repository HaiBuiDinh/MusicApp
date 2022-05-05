package com.aemyfiles.musicapp.External.ui.fragment.pagerfragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemyfiles.musicapp.External.ui.activities.MainActivity
import com.aemyfiles.musicapp.External.ui.adapter.ShowListSongAdapter
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.Presenter.controller.LibraryController
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.fragment_all_song_layout.view.*

class SongFragment(private val libraryController: LibraryController, private val mService: MediaPlayService): Fragment() {
    lateinit var mAdapter: ShowListSongAdapter

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
        mAdapter = ShowListSongAdapter(mService)
        view.recycler_all_song?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        libraryController.getAllSong().observe(this, Observer {
            mAdapter.setData(it)
        })
        registerReceiver()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("hai.bui1", "onReceive: all song ")
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(MainActivity.UPDATE_LAYOUT)
        activity!!.applicationContext.registerReceiver(broadcastReceiver, intentFilter)
    }
}