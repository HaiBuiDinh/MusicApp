package com.aemyfiles.musicapp.Presenter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import com.aemyfiles.musicapp.External.repository.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicViewModel(private val repository: MusicRepository) : ViewModel() {

    var mListAudio = repository.getAllSong()
    var mListAlbum = repository.getAllAlbum()
    val isSyncFinish = MutableLiveData(false)

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

    fun syncFromProvider(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            isSyncFinish.postValue(false)
            repository.syncFromProvider(context)
            isSyncFinish.postValue(true)
        }
    }
}