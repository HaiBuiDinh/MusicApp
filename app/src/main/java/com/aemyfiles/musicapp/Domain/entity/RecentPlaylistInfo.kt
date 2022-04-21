package com.aemyfiles.musicapp.Domain.entity

class RecentPlaylistInfo(
    val playlist_name: String,
    val list_song: List<SongInfo>,
    val total_song: Int
) {
}