package com.aemyfiles.musicapp.External.repository

import com.aemyfiles.musicapp.Domain.MusicDatabase
import com.aemyfiles.musicapp.Domain.entity.ItemType
import com.aemyfiles.musicapp.Domain.entity.SongInfo

class DetailRepository(private val database: MusicDatabase) {

    fun getListSongByType(id: Int, type: ItemType): List<SongInfo> {
        when (type) {
            ItemType.ALBUM_TYPE -> return getListSongByAlbumId(id)
            ItemType.PLAYLIST_TYPE -> return ArrayList()
            ItemType.FOLDER_TYPE -> return ArrayList()
        }
        return ArrayList()
    }

    fun getListSongByAlbumId(albumId: Int): List<SongInfo> {
        return database.audioDao().getListSongByAlbumId(albumId)
    }
}