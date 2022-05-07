package com.aemyfiles.musicapp.External.repository

import com.aemyfiles.musicapp.Domain.MusicDatabase
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import javax.inject.Inject

class LibraryRepository @Inject constructor(private val database: MusicDatabase) {

    fun getAllSong() = database.audioDao().getAllSong()

    fun getAllAlbum() : List<AlbumInfo>{
        var listAlbum = database.albumDao().getAllAlbum()
        listAlbum.forEach {
            if(it.thumbnail.equals("")){
                it.listSongs = getListSongByAlbumId(it.album_id)
            }
        }
        return listAlbum;
    }

    suspend fun updateAlbum(albumInfo: AlbumInfo) = database.albumDao().update(albumInfo)

    fun updateThumbnail(album_id: Int, thumbnail:String) = database.albumDao().updateThumbnail(album_id, thumbnail)

    fun getListSongByAlbumId(album_id: Int): List<SongInfo> {
        return database.audioDao().getListSongByAlbumId(album_id)
    }
}