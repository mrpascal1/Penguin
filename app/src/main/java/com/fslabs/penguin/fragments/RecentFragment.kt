package com.fslabs.penguin.fragments

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fslabs.penguin.activities.MainActivity
import com.fslabs.penguin.models.Recent
import com.fslabs.penguin.adapters.RecentAdapter
import com.fslabs.penguin.databinding.FragmentRecentBinding
import com.fslabs.penguin.models.NumberDetailList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class RecentFragment : Fragment() {

    lateinit var binding: FragmentRecentBinding
    lateinit var recentList: ArrayList<Recent>
    lateinit var adapter: RecentAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecentAdapter(requireContext())
        recentList = ArrayList()

        val recentLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recentLayoutManager.stackFromEnd = false
        binding.recentRecycler.layoutManager = recentLayoutManager
        binding.recentRecycler.adapter = adapter
        if (checkSelfPermission(MainActivity().permissions[0], MainActivity().REQUEST_READ_CONTACTS) &&
            checkSelfPermission(MainActivity().permissions[1], MainActivity().REQUEST_READ_CONTACTS) &&
            checkSelfPermission(MainActivity().permissions[2], MainActivity().REQUEST_READ_CONTACTS)) {
            getCallLogs()
        }
    }

    private fun getCallLogs(){
        val hash = HashMap<String, ArrayList<NumberDetailList>>()
        GlobalScope.launch(Dispatchers.IO){
            val uriCallLogs = Uri.parse("content://call_log/calls")
            val cursorCallLogs = requireContext().contentResolver.query(uriCallLogs, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER)
            if (cursorCallLogs != null) {
                while (cursorCallLogs.moveToNext()) {
                    val number = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.NUMBER).toInt())
                    val name = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.CACHED_NAME).toInt())
                    val lastModified = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.DATE).toInt())
                    val modified = Date(TimeUnit.MILLISECONDS.convert(lastModified.toLong(), TimeUnit.SECONDS))
                    val duration = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.DURATION).toInt())
                    var type = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.TYPE).toInt())
                    when (type.toInt()){
                        CallLog.Calls.OUTGOING_TYPE -> type = "Outgoing"
                        CallLog.Calls.INCOMING_TYPE -> type = "Incoming"
                        CallLog.Calls.MISSED_TYPE -> type = "Missed"
                    }
                    if (name != null) {
                        if (hash.containsKey(name)) {
                            hash[name]?.add(NumberDetailList(number, duration))
                        } else {
                            hash[name] = arrayListOf(NumberDetailList(number, duration))
                        }
                    }else{
                        if (hash.containsKey(number)) {
                            hash[number]?.add(NumberDetailList(number, duration))
                        } else {
                            hash[number] = arrayListOf(NumberDetailList(number, duration))
                        }
                    }
                    recentList.add(Recent(name, number, hash, type, lastModified))
                    Log.d("TAG",  "$number $name $duration $type $lastModified $modified")
                }
            }
            requireActivity().runOnUiThread {
                adapter.setData(recentList)
                binding.progressBar.visibility = View.GONE
            }
            cursorCallLogs?.close()
        }
    }

    fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), MainActivity().permissions, requestCode)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MainActivity().REQUEST_READ_CONTACTS) {
            if (
                grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                grantResults[2] != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(requireContext(), "Permissions needed", Toast.LENGTH_LONG).show()
                return
            }
            // Here we continue only if all permissions are granted.
            // The permissions can also be granted in the system settings manually.
            //permissionGrantedListener?.onPermissionGranted()
            getCallLogs()
            Log.d("TAG", "Activity perm granted")
        }
    }
}