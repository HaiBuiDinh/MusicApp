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

    @Query("update album_info_table set thumbnail =:thumbnail WHERE album_id = :albumId")
    fun updateThumbnail(albumId: Int, thumbnail:String)

    @Query("SELECT * FROM album_info_table")
    fun getAllAlbum(): List<AlbumInfo>

    @Query("SELECT * FROM album_info_table limit 9")
    fun getAllAlbumWithLimit(): List<AlbumInfo>
}