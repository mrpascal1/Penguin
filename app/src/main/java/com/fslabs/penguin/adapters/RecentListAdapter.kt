package com.fslabs.penguin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fslabs.penguin.databinding.LayoutRecentLogListBinding
import com.fslabs.penguin.models.NumberDetailList

class RecentListAdapter(val context: Context) : RecyclerView.Adapter<RecentListAdapter.MyHolder>(){

    val items = ArrayList<NumberDetailList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutRecentLogListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addData(items: ArrayList<NumberDetailList>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class MyHolder(val binding: LayoutRecentLogListBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(details: NumberDetailList){
            binding.numberTv.text = details.number
            binding.timeTv.text = "Duration ${getDurationString(details.duration.toString().toInt())}"
        }

        private fun getDurationString(seconds: Int): String {
            var seconds = seconds
            val hours = seconds / 3600
            val minutes = seconds % 3600 / 60
            seconds %= 60
            return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds)
        }

        private fun twoDigitString(number: Int): String {
            if (number == 0) {
                return "00"
            }
            return if (number / 10 == 0) {
                "0$number"
            } else {
                number.toString()
            }
        }
    }
}