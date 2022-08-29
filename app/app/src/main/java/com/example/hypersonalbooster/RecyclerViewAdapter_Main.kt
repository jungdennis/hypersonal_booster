package com.example.hypersonalbooster

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.hypersonalbooster.databinding.FragmentMainBoosterAdapterBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RecyclerViewAdapter_Main(private val booster_list : ArrayList<Booster>)
    : RecyclerView.Adapter<RecyclerViewAdapter_Main.MyViewHolder>() {
    inner class MyViewHolder(binding : FragmentMainBoosterAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        val boosterKind = binding.boosterKind
        val boosterName = binding.boosterName
        val boosterImage = binding.boosterImage
        val root = binding.root
    }

    var check : Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : FragmentMainBoosterAdapterBinding =
            FragmentMainBoosterAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val booster = booster_list[position]

        val image_name = booster.name + ".jpg"
        val image_path : StorageReference = FirebaseStorage.getInstance("gs://hypersonal-booster.appspot.com").reference.child(image_name)

        holder.boosterKind.text = booster.class_2
        holder.boosterName.text = booster.name

        /*
        var booster_url_name : String = ""

        if(booster.name.contains(":")) {
            booster_url_name += booster.name.replace(":", "").replace("%", "%25").replace(" ", "%20").replace("+","%2B")
        }
        else {
            booster_url_name += booster.name.replace("%", "%25").replace(" ", "%20").replace("+","%2B")
        }

        val url = "https://firebasestorage.googleapis.com/v0/b/hypersonal-booster.appspot.com/o/" + booster_url_name + ".jpg?alt=media"
        Log.d("ListViewAdapter_Main", "Url : $url")

        holder.boosterImage.load(url) {
            placeholder(R.drawable.icon_loading)
            error(R.drawable.img_scoop)
        }

         */
    }

    override fun getItemCount(): Int {
        return booster_list.size
    }
}