package com.aemyfiles.musicapp.Presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aemyfiles.musicapp.Domain.MusicRepository

class AudioViewModelFactory(private val repository: MusicRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MusicViewModel(repository) as T
    }
}