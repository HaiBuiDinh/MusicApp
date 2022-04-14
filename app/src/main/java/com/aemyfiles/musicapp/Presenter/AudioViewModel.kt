package com.aemyfiles.musicapp.Presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aemyfiles.musicapp.Domain.AudioInfo
import com.aemyfiles.musicapp.Domain.AudioRepository
import kotlinx.coroutines.launch

class AudioViewModel(private val repository: AudioRepository): ViewModel() {

    fun getAllAudio() = repository.getAllAudio()

    fun insert(audioInfo: AudioInfo) = viewModelScope.launch {
        repository.insert(audioInfo)
    }

    fun delete(audioInfo: AudioInfo) = viewModelScope.launch {
        repository.delete(audioInfo)
    }

    fun update(audioInfo: AudioInfo) = viewModelScope.launch {
        repository.update(audioInfo)
    }

}