package com.aemyfiles.musicapp.External.utils

import com.aemyfiles.musicapp.R

enum class ItemType {

    SONG_TYPE, ALBUM_TYPE, ARTIST_TYPE, PLAYLIST_TYPE, FOLDER_TYPE, NON_TYPE;

    companion object {
        fun getIdByItemType(itemType: ItemType) : Int{
            return R.drawable.ic_launcher_background;
        }
    }

    /*
        public static int getIdByItemType(ItemType itemType) {
        switch (itemType) {
            case SONG_TYPE:
                return R.drawable.music_item;
            case ALBUM_TYPE:
            case PLAYLIST_TYPE:
                return R.drawable.album_item;
            case ARTIST_TYPE:
                return R.drawable.artist_item;
            case FOLDER_TYPE:
                return R.drawable.folder_item;
        }
        return -1;
    }


     */
 }