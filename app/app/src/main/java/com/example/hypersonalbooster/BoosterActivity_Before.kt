package com.example.hypersonalbooster

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import com.example.hypersonalbooster.databinding.FragmentBoosterButtonAdapterBinding
import com.example.hypersonalbooster.databinding.LayoutBoosterAfterBinding
import com.example.hypersonalbooster.databinding.LayoutBoosterBeforeBinding
import com.google.firebase.database.FirebaseDatabase


class BoosterActivity_Before : AppCompatActivity(), OnRecommendBoosterClickListener {

    private lateinit var binding : LayoutBoosterBeforeBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    var booster_before = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBoosterBeforeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val before = shared_cloud.getString("booster_before", "NoBooster")!!.split(",").distinct()

        val shared_before = getSharedPreferences("booster_before", 0)

        for(boosterID in before) {
            val info = shared_before.getString(boosterID, "Nothing")
            if(info != "Nothing") {
                booster_before.add(info.toString())
            }
        }

        setContentView(binding.root)

        val mlAdapter = ListViewAdapter_Booster(this, booster_before,this)
        binding.boosterList.adapter = mlAdapter


        binding.back.setOnClickListener {
            overridePendingTransition(0, 0)
            finish()
        }

        binding.supply.setOnClickListener {
            finish()
        }
        binding.location.setOnClickListener {
            val location_intent = Intent(this, KioskActivity::class.java)
            location_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(location_intent)
            finish()
        }
        binding.qr.setOnClickListener {
            val qr_popup = MainFragment_QR()
            qr_popup.show(supportFragmentManager, qr_popup.tag)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(0, 0)
        finish()
    }

    override fun onBoosterClickAdd(booster_info: String) {
        val Booster_popup = BoosterFragment(booster_info)
        Booster_popup.show(supportFragmentManager, Booster_popup.tag)
    }

    override fun onBoosterClickRemove(booster_name: String) {

    }
}
