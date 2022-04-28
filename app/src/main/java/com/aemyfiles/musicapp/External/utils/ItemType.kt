package com.aemyfiles.musicapp.External.utils

import com.aemyfiles.musicapp.R

enum class ItemType {

    SONG_TYPE, ALBUM_TYPE, ARTIST_TYPE, PLAYLIST_TYPE, FOLDER_TYPE, NON_TYPE;

    companion object {
        fun getIdByItemType(itemType: ItemType): Int {
            return when(itemType) {
                SONG_TYPE -> R.drawable.ic_single_song
                ALBUM_TYPE -> R.drawable.ic_groove
                PLAYLIST_TYPE -> R.drawable.playlist
                ARTIST_TYPE -> R.drawable.man
                else -> R.drawable.ic_single_song
            }
        }
    }




}