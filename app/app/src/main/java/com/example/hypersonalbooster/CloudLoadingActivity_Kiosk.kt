package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CloudLoadingActivity_Kiosk : AppCompatActivity(), CloudCallbackListener {

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    var kiosk_list = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("KioskLoading", "Kiosk Loading Start")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_cloud_loading)

        val shared_cloud = getSharedPreferences("data_cloud", 0)

        val uid = shared_cloud.getString("uid", "Nouid")
        Log.d("RegisterActivity_0", "uid : $uid")

        val shared_kiosk = getSharedPreferences("data_kiosk", 0)
        val editor_kiosk = shared_kiosk.edit()

        // 키오스크 정보 받기
        ref.addValueEventListener(object : ValueEventListener {
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
                Log.d("KioskLoading", "Kiosk Loading Success")

                onCallback()
            }

            override fun onCancelled(databaseError: DatabaseError) {}})
    }

    override fun onCallback() {
        val check_cloud = getSharedPreferences("data_cloud", 0).getString("cloud_check", "nothing")
        val check_health = getSharedPreferences("data_health", 0).getString("check_health", "nothing")

        if(check_cloud == "true") {
            if(check_health == "true") {
                val intent = Intent(this, RecommendActivity_Before::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, CloudLoadingActivity_Members::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }
        }
        else{
            val intent = Intent(this, RegisterActivity_0_Welcome::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }
}