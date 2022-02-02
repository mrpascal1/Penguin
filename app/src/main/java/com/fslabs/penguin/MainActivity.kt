package com.fslabs.penguin

import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fslabs.penguin.databinding.ActivityMainBinding
import com.fslabs.penguin.fragments.ContactsFragment
import com.fslabs.penguin.fragments.RecentFragment
import com.fslabs.penguin.listeners.PermissionGrantedListener
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


open class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    open val REQUEST_READ_CONTACTS = 79
    open val permissions = arrayOf(android.Manifest.permission.READ_CONTACTS,
                                android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.READ_CALL_LOG)
    var permissionGrantedListener: PermissionGrantedListener? = null


    fun setOnPermissionGrantedListener(permissionGrantedListener: PermissionGrantedListener){
        this.permissionGrantedListener = permissionGrantedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val contactsFragment = ContactsFragment()
        val contactsTransaction = supportFragmentManager.beginTransaction()
        contactsTransaction.replace(R.id.content, contactsFragment).commit()
        binding.bottomNavigation.selectedItemId = R.id.contacts

        //notifyFragment()

        var selected = 2
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.recent -> {
                    if (selected != 1) {
                        val recentFragment = RecentFragment()
                        val recentTransaction = supportFragmentManager.beginTransaction()
                        recentTransaction.replace(R.id.content, recentFragment).commit()
                        selected = 1
                        true
                    }else {
                        false
                    }
                }
                R.id.contacts -> {
                    if (selected != 2) {
                        val cf = ContactsFragment()
                        val ct = supportFragmentManager.beginTransaction()
                        ct.replace(R.id.content, cf).commit()
                        //notifyFragment()
                        selected = 2
                        true
                    }else{
                        false
                    }
                }
                else -> false
            }
        }
    }

}