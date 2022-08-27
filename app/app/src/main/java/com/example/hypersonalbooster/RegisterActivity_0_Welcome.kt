package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterWelcomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*

class RegisterActivity_0_Welcome : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterWelcomeBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref_booster = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")
    val ref_kiosk = database.getReference("kiosk")
    val ref_members = database.getReference("members")


    var company_list = ""
    var taste_list = ""
    var kiosk_list = ""

    // 어플리케이션 테스트용 (나중에 BoosterRecommend로 넘어갈 예정)
    var booster_before = ""
    var booster_after = ""

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared_booster = getSharedPreferences("data_booster", 0)
        val editor_booster = shared_booster.edit()

        val shared_kiosk = getSharedPreferences("data_kiosk", 0)
        val editor_kiosk = shared_kiosk.edit()

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val editor_cloud = shared_cloud.edit()

        val uid = shared_cloud.getString("uid", "Nouid")
        Log.d("RegisterActivity_0", "uid : $uid")
        if(uid == "Nouid") {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            val health_check = getSharedPreferences("data_health", 0).getString("health_check", "Nothing")
            val cloud_check = shared_cloud.getString("cloud_check", "Nothing")
            Log.d("RegisterActivity_0", "health_check, cloud_check : $health_check, $cloud_check")

            if(health_check == "true" && cloud_check == "true") {
                val intent = Intent(this, RecommendActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // 브랜드 정보 받기
        ref_booster.child("booster").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var temp = ArrayList<String>()
                for (snapshot in dataSnapshot.getChildren()) {
                    val brand = snapshot.child("brand").getValue().toString()
                    if(brand.isNotEmpty()) {
                        if(brand !in temp){
                            if(company_list.isEmpty()){
                                company_list = company_list + brand
                            }
                            else{
                                company_list = company_list + "," + brand
                            }
                        }
                        temp.add(brand)
                    }
                }
                editor_booster.putString("company", company_list)
                editor_booster.apply()
            }

            override fun onCancelled(databaseError: DatabaseError) {}})

        // 맛 정보 받기
        ref_booster.child("booster").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var temp = ArrayList<String>()
                for (snapshot in dataSnapshot.getChildren()) {
                    val taste = snapshot.child("taste2").getValue().toString()
                    if(taste.isNotEmpty()) {
                        if(taste !in temp){
                            if(taste_list.isEmpty()){
                                taste_list = taste_list + taste
                            }
                            else{
                                taste_list = taste_list + "," + taste
                            }
                        }
                        temp.add(taste)
                    }
                }
                editor_booster.putString("taste", taste_list)
                editor_booster.apply()
            }

            override fun onCancelled(databaseError: DatabaseError) {}})

        // 키오스크 정보 받기
        ref_kiosk.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var temp = ArrayList<String>()
                for (snapshot in dataSnapshot.getChildren()) {
                    val name = snapshot.child("name").getValue().toString()
                    val location = snapshot.child("location").getValue().toString()
                    val kiosk = name + "," + location
                    if(kiosk.isNotEmpty()) {
                        if(kiosk !in temp){
                            if(kiosk_list.isEmpty()){
                                kiosk_list = kiosk_list + kiosk
                            }
                            else{
                                kiosk_list = kiosk_list + "/" + kiosk
                            }
                        }
                        temp.add(kiosk)
                    }
                }
                editor_kiosk.putString("kiosk", kiosk_list)
                editor_kiosk.apply()
            }

            override fun onCancelled(databaseError: DatabaseError) {}})

        // 어플리케이션 테스트용 (나중에 BoosterRecommend로 넘어갈 예정)
        if(uid != "Nouid"){
            ref_members.child(uid!!).child("Booster_before").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.getChildren()) {
                        val booster = data.getValue().toString().split(",")[0].toString()
                        if(booster.isNotEmpty()) {
                            if(booster_before.isEmpty()){
                                booster_before = booster_before + booster
                            }
                            else{
                                booster_before = booster_before + "," + booster
                            }
                        }
                    }
                    editor_cloud.putString("booster_before", booster_before)
                    editor_cloud.apply()
                }

                override fun onCancelled(error: DatabaseError) { } })

            ref_members.child(uid).child("Booster_after").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.getChildren()) {
                        val booster = data.getValue().toString().split(",")[0].toString()
                        if(booster.isNotEmpty()) {
                            if(booster_after.isEmpty()){
                                booster_after = booster_after + booster
                            }
                            else{
                                booster_after = booster_after + "," + booster
                            }
                        }
                    }
                    editor_cloud.putString("booster_after", booster_after)
                    editor_cloud.apply()
                }

                override fun onCancelled(error: DatabaseError) { } })

            // 키오스크 테스트용 코드 (나중에 지울 것!)
            val save_before = ref_members.child(uid).child("Booster_before")
            save_before.child("bp1").setValue("CLB0111-04,20")
            save_before.child("bp2").setValue("CLB0111-03,32")
            save_before.child("bp3").setValue("CLB0111-05,33")

            val save_after = ref_members.child(uid).child("Booster_after")
            save_after.child("ap1").setValue("CLB0111-05,41")
            save_after.child("ap2").setValue("CLB0111-04,50")
            save_after.child("ap3").setValue("CLB0111-03,60")
        }


        binding.start.setOnClickListener {
            val intent = Intent(this, RegisterActivity_1_Basic::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed()


        // 나중에 첫 화면으로 빠질 예정
        var test = getSharedPreferences("data_booster", 0).getString("company", "failed")
        Log.d("RegisterActiviy_0_Welcome", "Brand : $test")
        test = getSharedPreferences("data_booster", 0).getString("taste", "failed")
        Log.d("RegisterActiviy_0_Welcome", "Taste : $test")
        test = getSharedPreferences("data_kiosk", 0).getString("kiosk", "failed")
        Log.d("RegisterActiviy_0_Welcome", "Kiosk : $test")
        test = getSharedPreferences("data_cloud", 0).getString("booster_before", "failed")
        Log.d("Set Booster Before", "$test")
        test = getSharedPreferences("data_cloud", 0).getString("booster_after", "failed")
        Log.d("Set Booster After", "$test")


        if (System.currentTimeMillis() - end_time >= 2000) {
            end_time = System.currentTimeMillis()
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else if (System.currentTimeMillis() - end_time < 2000) {
            finishAffinity()
        }
    }
}