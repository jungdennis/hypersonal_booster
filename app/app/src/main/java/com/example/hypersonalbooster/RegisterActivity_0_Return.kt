package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hypersonalbooster.databinding.LayoutRegisterReturnBinding

class RegisterActivity_0_Return : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterReturnBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding = LayoutRegisterReturnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            val intent = Intent(this, RegisterActivity_2_Health::class.java)
            startActivity(intent)
        }
    }
}