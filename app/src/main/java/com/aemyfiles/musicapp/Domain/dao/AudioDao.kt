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

    @Query("SELECT * FROM audio_info_table")
    fun getAllSong(): LiveData<List<SongInfo>>

    @Query("SELECT album_id, album_name, COUNT(id) FROM audio_info_table GROUP BY album_name ORDER BY COUNT(id) DESC")
    fun getAllAlbum(): Cursor

    @Query("SELECT * FROM audio_info_table where album_id = :albumId ORDER BY album_name")
    fun getListAudioByAlbumId(albumId: Int): List<SongInfo>

    @Query("SELECT * FROM audio_info_table limit 9")
    fun getSongForEmptyRecent(): List<SongInfo>
}