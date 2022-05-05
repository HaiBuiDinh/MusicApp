package com.aemyfiles.musicapp.External.repository

import com.aemyfiles.musicapp.Domain.MusicDatabase
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.RecentInfo
import com.aemyfiles.musicapp.Domain.entity.SongInfo

class HomeRepository(private val database: MusicDatabase) {

    suspend fun insert(recentInfo: RecentInfo) {
        database.recentDao().insert(recentInfo)
    }

    fun getRecentPlaylist() = database.recentDao().getRecent(true)
    fun getRecentSong() = database.recentDao().getRecent(false)

    fun getListSongWhenRecentEmpty() = database.audioDao().getSongForEmptyRecent()

    fun getAllAlbum() = database.albumDao().getAllAlbumWithLimit()

    suspend fun updateAlbum(albumInfo: AlbumInfo) = database.albumDao().update(albumInfo)

    fun getListSongByAlbumId(album_id: Int): List<SongInfo> {
        return database.audioDao().getListSongByAlbumId(album_id)
    }
}