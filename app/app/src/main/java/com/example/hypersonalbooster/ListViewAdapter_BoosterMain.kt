package com.example.hypersonalbooster

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import coil.api.load
import com.example.hypersonalbooster.databinding.FragmentBoosterButtonAdapterBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference



class ListViewAdapter_BoosterMain(private val context: Context, private val booster_list : ArrayList<Booster>)
    : BaseAdapter() {

    override fun getCount() : Int = booster_list.size

    override fun getItem(position :Int) : Booster = booster_list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = FragmentBoosterButtonAdapterBinding.inflate(LayoutInflater.from(context))

        val booster = booster_list[position]

        val image_name = booster.name + "jpg"

        binding.boosterName.text = "제품명 : " + booster.name
        binding.boosterInfo.text = "제조사 : " + booster.company
        binding.boosterFlavor.text = " 맛  : " + booster.taste1

        val booster_url_name = booster.name.replace("%", "%25").replace(" ", "%20").replace("+","%2B")

        val booster_image = context.resources.getIdentifier("img_main", "drawable", context.packageName)

        val url = "https://firebasestorage.googleapis.com/v0/b/hypersonal-booster.appspot.com/o/" + booster_url_name + ".jpg?alt=media"
        Log.d("ListViewAdapter_Main", "Url : $url")

        binding.boosterImage.load(url) {
            placeholder(R.drawable.icon_loading)
            error(R.drawable.img_scoop)
        }

        return binding.root
    }


}
