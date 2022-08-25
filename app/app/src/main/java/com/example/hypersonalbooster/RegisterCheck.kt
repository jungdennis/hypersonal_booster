package com.example.hypersonalbooster

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterCheck : AppCompatActivity() {
    fun health_check() : Boolean {
        val check = getSharedPreferences("data_health", 0)

        val height = check.getFloat("height", 0.0F)
        val weight = check.getFloat("weight", 0.0F)

        if(height == 0.0F || weight == 0.0F) {
            Log.d("health_check", "false")
            return false
        }
        else {
            Log.d("health_check", "true")
            return true
        }
    }

    fun cloud_check(uid : String) : Boolean {
        var flag : String? = null
        val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
        val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/appdata")

        ref.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                flag = dataSnapshot.child("cloud_flag").getValue().toString()
            }
            override fun onCancelled(databaseError: DatabaseError) {}})

        if(flag == null) {
            Log.d("cloud_check", "false")
            return false
        }
        else{
            Log.d("cloud_check", "true")
            return true
        }
    }
}