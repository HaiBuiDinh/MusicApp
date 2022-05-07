package com.aemyfiles.musicapp.Presenter.controller

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aemyfiles.musicapp.External.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainController @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val isSyncFinish = MutableLiveData(false)

    fun syncFromProvider(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            isSyncFinish.postValue(false)
            repository.syncFromProvider(context)
            isSyncFinish.postValue(true)
        }
    }
}