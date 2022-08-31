package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hypersonalbooster.databinding.LayoutMapRequestRecommandBinding

class KioskRequestRecommand : AppCompatActivity(), OnRecommendBoosterClickListener {

    private lateinit var binding : LayoutMapRequestRecommandBinding

    var booster_before = ArrayList<KioskBooster>()
    var booster_after = ArrayList<KioskBooster>()
    var booster_init = ArrayList<KioskBooster>()

    var check_kiosk = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutMapRequestRecommandBinding.inflate(layoutInflater)

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val before = shared_cloud.getString("booster_before", "NoBooster")!!.split(",").distinct()
        val after = shared_cloud.getString("booster_after", "NoBooster")!!.split(",").distinct()

        setContentView(binding.root)

        val shared_before = getSharedPreferences("booster_before", 0)
        val shared_after = getSharedPreferences("booster_after", 0)

        for(boosterID in before) {
            val info = shared_before.getString(boosterID, "Nothing")
            if(info != "Nothing") {
                booster_before.add(KioskBooster(info.toString(), false))
            }
        }
        for(boosterID in after) {
            val info = shared_after.getString(boosterID, "Nothing")
            if(info != "Nothing") {
                booster_after.add(KioskBooster(info.toString(), false))
            }
        }
        for(boosterID in before) {
            val info = shared_before.getString(boosterID, "Nothing")
            if(info != "Nothing") {
                booster_init.add(KioskBooster(info.toString(), false))
            }
        }

        val mlAdapter = ListViewAdapter_KioskRecommend(this, booster_init, this)
        binding.boosterList.adapter = mlAdapter

        binding.switch2.setOnCheckedChangeListener { CompoundButton, isChecked ->
            if (isChecked) {
                val mlAdapter = ListViewAdapter_KioskRecommend(this, booster_after, this)
                binding.boosterList.adapter = mlAdapter
            }
            else {
                val mlAdapter = ListViewAdapter_KioskRecommend(this, booster_before, this)
                binding.boosterList.adapter = mlAdapter
            }
        }


        binding.request.setOnClickListener {
            Toast.makeText(this, "보충제를 요청하였습니다", Toast.LENGTH_SHORT)
                .show()
        }

        binding.cancel.setOnClickListener {
            finish()
        }

        binding.search.setOnClickListener {
            val ReqSearch_intent = Intent(this, KioskFragmentDetailSearch::class.java)
            ReqSearch_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(ReqSearch_intent)
            finish()
        }


    }
    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(0, 0)
        finish()
    }

    override fun onBoosterClickAdd(Kiosks_name: String) {
        check_kiosk.add(Kiosks_name)
    }
    override fun onBoosterClickRemove(Kiosks_name: String){
        check_kiosk.remove(Kiosks_name)
    }

}