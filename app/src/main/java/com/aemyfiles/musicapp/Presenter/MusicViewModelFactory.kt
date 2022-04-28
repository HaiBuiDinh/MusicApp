package com.aemyfiles.musicapp.Presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aemyfiles.musicapp.External.repository.HomeRepository
import com.aemyfiles.musicapp.External.repository.LibraryRepository
import com.aemyfiles.musicapp.External.repository.MainRepository
import com.aemyfiles.musicapp.Presenter.controller.HomeController
import com.aemyfiles.musicapp.Presenter.controller.LibraryController
import java.lang.IllegalArgumentException

class MusicViewModelFactory() : ViewModelProvider.Factory {
    lateinit var mMainRepository: MainRepository
    lateinit var mHomeRepository: HomeRepository
    lateinit var mLibraryRepository: LibraryRepository

    constructor(repository: MainRepository) : this() {
        mMainRepository = repository
    }

    constructor(repository: HomeRepository) : this() {
        mHomeRepository = repository
    }

    constructor(repository: LibraryRepository) : this() {
        mLibraryRepository = repository
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainController::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainController(mMainRepository) as T
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