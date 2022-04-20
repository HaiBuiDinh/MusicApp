package com.aemyfiles.musicapp.Domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SongInfo::class, AlbumInfo::class], version = 1, exportSchema = false)
abstract class MusicDatabase: RoomDatabase() {

    abstract fun audioDao(): AudioDao
    abstract fun albumDao(): AlbumDAO

    companion object{
        @Volatile
        private var INSTANCE: MusicDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE?: synchronized(LOCK) {
            INSTANCE?: createDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, MusicDatabase::class.java, "AudioDatabase.db").build()
    }
}