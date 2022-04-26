package com.aemyfiles.musicapp.External.fragment

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemyfiles.musicapp.Domain.MusicApplication
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.RecentInfo
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import com.aemyfiles.musicapp.External.adapter.CenterZoomLayoutManager
import com.aemyfiles.musicapp.External.adapter.RecentPlaylistAdapter
import com.aemyfiles.musicapp.External.adapter.ShowListAlbumAdapter
import com.aemyfiles.musicapp.External.adapter.ShowListSongAdapter
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.utils.Permission
import com.aemyfiles.musicapp.Presenter.MusicViewModel
import com.aemyfiles.musicapp.Presenter.MusicViewModelFactory
import com.aemyfiles.musicapp.Presenter.controller.HomeController
import com.aemyfiles.musicapp.R
import kotlinx.android.synthetic.main.fragment_home_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment(val mMusicViewModel: MusicViewModel, val mService: MediaPlayService) :
    AbsFragment<HomeController>() {
    lateinit var mPlayListAdapter: RecentPlaylistAdapter
    lateinit var mRecentSongAdapter: ShowListSongAdapter
    lateinit var mAlbumAdapter: ShowListAlbumAdapter

    override fun initController(): HomeController {
        val homeController by viewModels<HomeController> {
            MusicViewModelFactory((activity!!.application as MusicApplication).homeRepository)
        }
        return homeController
    }


    override fun updateContentView(isServiceConnected: Boolean) {
        TODO("Not yet implemented")
    }

    private fun updateContent() {
        controller?.getRecentPlaylist()?.observe(this@HomeFragment, {
            Log.d("hai.bui1", "initUI: ${it.size}")
            if (it.size <= 0) {
                GlobalScope.launch(Dispatchers.IO) {
                    var list = controller?.getListSongWhenRecentEmpty();
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

        mAlbumAdapter = ShowListAlbumAdapter(mMusicViewModel)
        recycler_recent_album!!.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAlbumAdapter
        }

        mMusicViewModel.getListAlBum().observe(this, Observer {
            it?.let { mAlbumAdapter.setData(it as ArrayList<AlbumInfo>) }
            Log.d("hai.bui1", "getAlbum: onChanged" + it.size)
        })


        controller?.getRecentPlaylist()?.observe(this, Observer {
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

        mMusicViewModel.isSyncFinish.observe(this, {
            if (it) {
                updateContent()
            }
        })
        if(Permission.isPermissionsAllowed(activity!!)){
            updateContent()
        }
    }

    override val resourceId: Int
        get() = R.layout.fragment_home_layout


}