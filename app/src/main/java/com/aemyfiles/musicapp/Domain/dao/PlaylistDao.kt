package com.aemyfiles.musicapp.Domain.dao

import androidx.room.*
import com.aemyfiles.musicapp.Domain.entity.PlaylistInfo

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistInfo: PlaylistInfo)

    @Delete
    suspend fun delete(playlistInfo: PlaylistInfo)

    @Update
    suspend fun update(playlistInfo: PlaylistInfo)
}