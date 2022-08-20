package com.example.hypersonalbooster

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hypersonalbooster.databinding.FragmentRegisterCompanyAdapterBinding

class CompanyRecyclerViewAdapter(private val company_list : ArrayList<String>) : RecyclerView.Adapter<CompanyRecyclerViewAdapter.MyViewHolder>() {
        inner class MyViewHolder(binding : FragmentRegisterCompanyAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
            val btn = binding.btn
            val root = binding.root
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : FragmentRegisterCompanyAdapterBinding =
            FragmentRegisterCompanyAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val company_name = company_list[position].toString()

        holder.btn.text = company_name
    }

    override fun getItemCount(): Int {
        return company_list.size
    }
}