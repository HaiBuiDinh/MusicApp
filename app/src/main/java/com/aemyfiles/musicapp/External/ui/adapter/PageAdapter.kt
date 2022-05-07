package com.aemyfiles.musicapp.External.ui.adapter

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aemyfiles.musicapp.External.ui.fragment.pagerfragment.AlbumFragment
import com.aemyfiles.musicapp.External.ui.fragment.pagerfragment.FolderFragment
import com.aemyfiles.musicapp.External.ui.fragment.pagerfragment.PlaylistFragment
import com.aemyfiles.musicapp.External.ui.fragment.pagerfragment.SongFragment
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.Presenter.controller.LibraryController

class PageAdapter(fm: FragmentManager, private val mService: MediaPlayService): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SongFragment(mService)
            1 -> AlbumFragment(mService)
            2 -> PlaylistFragment()
            3 -> FolderFragment()
            else -> SongFragment(mService)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Song"
            1 -> return "Album"
            2 -> return "Playlist"
            3 -> return "Folder"
        }
        return super.getPageTitle(position)
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        Log.d("hai.bui1", "destroyItem: ")
        super.destroyItem(container, position, `object`)
    }
}