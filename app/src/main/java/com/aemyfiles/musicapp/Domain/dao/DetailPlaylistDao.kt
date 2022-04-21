package com.aemyfiles.musicapp.Domain.dao

import androidx.room.*
import com.aemyfiles.musicapp.Domain.entity.DetailPlaylistInfo

@Dao
interface DetailPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(detailPlaylistInfo: DetailPlaylistInfo)

    @Delete
    suspend fun delete(detailPlaylistInfo: DetailPlaylistInfo)

    @Update
    suspend fun update(detailPlaylistInfo: DetailPlaylistInfo)
}