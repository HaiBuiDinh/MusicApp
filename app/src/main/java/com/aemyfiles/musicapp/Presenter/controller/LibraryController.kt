package com.aemyfiles.musicapp.Presenter.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import com.aemyfiles.musicapp.External.repository.LibraryRepository

class LibraryController(private val libraryRepository: LibraryRepository): ViewModel() {

    fun getAllSong() : LiveData<List<SongInfo>> {
        return libraryRepository.getAllSong()
    }

    fun getAllAlbum(): LiveData<List<AlbumInfo>> {
        return libraryRepository.getAllAlbum()
    }
}