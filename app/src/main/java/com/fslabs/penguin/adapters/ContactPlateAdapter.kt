package com.fslabs.penguin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fslabs.penguin.models.Contact
import com.fslabs.penguin.activities.DetailedProfileActivity
import com.fslabs.penguin.databinding.LayoutContactPlateBinding

class ContactPlateAdapter(val context: Context): RecyclerView.Adapter<ContactPlateAdapter.MyHolder>(){

    var items: ArrayList<Contact> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutContactPlateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val contact = items[position]
        if (contact.numbers.isNotEmpty()) {
            holder.bindItems(items[position])
        }
        holder.binding.cardView.setOnClickListener {
            val intent = Intent(context, DetailedProfileActivity::class.java)
            intent.putExtra("id", items[position].id)
            intent.putExtra("name", items[position].name)
            intent.putExtra("numbers", items[position].numbers)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(items: ArrayList<Contact>){
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun updateList(list: ArrayList<Contact>){
        items = list
        notifyDataSetChanged()
    }

    class MyHolder(val binding: LayoutContactPlateBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItems(item: Contact){
            binding.nameTv.text = item.name
            if (item.numbers.isNotEmpty()) {
                var nums = ""
                item.numbers.forEach {
                    if (nums == ""){
                        nums = "$it"
                    }else{
                        nums += ", $it"
                    }
                }
                binding.numberTv.text = nums
            }else{
                binding.numberTv.text = "Not found"
            }
        }
    }
}