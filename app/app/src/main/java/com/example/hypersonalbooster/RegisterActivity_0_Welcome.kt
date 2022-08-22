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


    var company_list = ""
    var taste_list = ""
    var kiosk_list = ""

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared_booster = getSharedPreferences("data_booster", 0)
        val editor_booster = shared_booster.edit()

        val shared_kiosk = getSharedPreferences("data_kiosk", 0)
        val editor_kiosk = shared_kiosk.edit()


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


        if (System.currentTimeMillis() - end_time >= 2000) {
            end_time = System.currentTimeMillis()
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else if (System.currentTimeMillis() - end_time < 2000) {
            finishAffinity()
        }
    }
}