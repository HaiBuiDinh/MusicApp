package com.aemyfiles.musicapp.Presenter.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.RecentInfo
import com.aemyfiles.musicapp.External.repository.HomeRepository
import com.aemyfiles.musicapp.Presenter.AlbumViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeController(private val homeRepository: HomeRepository): ViewModel(), AlbumViewModel {

    suspend fun insert(recentInfo: RecentInfo) = viewModelScope.launch { homeRepository.insert(recentInfo) }

    fun getRecentPlaylist() = homeRepository.getRecentPlaylist()
    fun getRecentSong() = homeRepository.getRecentSong()
    fun getListSongWhenRecentEmpty() = homeRepository.getListSongWhenRecentEmpty()

    override fun updateAlbum(albumInfo: AlbumInfo) {
        viewModelScope.launch (Dispatchers.IO){
            homeRepository.updateAlbum(albumInfo)
        }
    }
    override fun getListAlBum() = homeRepository.getAllAlbum()
    override fun getListSongByAlbumId(album_id: Int) = homeRepository.getListSongByAlbumId(album_id)
}