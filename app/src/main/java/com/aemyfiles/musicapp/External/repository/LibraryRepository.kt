package com.aemyfiles.musicapp.External.repository

import com.aemyfiles.musicapp.Domain.MusicDatabase
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.SongInfo

class LibraryRepository(private val database: MusicDatabase) {

    fun getAllSong() = database.audioDao().getAllSong()

    fun getAllAlbum() = database.albumDao().getAllAlbum()

    suspend fun updateAlbum(albumInfo: AlbumInfo) = database.albumDao().update(albumInfo)

    fun getListSongByAlbumId(album_id: Int): List<SongInfo> {
        return database.audioDao().getListAudioByAlbumId(album_id)
    }
}