package com.fslabs.penguin.fragments

import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
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
import com.fslabs.penguin.AppUtils
import com.fslabs.penguin.Contact
import com.fslabs.penguin.ContactPlateAdapter
import com.fslabs.penguin.MainActivity
import com.fslabs.penguin.databinding.FragmentContactsBinding
import com.fslabs.penguin.listeners.PermissionGrantedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ContactsFragment : Fragment() {

    lateinit var binding: FragmentContactsBinding
    lateinit var adapter: ContactPlateAdapter
    var contacts = ArrayList<Contact>()
    val REQUEST_READ_CONTACTS = 79
    val permissions = arrayOf(android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.CALL_PHONE,
        android.Manifest.permission.READ_CALL_LOG)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ContactPlateAdapter(requireContext())
        val contactsLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        contactsLayoutManager.stackFromEnd = false
        binding.contactsRecycler.layoutManager = contactsLayoutManager
        binding.contactsRecycler.adapter = adapter

        binding.searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }
        })

        if (checkSelfPermission(MainActivity().permissions[0], MainActivity().REQUEST_READ_CONTACTS) &&
            checkSelfPermission(MainActivity().permissions[1], MainActivity().REQUEST_READ_CONTACTS) &&
            checkSelfPermission(MainActivity().permissions[2], MainActivity().REQUEST_READ_CONTACTS)) {
            fetchContacts()
        }
    }

    fun filter(text: String){
        val temp  = ArrayList<Contact>()
        for(c in contacts){
            if(c.name.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))){
                temp.add(c)
            }else{
                c.numbers.forEach {
                    if (it.contains(text.lowercase(Locale.getDefault()))){
                        if (!temp.contains(c)){
                            temp.add(c)
                        }
                    }
                }
            }
        }
        adapter.updateList(temp)
    }

    private fun getPhoneContacts(): ArrayList<Contact> {
        val contactsList = ArrayList<Contact>()
        val contactsCursor = requireActivity().contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        if (contactsCursor != null && contactsCursor.count > 0) {
            val idIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)
            val nameIndex = contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            while (contactsCursor.moveToNext()) {
                val id = contactsCursor.getString(idIndex)
                val name = contactsCursor.getString(nameIndex)
                if (name != null) {
                    contactsList.add(Contact(id, name))
                }
            }
            contactsCursor.close()
        }
        return contactsList
    }

    private fun getContactNumbers(): HashMap<String, ArrayList<String>> {
        val contactsNumberMap = HashMap<String, ArrayList<String>>()
        val phoneCursor: Cursor? = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (phoneCursor != null && phoneCursor.count > 0) {
            val contactIdIndex = phoneCursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val numberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (phoneCursor.moveToNext()) {
                val contactId = phoneCursor.getString(contactIdIndex)
                val number: String = phoneCursor.getString(numberIndex)
                //check if the map contains key or not, if not then create a new array list with number
                if (contactsNumberMap.containsKey(contactId)) {
                    contactsNumberMap[contactId]?.add(number)
                } else {
                    contactsNumberMap[contactId] = arrayListOf(number)
                }
            }
            //contact contains all the number of a particular contact
            phoneCursor.close()
        }
        return contactsNumberMap
    }

    private fun fetchContacts() {
        GlobalScope.launch(Dispatchers.IO) {
            val contactsListAsync = async { getPhoneContacts() }
            val contactNumbersAsync = async { getContactNumbers() }
            contacts = contactsListAsync.await()
            val contactNumbers = contactNumbersAsync.await()

            contacts.forEach {
                contactNumbers[it.id]?.let { numbers ->
                    if (numbers.isNotEmpty()) {
                        numbers.forEach { num ->
                            var n = num.filter { filterIt -> !filterIt.isWhitespace() }
                            if (n.startsWith("+91")){
                                n = n.substring(3)
                            }
                            n = n.replace("-", "");
                            if (!it.numbers.contains(n)){
                                it.numbers.add(n)
                            }
                        }
                    }
                    Log.d("TAG", "Name: " + it.name + " Number:" + numbers)
                }
            }
            requireActivity().runOnUiThread {
                adapter.setData(contacts)
                binding.progressBar.visibility = View.GONE
            }
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
        if (requestCode == REQUEST_READ_CONTACTS) {
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
            fetchContacts()
            Log.d("TAG", "Activity perm granted")
        }
    }

}