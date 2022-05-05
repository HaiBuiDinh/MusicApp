package com.aemyfiles.musicapp.Domain

import android.app.Application
import com.aemyfiles.musicapp.External.repository.DetailRepository
import com.aemyfiles.musicapp.External.repository.HomeRepository
import com.aemyfiles.musicapp.External.repository.LibraryRepository
import com.aemyfiles.musicapp.External.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MusicApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { MusicDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { MainRepository(database) }
    val homeRepository by lazy { HomeRepository(database) }
    val libraryRepository by lazy { LibraryRepository(database)}
    val detailRepository by lazy { DetailRepository(database) }
}