package com.aemyfiles.musicapp.Presenter.dagger2

import android.app.Application
import androidx.fragment.app.FragmentActivity
import com.aemyfiles.musicapp.External.ui.activities.MainActivity
import com.aemyfiles.musicapp.External.ui.fragment.DetailFragment
import com.aemyfiles.musicapp.External.ui.fragment.HomeFragment
import com.aemyfiles.musicapp.External.ui.fragment.LibraryFragment
import com.aemyfiles.musicapp.External.ui.fragment.pagerfragment.AlbumFragment
import com.aemyfiles.musicapp.External.ui.fragment.pagerfragment.SongFragment
import dagger.BindsInstance
import dagger.Component

@Component (modules = arrayOf(RepositoryModule::class, DatabaseModule::class, ControllerModule::class))
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment : HomeFragment)
    fun inject(fragment : LibraryFragment)
    fun inject(fragment : DetailFragment)
    fun inject(fragment : AlbumFragment)
    fun inject(fragment : SongFragment)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application) : Builder
        @BindsInstance
        fun activity(activity: FragmentActivity) : Builder
        fun build() : AppComponent
    }
}