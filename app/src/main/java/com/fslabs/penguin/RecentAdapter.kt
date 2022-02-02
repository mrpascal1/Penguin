package com.fslabs.penguin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fslabs.penguin.databinding.LayoutRecentLogsBinding

class RecentAdapter(val context: Context): RecyclerView.Adapter<RecentAdapter.MyHolder>(){
    var items = ArrayList<Recent>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutRecentLogsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(context, items[position])
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
                    binding.typeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_incoming))
                }
                "Outgoing" -> {
                    binding.typeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_outgoing))
                }
                else -> {
                    binding.typeIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_missed))
                }
            }
            val name = recent.name
            if (name != null) {
                binding.nameTv.text = name
            }else{
                binding.nameTv.text = recent.number
            }
            binding.timeTv.text = "11:45"
        }
    }
}