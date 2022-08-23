package com.example.hypersonalbooster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.hypersonalbooster.databinding.FragmentRegisterCompanyAdapterBinding

class ListViewAdapter_Taste (private val context: Context, private val taste_list : ArrayList<Taste>, private val listener : OnTasteClickListener)
    : BaseAdapter() {

    override fun getCount() : Int = taste_list.size

    override fun getItem(position :Int) : Taste = taste_list[position]

    override fun getItemId(position: Int) : Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = FragmentRegisterCompanyAdapterBinding.inflate(LayoutInflater.from(context))

        val taste_name = taste_list[position].name.toString()

        binding.btn.text = taste_name

        if(taste_list[position].check == false) {
            binding.btn.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        else{
            binding.btn.setBackgroundResource(R.drawable.btn_main_color)
        }

        binding.btn.setOnClickListener {
            if(taste_list[position].check == false) {
                binding.btn.setBackgroundResource(R.drawable.btn_main_color)
                taste_list[position].check = true
                listener.onTasteClickAdd(taste_name)
            }
            else {
                binding.btn.setBackgroundResource(R.drawable.btn_sub_color_light)
                taste_list[position].check = false
                listener.onTasteClickRemove(taste_name)
            }
        }

        return binding.root
    }
}