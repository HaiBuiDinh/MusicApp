package com.aemyfiles.musicapp.Domain

import android.app.Application
import com.aemyfiles.musicapp.External.repository.HomeRepository
import com.aemyfiles.musicapp.External.repository.MusicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MusicApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { MusicDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { MusicRepository(database.audioDao(), database.albumDao()) }
    val homeRepository by lazy { HomeRepository(database) }
}