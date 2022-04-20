package com.aemyfiles.musicapp.Presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aemyfiles.musicapp.Domain.AlbumInfo
import com.aemyfiles.musicapp.Domain.SongInfo
import com.aemyfiles.musicapp.Domain.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel(private val repository: MusicRepository) : ViewModel() {

    var mListAudio = repository.getAllSong()
    var mListAlbum = repository.getAllAlbum()

    fun insert(songInfo: SongInfo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(songInfo)
    }

    fun delete(songInfo: SongInfo) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(songInfo)
    }

    fun update(songInfo: SongInfo) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(songInfo)
    }

    fun updateAlbum(albumInfo: AlbumInfo) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateAlbum(albumInfo)
    }

    fun getAllAudio(): LiveData<List<SongInfo>> {
        return mListAudio
    }

    fun getListAlBum(): LiveData<List<AlbumInfo>> {
        return mListAlbum
    }

    fun getListSongByAlbumId(album_id: Int): List<SongInfo> {
        return repository.getListSongByAlbumId(album_id)
    }
}