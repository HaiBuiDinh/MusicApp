package com.aemyfiles.musicapp.Presenter.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import com.aemyfiles.musicapp.External.repository.LibraryRepository
import com.aemyfiles.musicapp.Presenter.AlbumViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryController(private val libraryRepository: LibraryRepository): ViewModel(), AlbumViewModel {

    fun getAllSong() : LiveData<List<SongInfo>> {
        return libraryRepository.getAllSong()
    }

    fun getAllAlbum(): LiveData<List<AlbumInfo>> {
        return libraryRepository.getAllAlbum()
    }

    override fun updateAlbum(albumInfo: AlbumInfo) {
        viewModelScope.launch (Dispatchers.IO) {
            libraryRepository.updateAlbum(albumInfo)
        }
    }

    override fun getListAlBum(): LiveData<List<AlbumInfo>> = libraryRepository.getAllAlbum()

    override fun getListSongByAlbumId(album_id: Int): List<SongInfo> = libraryRepository.getListSongByAlbumId(album_id)
}