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


class ListViewAdapter_Main(private val context: Context, private val booster_list : ArrayList<String>)
    : BaseAdapter() {
    override fun getCount() : Int = booster_list.size

    override fun getItem(position : Int) : String = booster_list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = FragmentMainBoosterAdapterBinding.inflate(LayoutInflater.from(context))

        // ID/name/company/taste/texture/class_0/class_1/class_2/amount/calories/carb/sugar/fat/sat_fat/protein*link
        val booster = booster_list[position].split("*")

        val id = booster[0]
        val name = booster[1]
        val company = booster[2]
        val taste = booster[3]
        val texture = booster[4]
        val class_2 = booster[7]
        val amount = booster[8]
        val calories = booster[9]
        val carb = booster[10]
        val sugar = booster[11]
        val fat = booster[12]
        val sat_fat = booster[13]
        val protein = booster[14]
        val link = booster[15]
        val etc = booster[16]

        binding.boosterKind.text = class_2
        binding.boosterName.text = name

        var booster_url_name : String = ""

        if(name.contains(":")) {
            booster_url_name += name.replace(":", "").replace("%", "%25").replace(" ", "%20").replace("+","%2B")
        }
        else {
            booster_url_name += name.replace("%", "%25").replace(" ", "%20").replace("+","%2B")
        }

        val url = "https://firebasestorage.googleapis.com/v0/b/hypersonal-booster.appspot.com/o/" + booster_url_name + ".jpg?alt=media"
        Log.d("ListViewAdapter_Main", "Url : $url")

        binding.boosterImage.load(url) {
            placeholder(R.drawable.icon_loading)
            error(R.drawable.img_scoop)
        }

        return binding.root
    }


}