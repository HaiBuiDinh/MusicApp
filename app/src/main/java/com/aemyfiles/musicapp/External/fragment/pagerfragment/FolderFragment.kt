package com.aemyfiles.musicapp.External.fragment.pagerfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aemyfiles.musicapp.R

class FolderFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = layoutInflater.inflate(R.layout.fragment_all_folder_layout, container, false)
        return view
    }
}