package com.aemyfiles.musicapp.External

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.aemyfiles.musicapp.R

class FragmentUtils {
    companion object {

        private const val CONTAINER = R.id.frame_container

        fun enterFragment(activity: FragmentActivity, fragment: Fragment, backStack: Boolean, bundle: Bundle?) {
            // load fragment
            fragment.arguments = bundle
            val transaction = activity.supportFragmentManager.beginTransaction()
            if (backStack) {
                transaction.replace(CONTAINER, fragment).addToBackStack(fragment::class.simpleName)
            } else {
                transaction.replace(CONTAINER, fragment)
            }
            transaction.commit()
        }
    }
}