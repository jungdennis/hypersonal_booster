package com.example.hypersonalbooster

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.SearchView
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
    var list = ArrayList<String>()

    private lateinit var adapter : ListViewAdapter_Booster


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

        list.addAll(booster_before)

        setContentView(binding.root)

        adapter = ListViewAdapter_Booster(this, list,this)
        binding.boosterList.adapter = adapter


        binding.back.setOnClickListener {
            val intent = Intent(this, BoosterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        binding.supply.setOnClickListener {
            val intent = Intent(this, BoosterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
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

        binding.boosterSearch.setOnQueryTextListener(searchViewTextListener)

    }

    override fun onBackPressed() {
        //super.onBackPressed()

        val intent = Intent(this, BoosterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }

    var searchViewTextListener: SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            //텍스트 입력/수정시에 호출
            override fun onQueryTextChange(s: String): Boolean {
                search(s)
                return false
            }
        }

    private fun search(charText: String) {
        list.clear()

        if (charText.length == 0) {
            list.addAll(booster_before)
        } else {
            for (i in 0 until booster_before.size) {
                if (booster_before.get(i).toLowerCase().contains(charText)) {
                    list.add(booster_before.get(i))
                }
            }
        }

        adapter.notifyDataSetChanged()
    }

    override fun onBoosterClickAdd(booster_info: String) {
        val Booster_popup = BoosterFragment(booster_info)
        Booster_popup.show(supportFragmentManager, Booster_popup.tag)
    }

    override fun onBoosterClickRemove(booster_name: String) {

    }
}
