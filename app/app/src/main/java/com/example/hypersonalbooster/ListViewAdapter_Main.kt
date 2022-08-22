package com.example.hypersonalbooster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.hypersonalbooster.databinding.FragmentMainBoosterAdapterBinding

class ListViewAdapter_Main(private val context: Context, private val booster_list : ArrayList<Booster>)
    : BaseAdapter() {
    override fun getCount() : Int = booster_list.size

    override fun getItem(position :Int) : Booster = booster_list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = FragmentMainBoosterAdapterBinding.inflate(LayoutInflater.from(context))

        val booster = booster_list[position]

        val booster_image = context.resources.getIdentifier("icon_google", "drawable", context.packageName)

        binding.boosterImage.setImageResource(booster_image)
        binding.boosterKind.text = booster.class_2
        binding.boosterName.text = booster.name

        return binding.root
    }
}