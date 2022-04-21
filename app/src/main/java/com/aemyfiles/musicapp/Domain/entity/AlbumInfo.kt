package com.aemyfiles.musicapp.Domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_info_table")
class AlbumInfo(
    @PrimaryKey
    @ColumnInfo val album_id: Int,
    @ColumnInfo val album_name: String,
    @ColumnInfo val total_song: Int,
    @ColumnInfo var thumbnail: String
)