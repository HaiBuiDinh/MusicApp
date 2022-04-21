package com.aemyfiles.musicapp.Domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_info_table")
class RecentInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val id: Int,
    @ColumnInfo val id_entity: Int,
    @ColumnInfo val name_entity: String,
    @ColumnInfo val total: Int,
    @ColumnInfo val is_playlist: Boolean
) {
}