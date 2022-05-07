package com.aemyfiles.musicapp.Presenter

import androidx.lifecycle.LiveData
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.SongInfo

interface AlbumViewModel {
    fun updateAlbum(albumInfo: AlbumInfo) {

    }

    fun getListAlBum(): LiveData<List<AlbumInfo>>
    fun getListSongByAlbumId(album_id: Int): List<SongInfo> {
        return listOf()
    }

    fun updateThumbnail(album_id: Int, thumbnail: String)
}