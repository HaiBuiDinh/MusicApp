package com.aemyfiles.musicapp.Domain.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.aemyfiles.musicapp.Domain.entity.SongInfo

@Dao
interface AudioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songInfo: SongInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listSongs: List<SongInfo>)

    @Delete
    suspend fun delete(songInfo: SongInfo)

    @Update
    suspend fun update(songInfo: SongInfo)

    @Query("delete from audio_info_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM audio_info_table order by display_name")
    fun getAllSong(): LiveData<List<SongInfo>>

    @Query("SELECT album_id, album_name, COUNT(id) FROM audio_info_table GROUP BY album_id ORDER BY COUNT(id)")
    fun getAllAlbum(): Cursor

    @Query("SELECT * FROM audio_info_table where album_id = :albumId ORDER BY album_name")
    fun getListSongByAlbumId(albumId: Int): List<SongInfo>

    @Query("SELECT * FROM audio_info_table ORDER BY RANDOM() limit 9")
    fun getSongForEmptyRecent(): List<SongInfo>
}