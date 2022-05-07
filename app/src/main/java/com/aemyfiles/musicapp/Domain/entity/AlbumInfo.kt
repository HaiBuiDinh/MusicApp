package com.aemyfiles.musicapp.Domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "album_info_table")
class AlbumInfo(
    @PrimaryKey
    @ColumnInfo val album_id: Int,
    @ColumnInfo val album_name: String,
    @ColumnInfo val total_song: Int,
    @ColumnInfo var thumbnail: String,
){
    @Ignore
    var listSongs: List<SongInfo>? = null

    constructor(album_id: Int, album_name: String, total_song: Int, thumbnail: String, listSong : List<SongInfo>) : this(album_id, album_name, total_song, thumbnail){
        listSongs = listSong;
    }
}