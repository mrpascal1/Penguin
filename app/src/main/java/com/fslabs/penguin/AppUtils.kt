package com.fslabs.penguin

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fslabs.penguin.listeners.PermissionGrantedListener

object AppUtils {

    @JvmStatic
    fun checkSelfPermission(permission: String, requestCode: Int, context: Context, activity: Activity, permissions: Array<String>): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
            return false
        }
        return true
    }

    @JvmStatic
    fun notifyFragment(permissions: Array<String>, REQUEST_READ_CONTACTS: Int, context: Context, activity: Activity): Boolean{
        if (checkSelfPermission(permissions[0], REQUEST_READ_CONTACTS, context, activity, permissions) &&
            checkSelfPermission(permissions[1], REQUEST_READ_CONTACTS, context, activity, permissions) &&
            checkSelfPermission(permissions[2], REQUEST_READ_CONTACTS, context, activity, permissions)){
            //permissionGrantedListener?.onPermissionGranted()
            Log.d("TAG", "Activity perm granted")
            //fetchContacts()
            return true
        }else{
            return false
        }
    }
}