package com.fslabs.penguin.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fslabs.penguin.R
import com.fslabs.penguin.activities.DetailedLogsActivity
import com.fslabs.penguin.models.Recent
import com.fslabs.penguin.databinding.LayoutRecentLogsBinding
import com.fslabs.penguin.models.NumberDetailList
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecentAdapter(val context: Context): RecyclerView.Adapter<RecentAdapter.MyHolder>(){
    var items = ArrayList<Recent>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutRecentLogsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(context, items[position])
        holder.binding.cardView.setOnClickListener {
            val intent = Intent(context, DetailedLogsActivity::class.java)
            intent.putExtra("name", holder.binding.nameTv.text.toString())
            intent.putExtra("details", items[position].details[holder.binding.nameTv.text.toString()])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(items: ArrayList<Recent>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class MyHolder(val binding: LayoutRecentLogsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItems(context: Context, recent: Recent){
            when (recent.type) {
                "Incoming" -> {
                    binding.typeIv.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_incoming
                    ))
                }
                "Outgoing" -> {
                    binding.typeIv.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_outgoing
                    ))
                }
                else -> {
                    binding.typeIv.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_missed
                    ))
                }
            }
            val name = recent.name
            if (name != null) {
                binding.nameTv.text = name
            }else{
                binding.nameTv.text = recent.number
            }
            val date = Date(recent.lastModified.toLong())
            val formatter = SimpleDateFormat("hh:mm")
            binding.timeTv.text = formatter.format(date).toString()
        }
    }
}