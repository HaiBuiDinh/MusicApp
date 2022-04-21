package com.aemyfiles.musicapp.Presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aemyfiles.musicapp.External.repository.HomeRepository
import com.aemyfiles.musicapp.External.repository.MusicRepository
import com.aemyfiles.musicapp.Presenter.controller.HomeController
import java.lang.IllegalArgumentException

class MusicViewModelFactory(): ViewModelProvider.Factory {
    lateinit var mMusicRepository: MusicRepository
    lateinit var mHomeRepository: HomeRepository
    constructor(repository: MusicRepository) : this() {
        mMusicRepository =  repository
    }

    constructor(repository: HomeRepository) : this() {
        mHomeRepository =  repository
    }


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MusicViewModel(mMusicRepository) as T
        } else if (modelClass.isAssignableFrom(HomeController::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeController(mHomeRepository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}