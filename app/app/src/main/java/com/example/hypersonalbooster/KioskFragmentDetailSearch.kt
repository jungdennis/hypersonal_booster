package com.example.hypersonalbooster

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.FragmentMapRequestSearchBinding
import com.google.firebase.database.FirebaseDatabase


class KioskFragmentDetailSearch : AppCompatActivity(), OnRecommendBoosterClickListener{

    private lateinit var binding: FragmentMapRequestSearchBinding

    var Boosters_list = ArrayList<Booster>()
    var list = ArrayList<Booster>()

    private lateinit var adapter : ListViewAdapter_KioskBoosterSearch

    var check_booster = ArrayList<String>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = FragmentMapRequestSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared_BoosterID = getSharedPreferences("BoosterID_list", 0).getString("BoosterID_list", "failed")
        Log.d("shared_boosterID", "here:$shared_BoosterID")
        val BID_temp = shared_BoosterID!!.split("/").distinct() as ArrayList<String>
        Log.d("BID_temp", "BIDBID : $BID_temp")

        for(boosterID in BID_temp) {
            Boosters_list.add(Booster(boosterID))
        }

        list.addAll(Boosters_list)


        binding.boosterSearch.setOnQueryTextListener(searchViewTextListener)
        adapter = ListViewAdapter_KioskBoosterSearch(this, list, this)
        binding.reqSearchSelect.adapter = adapter




        binding.request.setOnClickListener {
            if(check_booster.isEmpty()){
                Toast.makeText(this, "보충제를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "보충제를 요청하였습니다", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        binding.cancel.setOnClickListener {
            finish()
        }

        binding.recommend.setOnClickListener {
            val ReqRecomm_intent = Intent(this, KioskRequestRecommand::class.java)
            ReqRecomm_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(ReqRecomm_intent)
        }

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
            list.addAll(Boosters_list)
        } else {
            for (i in 0 until Boosters_list.size) {
                if (Boosters_list.get(i).name.toLowerCase().contains(charText)) {
                    list.add(Boosters_list.get(i))
                }
            }
        }

        adapter.notifyDataSetChanged()
    }


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(0, 0)
        finish()
    }

    override fun onBoosterClickAdd(booster_name : String){
        check_booster.add(booster_name)
    }
    override fun onBoosterClickRemove(booster_name: String){
        check_booster.remove(booster_name)
    }

}








