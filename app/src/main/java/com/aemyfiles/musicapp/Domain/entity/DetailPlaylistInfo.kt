package com.aemyfiles.musicapp.Domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DetailPlaylistInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "playlist_id") val playlist_id: Int,
    @ColumnInfo(name = "song_id") val song_id: Int
) {
}