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
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster")

    private var end_time: Long = 0

    var company_list = ""
    var taste_list = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shared = getSharedPreferences("data_booster", 0)
        val editor = shared.edit()

        binding = LayoutRegisterWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 브랜드 정보 받기
        ref.addValueEventListener(object : ValueEventListener {
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
                editor.putString("company", company_list)
                editor.apply()
            }

            override fun onCancelled(databaseError: DatabaseError) {}})

        // 맛 정보 받기
        ref.addValueEventListener(object : ValueEventListener {
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
                editor.putString("taste", taste_list)
                editor.apply()
            }

            override fun onCancelled(databaseError: DatabaseError) {}})

        binding.start.setOnClickListener {
            val intent = Intent(this, RegisterActivity_1_Basic::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed()

        var test = getSharedPreferences("data_booster", 0).getString("company", "failed")
        Log.d("RegisterActiviy_0_Welcome", "Brand : $test")
        test = getSharedPreferences("data_booster", 0).getString("taste", "failed")
        Log.d("RegisterActiviy_0_Welcome", "Taste : $test")

        if (System.currentTimeMillis() - end_time >= 2000) {
            end_time = System.currentTimeMillis()
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else if (System.currentTimeMillis() - end_time < 2000) {
            finishAffinity()
        }
    }
}