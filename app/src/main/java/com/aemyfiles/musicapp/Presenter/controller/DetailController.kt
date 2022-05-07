package com.aemyfiles.musicapp.Presenter.controller

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aemyfiles.musicapp.Domain.entity.ItemType
import com.aemyfiles.musicapp.Domain.entity.SongInfo
import com.aemyfiles.musicapp.External.repository.DetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailController @Inject constructor(val mRepository: DetailRepository) : ViewModel() {
    val mListItems = MutableLiveData<List<SongInfo>>()

    init {
        mListItems.value = ArrayList()
    }

    fun getListItems() : MutableLiveData<List<SongInfo>>{
        return mListItems
    }

    fun getListSongById(id: Int, type: ItemType) {
        GlobalScope.launch (Dispatchers.IO) {
            mListItems.postValue(mRepository.getListSongByType(id, type))
        }
    }
}