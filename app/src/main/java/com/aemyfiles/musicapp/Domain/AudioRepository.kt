package com.aemyfiles.musicapp.Domain

import android.content.Context
import com.aemyfiles.musicapp.External.utils.MediaManager

class AudioRepository(private val db: AudioDatabase) {
    fun getAllAudio() = db.audioDao().getAllAudio()
    fun getAllAlbum() = db.audioDao().getAllAlbum()

    suspend fun insert(audioInfo: AudioInfo) = db.audioDao().insert(audioInfo)
    suspend fun delete(audioInfo: AudioInfo) = db.audioDao().delete(audioInfo)
    suspend fun update(audioInfo: AudioInfo) = db.audioDao().update(audioInfo)

    fun getAllSongFromMedia(context: Context) {
        val listSong = MediaManager.getDataFromMedia(context)
    }
}