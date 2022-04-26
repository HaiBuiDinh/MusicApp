package com.aemyfiles.musicapp.External.repository

import com.aemyfiles.musicapp.Domain.MusicDatabase

class LibraryRepository(private val database: MusicDatabase) {

    fun getAllSong() = database.audioDao().getAllSong()

    fun getAllAlbum() = database.albumDao().getAllAlbum()
}