package com.aemyfiles.musicapp.External.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aemyfiles.musicapp.External.activities.MainActivity

class Permission {

    companion object {
        fun isPermissionsAllowed(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }

        fun askForPermissions(context: Context): Boolean {
            if (!isPermissionsAllowed(context)) {
                ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.FOREGROUND_SERVICE),
                        MainActivity.REQUEST_CODE
                )
                return false
            }
            return true
        }
    }


}