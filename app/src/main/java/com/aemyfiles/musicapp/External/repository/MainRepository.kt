package com.aemyfiles.musicapp.External.repository

import android.content.Context
import com.aemyfiles.musicapp.Domain.MusicDatabase
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo

class MainRepository(private val database: MusicDatabase) {

    private fun getAllAlbumFromSongTable(): List<AlbumInfo> {
        val listAlbum = ArrayList<AlbumInfo>()
        val cursor = database.audioDao().getAllAlbum()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val albumId = cursor.getInt(0)
                val album_name = cursor.getString(1)
                val total_song = cursor.getInt(2)
                val newAlbum = AlbumInfo(albumId, album_name, total_song, "")
                listAlbum.add(newAlbum)
            }
            cursor.close()
        }
        return listAlbum
    }

    suspend fun syncFromProvider(context: Context) {
        database.audioDao().deleteAll()
        database.albumDao().deleteAll()
        val listSong = MediaManager.getDataFromMedia(context)
        database.audioDao().insert(listSong)
        val listAlbum = getAllAlbumFromSongTable()
        database.albumDao().insert(listAlbum)
    }
}