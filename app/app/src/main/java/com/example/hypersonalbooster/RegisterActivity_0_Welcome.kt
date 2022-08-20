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

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            val intent = Intent(this, RegisterActivity_1_Basic::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed()

        if (System.currentTimeMillis() - end_time >= 2000) {
            end_time = System.currentTimeMillis()
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else if (System.currentTimeMillis() - end_time < 2000) {
            finishAffinity()
        }
    }
}