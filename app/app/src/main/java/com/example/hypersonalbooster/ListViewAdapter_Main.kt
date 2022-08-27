package com.example.hypersonalbooster

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import coil.api.load
import com.example.hypersonalbooster.databinding.FragmentMainBoosterAdapterBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ListViewAdapter_Main(private val context: Context, private val booster_list : ArrayList<Booster>)
    : BaseAdapter() {
    override fun getCount() : Int = booster_list.size

    override fun getItem(position :Int) : Booster = booster_list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = FragmentMainBoosterAdapterBinding.inflate(LayoutInflater.from(context))

        val booster = booster_list[position]

        val image_name = booster.name + ".jpg"
        val image_path : StorageReference = FirebaseStorage.getInstance("gs://hypersonal-booster.appspot.com").reference.child(image_name)

        binding.boosterKind.text = booster.class_2
        binding.boosterName.text = booster.name

        val booster_url_name = booster.name.replace("%", "%25").replace(" ", "%20").replace("+","%2B")

        val url = "https://firebasestorage.googleapis.com/v0/b/hypersonal-booster.appspot.com/o/" + booster_url_name + ".jpg?alt=media"
        Log.d("ListViewAdapter_Main", "Url : $url")

        binding.boosterImage.load(url) {
            placeholder(R.drawable.icon_loading)
            error(R.drawable.img_scoop)
        }

        return binding.root
    }
}