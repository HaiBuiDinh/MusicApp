package com.aemyfiles.musicapp.Domain.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aemyfiles.musicapp.Domain.entity.RecentInfo

@Dao
interface RecentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentInfo: RecentInfo)

    @Delete
    suspend fun delete(recentInfo: RecentInfo)

    @Update
    suspend fun update(recentInfo: RecentInfo)

    @Query("SELECT * FROM recent_info_table WHERE is_playlist =:isPlaylist")
    fun getRecent(isPlaylist: Boolean): LiveData<List<RecentInfo>>
}