package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutBoosterMainBinding

class BoosterActivity : AppCompatActivity() {

    lateinit var binding : LayoutBoosterMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutBoosterMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared = getSharedPreferences("data_health", 0)

        var nickname = shared.getString("nickname", "닉네임없음")
        binding.userName.text = nickname

        binding.back.setOnClickListener {
            val main_intent = Intent(this, MainActivity::class.java)
            startActivity(main_intent)
        }
        binding.location.setOnClickListener {
            val map_intent = Intent(this, MapActivity::class.java)
            startActivity(map_intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}