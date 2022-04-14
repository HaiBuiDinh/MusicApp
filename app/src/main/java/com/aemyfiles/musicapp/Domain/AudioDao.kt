package com.aemyfiles.musicapp.Domain

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AudioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(audioInfo: AudioInfo)

    @Delete
    suspend fun delete(audioInfo: AudioInfo)

    @Update
    suspend fun update(audioInfo: AudioInfo)

    @Query("SELECT * FROM audio_info_table")
    fun getAllAudio(): LiveData<List<AudioInfo>>
}