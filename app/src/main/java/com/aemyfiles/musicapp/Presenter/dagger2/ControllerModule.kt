package com.aemyfiles.musicapp.Presenter.dagger2

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.aemyfiles.musicapp.External.repository.DetailRepository
import com.aemyfiles.musicapp.External.repository.HomeRepository
import com.aemyfiles.musicapp.External.repository.LibraryRepository
import com.aemyfiles.musicapp.External.repository.MainRepository
import com.aemyfiles.musicapp.Presenter.MusicViewModelFactory
import com.aemyfiles.musicapp.Presenter.controller.DetailController
import com.aemyfiles.musicapp.Presenter.controller.HomeController
import com.aemyfiles.musicapp.Presenter.controller.LibraryController
import com.aemyfiles.musicapp.Presenter.controller.MainController
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class ControllerModule {

    @Provides
    @Reusable
    fun provideFactory(mainRepo: MainRepository, homeRepo: HomeRepository, libRepo : LibraryRepository, detailRepo: DetailRepository) : MusicViewModelFactory {
        return MusicViewModelFactory(mainRepo, homeRepo, libRepo, detailRepo)
    }

    @Provides
    @Reusable
    fun provideMainController(activity: FragmentActivity, factory: MusicViewModelFactory) : MainController{
        return ViewModelProviders.of(activity, factory).get(MainController::class.java)
    }

    @Provides
    @Reusable
    fun provideHomeController(activity: FragmentActivity, factory: MusicViewModelFactory) : HomeController{
        return ViewModelProviders.of(activity, factory).get(HomeController::class.java)
    }

    @Provides
    @Reusable
    fun provideLibraryController(activity: FragmentActivity, factory: MusicViewModelFactory) : LibraryController {
        return ViewModelProviders.of(activity, factory).get(LibraryController::class.java)
    }

    @Provides
    @Reusable
    fun provideDetailController(activity: FragmentActivity, factory: MusicViewModelFactory) : DetailController {
        return ViewModelProviders.of(activity, factory).get(DetailController::class.java)
    }
}