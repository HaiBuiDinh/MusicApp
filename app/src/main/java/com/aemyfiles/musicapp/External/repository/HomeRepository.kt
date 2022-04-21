package com.aemyfiles.musicapp.External.repository

import com.aemyfiles.musicapp.Domain.MusicDatabase
import com.aemyfiles.musicapp.Domain.entity.RecentInfo

class HomeRepository(private val database: MusicDatabase) {

    suspend fun insert(recentInfo: RecentInfo) {
        database.recentDao().insert(recentInfo)
    }

    fun getRecentPlaylist() = database.recentDao().getRecent(true)
    fun getRecentSong() = database.recentDao().getRecent(false)

    fun getListSongWhenRecentEmpty() = database.audioDao().getSongForEmptyRecent()
}