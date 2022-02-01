package com.fslabs.penguin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fslabs.penguin.databinding.ActivityDetailedProfileBinding

class DetailedProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailedProfileBinding
    lateinit var adapter: DetailNumAdapter
    var numbers = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        val intent = intent
        val id = intent.getStringExtra("id").toString()
        val name = intent.getStringExtra("name").toString()
        val numbers = intent.getStringArrayListExtra("numbers")
        if (numbers != null) {
            this.numbers = numbers
        }
        binding.nameTv.text = name
        adapter = DetailNumAdapter(this)
        val numberLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        numberLayoutManager.stackFromEnd = false
        binding.numberRecycler.layoutManager = numberLayoutManager
        binding.numberRecycler.adapter = adapter
        if (numbers != null) adapter.setData(numbers)
        Log.d("TAG", "Name: $name Numbers: $numbers")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}