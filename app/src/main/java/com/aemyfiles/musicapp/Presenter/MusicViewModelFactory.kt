package com.aemyfiles.musicapp.Presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aemyfiles.musicapp.External.repository.HomeRepository
import com.aemyfiles.musicapp.External.repository.LibraryRepository
import com.aemyfiles.musicapp.External.repository.MusicRepository
import com.aemyfiles.musicapp.Presenter.controller.HomeController
import com.aemyfiles.musicapp.Presenter.controller.LibraryController
import java.lang.IllegalArgumentException

class MusicViewModelFactory(): ViewModelProvider.Factory {
    lateinit var mMusicRepository: MusicRepository
    lateinit var mHomeRepository: HomeRepository
    lateinit var mLibraryRepository: LibraryRepository
    constructor(repository: MusicRepository) : this() {
        mMusicRepository =  repository
    }

    constructor(repository: HomeRepository) : this() {
        mHomeRepository =  repository
    }

    constructor(repository: LibraryRepository) : this() {
        mLibraryRepository =  repository
    }


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MusicViewModel(mMusicRepository) as T
        } else if (modelClass.isAssignableFrom(HomeController::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeController(mHomeRepository) as T
        } else if (modelClass.isAssignableFrom(LibraryController::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibraryController(mLibraryRepository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}