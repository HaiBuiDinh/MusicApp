package com.aemyfiles.musicapp.Domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PlaylistInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val playlist_id: Int,
    @ColumnInfo val playlist_name: String,
    @ColumnInfo val total_song: Int
) {
}