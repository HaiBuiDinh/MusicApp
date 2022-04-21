package com.aemyfiles.musicapp.Presenter.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aemyfiles.musicapp.Domain.entity.RecentInfo
import com.aemyfiles.musicapp.External.repository.HomeRepository
import com.aemyfiles.musicapp.External.repository.MusicRepository
import kotlinx.coroutines.launch

class HomeController(private val homeRepository: HomeRepository): ViewModel() {

    suspend fun insert(recentInfo: RecentInfo) = viewModelScope.launch { homeRepository.insert(recentInfo) }

    fun getRecentPlaylist() = homeRepository.getRecentPlaylist()
    fun getRecentSong() = homeRepository.getRecentSong()
    fun getListSongWhenRecentEmpty() = homeRepository.getListSongWhenRecentEmpty()
}