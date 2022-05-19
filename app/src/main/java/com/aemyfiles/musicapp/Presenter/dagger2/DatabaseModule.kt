package com.aemyfiles.musicapp.Presenter.dagger2

import android.app.Application
import com.aemyfiles.musicapp.Domain.MusicDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @Provides
    fun provideDatabase(application: Application) : MusicDatabase{
        return MusicDatabase.getDatabase(application)
    }
}