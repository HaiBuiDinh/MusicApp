package com.aemyfiles.musicapp.External.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import com.aemyfiles.musicapp.Domain.entity.SongInfo

class MediaManager {
    companion object {
        @SuppressLint("Range")
        fun getDataFromMedia(context: Context): List<SongInfo> {
            val list = ArrayList<SongInfo>()
            val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST_ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.DATE_ADDED,
                    MediaStore.Audio.Media.DISPLAY_NAME
            )

            val cursor = context.contentResolver.query(uri, projection, null, null, null)

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val dur = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
                    if (dur / 1000 < 60 || size / 1000000 < 0) {
                        continue
                    }
                    val songId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                    val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artirstId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                    val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val date = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED))
                    val song = SongInfo(songId, path, albumId, album, artirstId, artist, dur, size, date, title)
                    list.add(song)
                }
                cursor.close()
            }
            return list
        }
    }
}