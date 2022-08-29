package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CloudLoadingActivity_Members : AppCompatActivity(), CloudCallbackListener {

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("members")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_cloud_loading)

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val editor_cloud = shared_cloud.edit()

        val uid = shared_cloud.getString("uid", "Nouid")
        Log.d("RegisterActivity_0", "uid : $uid")

        ref.child(uid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val age = snapshot.child("Info_Basic").child("age").getValue().toString().toInt()
                val sex = snapshot.child("Info_Basic").child("sex").getValue().toString()
                val pragent = snapshot.child("Info_Basic").child("pragent").getValue().toString()

                val feeling = snapshot.child("Info_Favorite").child("feeling").getValue().toString()
                val company = snapshot.child("Info_Favorite").child("company").getValue().toString()
                val taste = snapshot.child("Info_Favorite").child("taste").getValue().toString()

                val milk = snapshot.child("Info_Particular").child("milk").getValue().toString()
                val caffeine = snapshot.child("Info_Particular").child("caffeine").getValue().toString()
                val vegan = snapshot.child("Info_Particular").child("vegan").getValue().toString()

                editor_cloud.putInt("age", age)
                editor_cloud.putString("sex",sex)
                editor_cloud.putString("pragent", pragent)
                editor_cloud.apply()

                editor_cloud.putString("Feeling", feeling)
                editor_cloud.putString("company",company)
                editor_cloud.putString("taste",taste)
                editor_cloud.apply()

                editor_cloud.putString("milk", milk)
                editor_cloud.putString("caffeine", caffeine)
                editor_cloud.putString("vegan", vegan)
                editor_cloud.apply()

                onCallback()
            }

            override fun onCancelled(databaseError: DatabaseError) {}})
    }

    override fun onCallback() {
        val intent = Intent(this, RegisterActivity_0_Return::class.java)
        startActivity(intent)
    }
}