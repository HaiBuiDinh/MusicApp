package com.aemyfiles.musicapp.Presenter.dagger2

import com.aemyfiles.musicapp.Domain.MusicDatabase
import com.aemyfiles.musicapp.External.repository.DetailRepository
import com.aemyfiles.musicapp.External.repository.HomeRepository
import com.aemyfiles.musicapp.External.repository.LibraryRepository
import com.aemyfiles.musicapp.External.repository.MainRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideMainRepository(database: MusicDatabase) : MainRepository{
        return MainRepository(database)
    }

    @Provides
    fun provideHomeRepository(database: MusicDatabase) : HomeRepository {
        return HomeRepository(database)
    }

    @Provides
    fun provideLibraryRepository(database: MusicDatabase) : LibraryRepository {
        return LibraryRepository(database)
    }

    @Provides
    fun provideDetailRepository(database: MusicDatabase) : DetailRepository {
        return DetailRepository(database)
    }
}