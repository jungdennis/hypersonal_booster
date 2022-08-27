package com.example.hypersonalbooster

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.hypersonalbooster.databinding.LayoutBoosterBeforeBinding
import com.google.firebase.database.FirebaseDatabase


class BoosterActivity_Before : AppCompatActivity() {

    private lateinit var binding : LayoutBoosterBeforeBinding



    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    var booster_before = ArrayList<Booster>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBoosterBeforeBinding.inflate(layoutInflater)


        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val before = shared_cloud.getString("booster_before", "NoBooster")!!.split(",")

        for(boosterID in before) {
            booster_before.add(Booster(boosterID))
        }

        val mlAdapter = LIstViewAdapter_BoosterMain(this, booster_before)

        binding.boosterList.adapter = mlAdapter

        setContentView(binding.root)

        binding.back.setOnClickListener {
            val main_intent = Intent(this, MainActivity::class.java)
            main_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(main_intent)
        }

        binding.supply.setOnClickListener {
            val supply_intent = Intent(this, BoosterActivity::class.java)
            supply_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(supply_intent)
        }
        binding.location.setOnClickListener {
            val location_intent = Intent(this, KioskActivity::class.java)
            location_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(location_intent)
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
}
