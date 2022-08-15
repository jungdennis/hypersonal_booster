package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterCompanyBinding

class RegisterActivity_8_Company : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        binding.booster1.setOnClickListener {
            binding.booster1.setBackgroundResource(R.drawable.btn_main_color)
            binding.booster2.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster3.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster4.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster5.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        binding.booster2.setOnClickListener {
            binding.booster1.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster2.setBackgroundResource(R.drawable.btn_main_color)
            binding.booster3.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster4.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster5.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        binding.booster3.setOnClickListener {
            binding.booster1.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster2.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster3.setBackgroundResource(R.drawable.btn_main_color)
            binding.booster4.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster5.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        binding.booster4.setOnClickListener {
            binding.booster1.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster2.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster3.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster4.setBackgroundResource(R.drawable.btn_main_color)
            binding.booster5.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        binding.booster5.setOnClickListener {
            binding.booster1.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster2.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster3.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster4.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster5.setBackgroundResource(R.drawable.btn_main_color)
        }

        binding.noCompany.setOnClickListener {
            val intent = Intent(this, RegisterActivity_9_Particular::class.java)
            startActivity(intent)
        }
        binding.confirm.setOnClickListener {
            val intent = Intent(this, RegisterActivity_9_Particular::class.java)
            startActivity(intent)
        }
    }
}