package com.aemyfiles.musicapp.External.repository

import androidx.lifecycle.LiveData
import com.aemyfiles.musicapp.Domain.MusicDatabase
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.RecentInfo
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import javax.inject.Inject

class HomeRepository @Inject constructor(private val database: MusicDatabase) {

    suspend fun insert(recentInfo: RecentInfo) {
        database.recentDao().insert(recentInfo)
    }

    fun getRecentPlaylist() = database.recentDao().getRecent(true)
    fun getRecentSong() = database.recentDao().getRecent(false)

    fun getListSongWhenRecentEmpty() = database.audioDao().getSongForEmptyRecent()

    fun getAllAlbum() : List<AlbumInfo>{
        var listALbum = database.albumDao().getAllAlbumWithLimit()
        listALbum?.forEach {
            if(it.thumbnail.equals("")){
                it.listSongs = getListSongByAlbumId(it.album_id)
            }
        }
        return listALbum
    }

    fun updateThumbnail(album_id: Int, thumbnail:String) = database.albumDao().updateThumbnail(album_id, thumbnail)

    fun getListSongByAlbumId(album_id: Int): List<SongInfo> {
        return database.audioDao().getListSongByAlbumId(album_id)
    }
}