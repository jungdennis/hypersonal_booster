package com.example.hypersonalbooster

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hypersonalbooster.databinding.FragmentRegisterCompanyAdapterBinding

class CompanyRecyclerViewAdapter(private val company_list : ArrayList<String>, private val listener : OnCompanyClickListener)
    : RecyclerView.Adapter<CompanyRecyclerViewAdapter.MyViewHolder>() {
        inner class MyViewHolder(binding : FragmentRegisterCompanyAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
            val btn = binding.btn
            val root = binding.root
        }

    var check : Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : FragmentRegisterCompanyAdapterBinding =
            FragmentRegisterCompanyAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val company_name = company_list[position].toString()

        holder.btn.text = company_name

        holder.btn.setOnClickListener {
            if(check == false) {
                holder.btn.setBackgroundResource(R.drawable.btn_main_color)
                listener.onCompanyClickAdd(company_name)
                check = true
            }
            else {
                holder.btn.setBackgroundResource(R.drawable.btn_sub_color_light)
                listener.onCompanyClickRemove(company_name)
                check = false
            }
        }
    }

    override fun getItemCount(): Int {
        return company_list.size
    }
}