package com.aemyfiles.musicapp.Domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "audio_info_table")
class SongInfo(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "album_id") val album_id: Int,
    @ColumnInfo(name = "album_name") val album_name: String,
    @ColumnInfo(name = "artist_id") val artist_id: Int,
    @ColumnInfo(name = "artist_name") val artist_name: String,
    @ColumnInfo(name = "duration") val duration: Long,
    @ColumnInfo(name = "size") val size: Long,
    @ColumnInfo(name = "date_added") val date_added: String,
    @ColumnInfo(name = "display_name") val display_name: String
)