package com.example.hypersonalbooster

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.hypersonalbooster.databinding.FragmentMainBoosterAdapterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.FirebaseStorage


class ListViewAdapter_Main(private val context: Context, private val booster_list : ArrayList<Booster>)
    : BaseAdapter() {
    override fun getCount() : Int = booster_list.size

    override fun getItem(position :Int) : Booster = booster_list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = FragmentMainBoosterAdapterBinding.inflate(LayoutInflater.from(context))

        val booster = booster_list[position]

        val image_name = booster.name + ".jpg"
        val image_path = FirebaseStorage.getInstance("gs://hypersonal-booster.appspot.com").getReference()

        binding.boosterKind.text = booster.class_2
        binding.boosterName.text = booster.name

        val url = image_path.child(image_name).downloadUrl
        Log.d("ListViewAdapter_Main", "Url : $url")

        image_path.child(image_name).getDownloadUrl().addOnCompleteListener( { task ->
            if (task.isSuccessful) {
                val requestOptions: RequestOptions = RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)

                Glide.with(context).load(task.getResult()).apply(requestOptions).into(binding.boosterImage)
            } else {
                val booster_image = context.resources.getIdentifier("img_scoop", "drawable", context.packageName)

                binding.boosterImage.setImageResource(booster_image)
            }
        })

        return binding.root
    }
}