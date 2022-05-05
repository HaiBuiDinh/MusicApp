package com.aemyfiles.musicapp.External.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class AbsFragment<P : ViewModel?> : Fragment() {
    var controller: P? = null
        protected set

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
        controller = initController()
        initUI(view)
    }

    abstract fun initController(): P
    abstract val resourceId: Int
    abstract fun updateContentView(isServiceConnected: Boolean)
    abstract fun initUI(view: View?)
    override fun onDestroy() {
        Log.d("long.vt", "onDestroy: " + this@AbsFragment)
        super.onDestroy()
    }
}