package com.aemyfiles.musicapp.External.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.aemyfiles.musicapp.Presenter.controller.MainController
import com.aemyfiles.musicapp.Presenter.dagger2.DaggerAppComponent
import javax.inject.Inject

abstract class AbsFragment<P : Any> : Fragment() {

    @Inject
    lateinit var mController:P
    @Inject
    lateinit var mMainController: MainController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(resourceId, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        initController()
        initUI(view)
    }

    abstract fun initController()
    abstract val resourceId: Int
    abstract fun updateContentView(isServiceConnected: Boolean)
    abstract fun initUI(view: View?)
    override fun onDestroy() {
        Log.d("long.vt", "onDestroy: " + this@AbsFragment)
        super.onDestroy()
    }
}