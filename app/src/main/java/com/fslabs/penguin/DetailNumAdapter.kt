package com.fslabs.penguin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fslabs.penguin.databinding.LayoutNumbersBinding

class DetailNumAdapter(val context: Context): RecyclerView.Adapter<DetailNumAdapter.MyHolder>() {

    private val items: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutNumbersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindItems(items[position])
        holder.binding.cardView.setOnClickListener {
            val i = Intent(Intent.ACTION_CALL);
            i.data = Uri.parse("tel:${items[position]}")
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(items: ArrayList<String>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class MyHolder(val binding: LayoutNumbersBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(number: String){
            binding.numberTv.text = number
        }
    }
}