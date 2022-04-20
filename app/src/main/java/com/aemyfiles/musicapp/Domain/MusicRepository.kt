package com.aemyfiles.musicapp.Domain

import android.content.Context
import android.util.Log
import com.aemyfiles.musicapp.External.utils.ItemType
import com.aemyfiles.musicapp.External.utils.MediaManager
import com.aemyfiles.musicapp.External.utils.ThumbnailManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MusicRepository(private val mDatabase: MusicDatabase) {

    fun getAllSong() = mDatabase.audioDao().getAllSong()
    fun getAllAlbum() = mDatabase.albumDao().getAllAlbum()

    suspend fun insert(songInfo: SongInfo) = mDatabase.audioDao().insert(songInfo)
    suspend fun delete(songInfo: SongInfo) = mDatabase.audioDao().delete(songInfo)
    suspend fun update(songInfo: SongInfo) = mDatabase.audioDao().update(songInfo)

    suspend fun updateAlbum(albumInfo: AlbumInfo) = mDatabase.albumDao().update(albumInfo)

    fun getListSongByAlbumId(album_id: Int): List<SongInfo> {
        return mDatabase.audioDao().getListAudioByAlbumId(album_id)
    }

    fun getAllAlbumFromSongTable(): List<AlbumInfo> {
        val listAlbum = ArrayList<AlbumInfo>()
        val cursor = mDatabase.audioDao().getAllAlbum()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val albumId = cursor.getInt(0)
                val album_name = cursor.getString(1)
                val total_song = cursor.getInt(2)
                val newAlbum = AlbumInfo(albumId, album_name, total_song, "")
                listAlbum.add(newAlbum)
            }
            cursor.close()
        }
        return listAlbum
    }

    suspend fun syncFromProvider(context: Context) {
        mDatabase.albumDao().deleteAll()
        mDatabase.audioDao().deleteAll()
        val listSong = MediaManager.getDataFromMedia(context)
        mDatabase.audioDao().insert(listSong)
        val listAlbum = getAllAlbumFromSongTable()
        mDatabase.albumDao().insert(listAlbum)

//        var thumbnailMap = HashMap<Int, String>();
//
//        for (album in listAlbum) {
//            thumbnailMap.put(album.album_id, "")
//        }
//
//        val thumbnailCallback = object : ThumbnailManager.ThumbnailCallback {
//            override fun onSuccess(albumId: Int, path: String?) {
//                Log.d("long.vt", "AudioRepository onSuccess: " + path)
//                GlobalScope.launch(Dispatchers.IO) {
//                    if (thumbnailMap.getOrDefault(albumId, "").equals("")) {
//                        thumbnailMap.put(albumId, path!!)
//                        val info = mDatabase.albumDao().getAlbum(albumId)
//                        info?.let {
//                            mDatabase.albumDao().delete(info)
//                            info.thumbnail = path
//                            mDatabase.albumDao().insert(info)
//                        }
//                    }
//                }
//            }
//        }
//
//        for (audio in listSong) {
//            if (thumbnailMap.getOrDefault(audio.album_id, "").equals("")) {
//                GlobalScope.launch(Dispatchers.Main) {
//                    ThumbnailManager.getInstance().loadThumbnail(
//                        ThumbnailManager.ThumbnailInfo(
//                            audio.album_id,
//                            audio.path,
//                            320,
//                            ItemType.ALBUM_TYPE,
//                            thumbnailCallback
//                        )
//                    )
//                }
//            }
//        }


//        var nameMap = HashMap<Int, String>();
//        Log.d("long.vt", "syncFromProvider: songsize = " + listSong.size)
//        for (audio in listSong) {
//            thumbnailMap.put(audio.album_id, "")
//            nameMap.put(audio.album_id, audio.album_name)
//            var list = listMap.getOrDefault(audio.album_id, ArrayList())
//            list.add(audio)
//            listMap.put(audio.album_id, list)
//        }
//
//        var listAlbum = ArrayList<AlbumInfo>();
//        val thumbnailCallback = object : ThumbnailManager.ThumbnailCallback{
//            override fun onSuccess(albumId: Int, path: String?) {
//                Log.d("long.vt",  "AudioRepository onSuccess: " + path)
//                if(thumbnailMap.getOrDefault(albumId, "").equals("")){
//                    thumbnailMap.put(albumId, path!!)
//                }
//            }
//        }
//
//        for (map in listMap){
//            val listAudio = map.value
//            for (audio in listAudio){
//                if(thumbnailMap.getOrDefault(audio.album_id, "").equals("")){
//                    ThumbnailManager.getInstance().loadThumbnail(ThumbnailManager.ThumbnailInfo(audio.album_id, audio.path, 320, ItemType.ALBUM_TYPE, thumbnailCallback))
//                }
//            }
//            listAlbum.add(AlbumInfo(map.key, nameMap.get(map.key) as String, listAudio.size, thumbnailMap.get(map.key) as String))
//        }
    }
}