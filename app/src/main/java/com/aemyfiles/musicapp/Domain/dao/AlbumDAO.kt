package com.aemyfiles.musicapp.Domain.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aemyfiles.musicapp.Domain.entity.AlbumInfo

@Dao
interface AlbumDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(albumInfo: AlbumInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(albumInfos: List<AlbumInfo>)

    @Update
    suspend fun update(albumInfo: AlbumInfo)

    @Delete
    fun delete(albumInfo: AlbumInfo)

    @Query("delete from album_info_table")
    fun deleteAll()

    @Query("SELECT * FROM album_info_table WHERE album_id = :albumId")
    fun getAlbum(albumId:Int) : AlbumInfo

    @Query("SELECT * FROM album_info_table")
    fun getAllAlbum(): LiveData<List<AlbumInfo>>

    @Query("SELECT * FROM album_info_table limit 9")
    fun getAllAlbumWithLimit(): LiveData<List<AlbumInfo>>
}