package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CloudLoadingActivity_Booster : AppCompatActivity(), CloudCallbackListener {


    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    var company_list = ""
    var taste_list = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("BoosterLoading", "Booster Loading Start")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_cloud_loading)

        val shared_booster = getSharedPreferences("data_booster", 0)
        val editor_booster = shared_booster.edit()

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val editor_cloud = shared_cloud.edit()

        val uid = shared_cloud.getString("uid", "Nouid")
        Log.d("RegisterActivity_0", "uid : $uid")

        // 브랜드 정보 받기
        ref.child("booster").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var temp_brand = ArrayList<String>()
                for (snapshot in dataSnapshot.getChildren()) {
                    val brand = snapshot.child("brand").getValue().toString()
                    if(brand.isNotEmpty()) {
                        if(brand !in temp_brand){
                            if(company_list.isEmpty()){
                                company_list = company_list + brand
                            }
                            else{
                                company_list = company_list + "," + brand
                            }
                        }
                        temp_brand.add(brand)
                    }
                }
                editor_booster.putString("company", company_list)
                editor_booster.apply()
                Log.d("BoosterLoading", "Company Loading Success")

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
                Log.d("BoosterLoading", "Taste Loading Success")

                onCallback()
            }

            override fun onCancelled(databaseError: DatabaseError) {}})
    }

    override fun onCallback() {
        val intent = Intent(this, CloudLoadingActivity_Kiosk::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }
}