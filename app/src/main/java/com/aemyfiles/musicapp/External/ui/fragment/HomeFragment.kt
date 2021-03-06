package com.aemyfiles.musicapp.External.ui.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.RecentInfo
import com.aemyfiles.musicapp.External.ui.activities.MainActivity
import com.aemyfiles.musicapp.External.ui.adapter.RecentPlaylistAdapter
import com.aemyfiles.musicapp.External.ui.adapter.ShowListAlbumAdapter
import com.aemyfiles.musicapp.External.ui.adapter.ShowListSongAdapter
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.utils.PermissionUtils
import com.aemyfiles.musicapp.Presenter.AlbumViewModel
import com.aemyfiles.musicapp.Presenter.controller.HomeController
import com.aemyfiles.musicapp.Presenter.dagger2.DaggerAppComponent
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.fragment_home_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment(val mService: MediaPlayService) :
    AbsFragment<HomeController>() {
    lateinit var mPlayListAdapter: RecentPlaylistAdapter
    lateinit var mRecentSongAdapter: ShowListSongAdapter
    lateinit var mAlbumAdapter: ShowListAlbumAdapter

    override fun initController() {
        val component = DaggerAppComponent.builder().application(activity!!.application).activity(activity!!).build()
        component.inject(this)
    }


    override fun updateContentView(isServiceConnected: Boolean) {
        mRecentSongAdapter.notifyDataSetChanged()
    }

    private fun updateContent() {
        mController?.getRecentPlaylist()?.observe(this@HomeFragment, {
            Log.d("hai.bui1", "initUI: ${it.size}")
            if (it.size <= 0) {
                GlobalScope.launch(Dispatchers.IO) {
                    var list = mController?.getListSongWhenRecentEmpty();
                    withContext(Dispatchers.Main) {
                        mRecentSongAdapter.setData(list!!)
                    }
                }
            }
        })
    }

    override fun initUI(view: View?) {
        mPlayListAdapter = RecentPlaylistAdapter()
        recycler_recent_playlist!!.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mPlayListAdapter
        }

        mRecentSongAdapter = ShowListSongAdapter(mService)
        recycler_recent_song!!.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = mRecentSongAdapter
        }

        mAlbumAdapter = ShowListAlbumAdapter(mController as AlbumViewModel, activity!!, mService)
        recycler_recent_album!!.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAlbumAdapter
        }

        mController?.getListAlBum()?.observe(this, Observer {
            it?.let { mAlbumAdapter.setData(it as ArrayList<AlbumInfo>) }
            Log.d("hai.bui1", "getAlbum: onChanged" + it.size)
        })


        mController?.getRecentPlaylist()?.observe(this, Observer {
            val listTemp = ArrayList<RecentInfo>()
            listTemp.addAll(it)
            if (listTemp.isEmpty()) {
                //fake data
                listTemp.add(RecentInfo(0, 0, "My playlist 2", 65, true))
                listTemp.add(RecentInfo(1, 1, "My playlist 3", 54, true))
                listTemp.add(RecentInfo(2, 2, "My playlist 4", 81, true))
                listTemp.add(RecentInfo(3, 3, "My playlist 5", 9, true))
                listTemp.add(RecentInfo(4, 4, "My playlist 6", 64, true))
                listTemp.add(RecentInfo(5, 5, "My playlist 7", 12, true))
            }
            mPlayListAdapter.setData(listTemp)
        })

        mMainController.isSyncFinish.observe(this, {
            if (it) {
                updateContent()
            }
        })
        if (PermissionUtils.isPermissionsAllowed(activity!!)) {
            updateContent()
        }

        registerReceiver()
    }

    override val resourceId: Int
        get() = R.layout.fragment_home_layout


    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("hai.bui1", "onReceive: home ")
            updateContentView(false)
        }
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(MainActivity.UPDATE_LAYOUT)
        activity!!.applicationContext.registerReceiver(broadcastReceiver, intentFilter)
    }
}