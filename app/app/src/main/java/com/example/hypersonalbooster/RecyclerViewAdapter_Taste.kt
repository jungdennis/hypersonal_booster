package com.example.hypersonalbooster

import android.annotation.SuppressLint
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.hypersonalbooster.databinding.FragmentRegisterTasteAdapterBinding


class RecyclerViewAdapter_Taste(private val taste_list : ArrayList<String>, private val listener : OnTasteClickListener)
    : RecyclerView.Adapter<RecyclerViewAdapter_Taste.MyViewHolder>(), Filterable {

    private var  mSelectedItems : SparseBooleanArray = SparseBooleanArray(0)

    inner class MyViewHolder(binding : FragmentRegisterTasteAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: FragmentRegisterTasteAdapterBinding

        val btn = binding.btn
        val root = binding.root

        val length = taste_list.size
        var check = Array<Boolean>(length) {
            false
        }

        init {
            this.binding = binding

            this.binding.btn.setOnClickListener(View.OnClickListener {
                val position = adapterPosition
                if (check[position] == false) {
                    binding.btn.setBackgroundResource(R.drawable.btn_main_color)
                    listener.onTasteClickAdd(taste_list[position])
                    check[position] = true
                }
                else {
                    binding.btn.setBackgroundResource(R.drawable.btn_sub_color_light)
                    listener.onTasteClickRemove(taste_list[position])
                    check[position] = false
                }
            })
        }
    }

    var TAG = "TasteRecyclerViewAdapter"
    var filteredTaste = ArrayList<String>()
    var itemFilter = ItemFilter()

    init {
        filteredTaste.addAll(taste_list)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : FragmentRegisterTasteAdapterBinding =
            FragmentRegisterTasteAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val taste_name = taste_list[position].toString()
        holder.btn.text = taste_name
        Log.d("Taste Adapter", "$taste_name")
    }

    override fun getItemCount(): Int {
        return taste_list.size
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
                results.values = taste_list
                results.count = taste_list.size
            }
            else {
                for (taste in taste_list) {
                    if (taste.contains(filterString)) {
                        filteredList.add(taste)
                    }
                }

                results.values = filteredList
                results.count = filteredList.size
            }

            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(p0: CharSequence?, p1: FilterResults) {
            filteredTaste.clear()
            filteredTaste.addAll(p1.values as ArrayList<String>)
            Log.d(TAG, "$filteredTaste")
            notifyDataSetChanged()
        }
    }
}