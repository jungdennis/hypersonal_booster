package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hypersonalbooster.databinding.LayoutRegisterReturnBinding

class RegisterActivity_0_Return : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterReturnBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterReturnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            val intent = Intent(this, RegisterActivity_2_Health::class.java)
            startActivity(intent)
        }
    }
}