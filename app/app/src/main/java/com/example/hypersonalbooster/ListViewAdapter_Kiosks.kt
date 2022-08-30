package com.example.hypersonalbooster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.hypersonalbooster.databinding.FragmentRegisterCompanyAdapterBinding
import com.example.hypersonalbooster.databinding.FragmentRegisterKiosksAdapterBinding

class ListViewAdapter_Kiosks (private val context: Context, private val Kiosks_list : ArrayList<Kiosks>, private val listener : OnKiosksClickListener)
    : BaseAdapter() {

    override fun getCount() : Int = Kiosks_list.size

    override fun getItem(position :Int) : Kiosks = Kiosks_list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = FragmentRegisterKiosksAdapterBinding.inflate(LayoutInflater.from(context))

        val kiosks_name = Kiosks_list[position].name.toString()

        binding.btn.text = kiosks_name

        if(Kiosks_list[position].check == false) {
            binding.btn.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        else{
            binding.btn.setBackgroundResource(R.drawable.btn_main_color)
        }

        binding.btn.setOnClickListener {
            if(Kiosks_list[position].check == false) {
                binding.btn.setBackgroundResource(R.drawable.btn_main_color)
                Kiosks_list[position].check = true
                listener.onKiosksClickAdd(kiosks_name)
            }
            else {
                binding.btn.setBackgroundResource(R.drawable.btn_sub_color_light)
                Kiosks_list[position].check = false
                listener.onKiosksClickRemove(kiosks_name)
            }
        }

        return binding.root
    }
}


