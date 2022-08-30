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
import com.example.hypersonalbooster.databinding.LayoutBoosterBeforeBinding
import com.google.firebase.database.FirebaseDatabase


class BoosterActivity_Before : AppCompatActivity(), OnRecommendBoosterClickListener {

    private lateinit var binding : LayoutBoosterBeforeBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")



    var booster_before = ArrayList<Booster>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBoosterBeforeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val before = shared_cloud.getString("booster_before", "NoBooster")!!.split(",").distinct()

        for(boosterID in before) {
            booster_before.add(Booster(boosterID))
        }

        setContentView(binding.root)

        val mlAdapter = ListViewAdapter_BoosterMain(this, booster_before,this)
        binding.boosterList.adapter = mlAdapter

        binding.boosterList.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val Booster_popup = BoosterFragment_After()
            Booster_popup.show(supportFragmentManager, Booster_popup.tag)
        }

        binding.switch2.setOnCheckedChangeListener { CompoundButton, isChecked ->
            if (isChecked) {
                val mlAdapter = ListViewAdapter_BoosterMain(this, booster_before,this)
                binding.boosterList.adapter = mlAdapter
            }
            else {
                val mlAdapter = ListViewAdapter_BoosterMain(this, booster_before,this)
                binding.boosterList.adapter = mlAdapter
            }
        }

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

    override fun onBoosterClickAdd(booster_name : String){
        Toast.makeText(this, "$booster_name 을 클릭함", Toast.LENGTH_SHORT)
            .show()
    }
    override fun onBoosterClickRemove(booster_name : String){

    }
}
