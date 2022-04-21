package com.aemyfiles.musicapp.External.repository

import android.content.Context
import androidx.annotation.WorkerThread
import com.aemyfiles.musicapp.Domain.dao.AlbumDAO
import com.aemyfiles.musicapp.Domain.dao.AudioDao
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import com.aemyfiles.musicapp.External.utils.MediaManager

class MusicRepository(private val mAudioDao: AudioDao, private val mAlbumDao: AlbumDAO) {

    fun getAllSong() = mAudioDao.getAllSong()
    fun getAllAlbum() = mAlbumDao.getAllAlbum()

    @WorkerThread
    @Suppress
    suspend fun insert(songInfo: SongInfo) = mAudioDao.insert(songInfo)
    @WorkerThread
    @Suppress
    suspend fun delete(songInfo: SongInfo) = mAudioDao.delete(songInfo)
    @WorkerThread
    @Suppress
    suspend fun update(songInfo: SongInfo) = mAudioDao.update(songInfo)

    suspend fun updateAlbum(albumInfo: AlbumInfo) = mAlbumDao.update(albumInfo)

    fun getListSongByAlbumId(album_id: Int): List<SongInfo> {
        return mAudioDao.getListAudioByAlbumId(album_id)
    }

    private fun getAllAlbumFromSongTable(): List<AlbumInfo> {
        val listAlbum = ArrayList<AlbumInfo>()
        val cursor = mAudioDao.getAllAlbum()
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
        mAudioDao.deleteAll()
        mAudioDao.deleteAll()
        val listSong = MediaManager.getDataFromMedia(context)
        mAudioDao.insert(listSong)
        val listAlbum = getAllAlbumFromSongTable()
        mAlbumDao.insert(listAlbum)
    }
}