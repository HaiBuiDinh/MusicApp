package com.aemyfiles.musicapp.Presenter.controller

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.RecentInfo
import com.aemyfiles.musicapp.External.repository.HomeRepository
import com.aemyfiles.musicapp.Presenter.AlbumViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeController @Inject constructor(private val homeRepository: HomeRepository): ViewModel(), AlbumViewModel {

    var listAlbums = MutableLiveData<List<AlbumInfo>>()

    suspend fun insert(recentInfo: RecentInfo) = viewModelScope.launch { homeRepository.insert(recentInfo) }

    fun getRecentPlaylist() = homeRepository.getRecentPlaylist()
    fun getRecentSong() = homeRepository.getRecentSong()
    fun getListSongWhenRecentEmpty() = homeRepository.getListSongWhenRecentEmpty()

    override fun getListAlBum() : MutableLiveData<List<AlbumInfo>> {
        viewModelScope.launch (Dispatchers.IO){
            listAlbums.postValue(homeRepository.getAllAlbum())
        }
        return listAlbums
    }

    override fun updateThumbnail(album_id: Int, thumbnail: String) {
        viewModelScope.launch (Dispatchers.IO){
            homeRepository.updateThumbnail(album_id, thumbnail)
        }
    }
}