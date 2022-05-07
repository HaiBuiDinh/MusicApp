package com.aemyfiles.musicapp.Presenter.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import com.aemyfiles.musicapp.External.repository.LibraryRepository
import com.aemyfiles.musicapp.Presenter.AlbumViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LibraryController @Inject constructor(private val libraryRepository: LibraryRepository): ViewModel(), AlbumViewModel {

    val listAlbums = MutableLiveData<List<AlbumInfo>>()

    fun getAllSong() : LiveData<List<SongInfo>> {
        return libraryRepository.getAllSong()
    }

    override fun getListAlBum(): MutableLiveData<List<AlbumInfo>> {
        viewModelScope.launch (Dispatchers.IO) {
            listAlbums.postValue(libraryRepository.getAllAlbum())
        }
        return listAlbums
    }

    override fun getListSongByAlbumId(album_id: Int): List<SongInfo> = libraryRepository.getListSongByAlbumId(album_id)

    override fun updateThumbnail(album_id: Int, thumbnail: String) {
        viewModelScope.launch (Dispatchers.IO) {
            libraryRepository.updateThumbnail(album_id, thumbnail)
        }
    }
}