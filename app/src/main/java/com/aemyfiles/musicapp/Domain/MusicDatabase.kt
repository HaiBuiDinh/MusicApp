package com.aemyfiles.musicapp.Domain

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aemyfiles.musicapp.Domain.dao.*
import com.aemyfiles.musicapp.Domain.entity.*

@Database(
    entities = [SongInfo::class, AlbumInfo::class, RecentInfo::class, PlaylistInfo::class, DetailPlaylistInfo::class],
    version = 1,
    exportSchema = false
)
abstract class MusicDatabase: RoomDatabase() {

    abstract fun audioDao(): AudioDao
    abstract fun albumDao(): AlbumDAO
    abstract fun recentDao(): RecentDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun detailPlayListDao(): DetailPlaylistDao

    companion object{
        @Volatile
        private var INSTANCE: MusicDatabase? = null

        fun getDatabase(application: Application) : MusicDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(application, MusicDatabase::class.java, "AudioDatabase.db")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                //return instance
                instance
            }
        }
    }
}