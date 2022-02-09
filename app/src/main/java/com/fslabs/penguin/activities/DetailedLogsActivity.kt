package com.fslabs.penguin.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fslabs.penguin.R
import com.fslabs.penguin.adapters.RecentListAdapter
import com.fslabs.penguin.databinding.ActivityDetailedLogsBinding
import com.fslabs.penguin.models.NumberDetailList

class DetailedLogsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailedLogsBinding
    lateinit var recentLogsAdapter: RecentListAdapter
    var details = ArrayList<NumberDetailList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedLogsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val intent = intent
        val name = intent.getStringExtra("name").toString()
        val details = intent.getSerializableExtra("details")
        this.details = details as ArrayList<NumberDetailList>
        Log.d("TAG", "${this.details}")

        binding.nameTv.text = name

        recentLogsAdapter = RecentListAdapter(this)
        binding.numberRecycler.configure(this)
        recentLogsAdapter.addData(this.details)
    }

    private fun RecyclerView.configure(context: Context){
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = recentLogsAdapter
    }
}