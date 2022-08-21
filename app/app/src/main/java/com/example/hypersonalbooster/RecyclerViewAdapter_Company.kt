package com.example.hypersonalbooster

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.hypersonalbooster.databinding.FragmentRegisterCompanyAdapterBinding


class RecyclerViewAdapter_Company(private val company_list : ArrayList<String>, private val listener : OnCompanyClickListener)
    : RecyclerView.Adapter<RecyclerViewAdapter_Company.MyViewHolder>(), Filterable {
        inner class MyViewHolder(binding : FragmentRegisterCompanyAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
            val btn = binding.btn
            val root = binding.root
        }

    var TAG = "CompanyRecyclerViewAdapter"
    var filteredCompany = ArrayList<String>()
    var itemFilter = ItemFilter()

    init {
        filteredCompany.addAll(company_list)
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

    override fun getFilter() : Filter {
        return itemFilter
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(p: CharSequence?): FilterResults {
            val filterString = p.toString()
            val results = FilterResults()
            Log.d(TAG, "charSequence : $p")

            val filteredList : ArrayList<String> = ArrayList<String>()
            if(filterString.trim{it<= ' '}.isEmpty()) {
                results.values = company_list
                results.count = company_list.size
            }
            else {
                for (company in company_list) {
                    if (company.contains(filterString)) {
                        filteredList.add(company)
                    }
                }
            }

            results.values = filteredList
            results.count = filteredList.size

            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(p0: CharSequence?, p1: FilterResults) {
            filteredCompany.clear()
            filteredCompany.addAll(p1.values as ArrayList<String>)
            Log.d(TAG, "$filteredCompany")
            notifyDataSetChanged()
        }
    }
}