package com.aemyfiles.musicapp.Presenter

import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aemyfiles.musicapp.Domain.AlbumInfo
import com.aemyfiles.musicapp.Domain.AudioInfo
import com.aemyfiles.musicapp.Domain.AudioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioViewModel(private val repository: AudioRepository): ViewModel() {

    fun getAllAudio() = repository.getAllAudio()
    val mListAlbum = MutableLiveData<List<AlbumInfo>>()

    fun insert(audioInfo: AudioInfo) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(audioInfo)
    }

    fun delete(audioInfo: AudioInfo) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(audioInfo)
    }

    fun update(audioInfo: AudioInfo) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(audioInfo)
    }

    fun getAllAlbumFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            val listAlbum = ArrayList<AlbumInfo>()
            val cursor = repository.getAllAlbum()
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val album_name = cursor.getString(0)
                    val total_song = cursor.getInt(1)
                    val newAlbum = AlbumInfo(album_name, total_song)
                    listAlbum.add(newAlbum)
                }
                cursor.close()
            }
            mListAlbum.postValue(listAlbum)
        }

    }

    fun getListAlBum(): MutableLiveData<List<AlbumInfo>> {
        return mListAlbum
    }
}