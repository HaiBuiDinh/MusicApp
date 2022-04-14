package com.aemyfiles.musicapp.Domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AudioInfo::class], version = 1, exportSchema = false)
abstract class AudioDatabase: RoomDatabase() {

    abstract fun audioDao(): AudioDao

    companion object{
        @Volatile
        private var INSTANCE: AudioDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE?: synchronized(LOCK) {
            INSTANCE?: createDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AudioDatabase::class.java, "AudioDatabase.db").build()
    }
}