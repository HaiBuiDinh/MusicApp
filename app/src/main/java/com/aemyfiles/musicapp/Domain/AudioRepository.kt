package com.aemyfiles.musicapp.Domain

class AudioRepository(private val db: AudioDatabase) {
    fun getAllAudio() = db.audioDao().getAllAudio()

    suspend fun insert(audioInfo: AudioInfo) = db.audioDao().insert(audioInfo)
    suspend fun delete(audioInfo: AudioInfo) = db.audioDao().delete(audioInfo)
    suspend fun update(audioInfo: AudioInfo) = db.audioDao().update(audioInfo)
}