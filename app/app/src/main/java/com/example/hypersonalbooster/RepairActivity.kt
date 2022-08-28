package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRecommendBeforeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RepairActivity : AppCompatActivity(), CloudCallbackListener {
    lateinit var binding : LayoutRecommendBeforeBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRecommendBeforeBinding.inflate(layoutInflater)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.getChildren()) {
                    val name = snapshot.child("ID").getValue().toString()
                    ref.child(name).setValue(snapshot)
                }

                onCallback()
            }

            override fun onCancelled(databaseError: DatabaseError) {}})
    }

    override fun onCallback() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}