package com.example.hypersonalbooster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.hypersonalbooster.databinding.FragmentRegisterCompanyAdapterBinding

class ListViewAdapter_Company (private val context: Context, private val company_list : ArrayList<Company>, private val listener : OnCompanyClickListener)
    : BaseAdapter() {

    override fun getCount() : Int = company_list.size

    override fun getItem(position :Int) : Company = company_list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = FragmentRegisterCompanyAdapterBinding.inflate(LayoutInflater.from(context))

        val company_name = company_list[position].name.toString()

        binding.btn.text = company_name

        if(company_list[position].check == false) {
            binding.btn.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        else{
            binding.btn.setBackgroundResource(R.drawable.btn_main_color)
        }

        binding.btn.setOnClickListener {
            if(company_list[position].check == false) {
                binding.btn.setBackgroundResource(R.drawable.btn_main_color)
                company_list[position].check = true
                listener.onCompanyClickAdd(company_name)
            }
            else {
                binding.btn.setBackgroundResource(R.drawable.btn_sub_color_light)
                company_list[position].check = false
                listener.onCompanyClickRemove(company_name)
            }
        }

        return binding.root
    }
}