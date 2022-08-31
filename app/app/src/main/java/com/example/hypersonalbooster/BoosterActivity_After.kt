package com.example.hypersonalbooster

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.example.hypersonalbooster.databinding.LayoutBoosterAfterBinding


class BoosterActivity_After : AppCompatActivity(), OnRecommendBoosterClickListener {

    private lateinit var binding : LayoutBoosterAfterBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    var booster_after = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBoosterAfterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val after = shared_cloud.getString("booster_after", "NoBooster")!!.split(",").distinct()

        val shared_after = getSharedPreferences("booster_after", 0)

        for(boosterID in after) {
            val info = shared_after.getString(boosterID, "Nothing")
            if(info != "Nothing") {
                booster_after.add(info.toString())
            }
        }

        setContentView(binding.root)

        val mlAdapter = ListViewAdapter_Booster(this, booster_after,this)
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
