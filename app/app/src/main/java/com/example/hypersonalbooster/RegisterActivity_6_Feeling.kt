package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterFeelingBinding

class RegisterActivity_6_Feeling : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterFeelingBinding
    private lateinit var input_feeling : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterFeelingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        binding.milky.setOnClickListener {
            binding.milky.setBackgroundResource(R.drawable.btn_main_color)
            binding.clean.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.anything.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_feeling = "milky"
        }
        binding.clean.setOnClickListener {
            binding.milky.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.clean.setBackgroundResource(R.drawable.btn_main_color)
            binding.anything.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_feeling = "clean"
        }
        binding.anything.setOnClickListener {
            binding.milky.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.clean.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.anything.setBackgroundResource(R.drawable.btn_main_color)

            input_feeling = "anything"
        }

        binding.confirm.setOnClickListener {
            if(input_feeling.isEmpty()) {
                Toast.makeText(this, "원하시는 느낌을 하나 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                val shared = getSharedPreferences("data_cloud", 0)
                val editor = shared.edit()

                editor.putString("Feeling", input_feeling)
                editor.apply()

                val intent = Intent(this, RegisterActivity_8_Company::class.java)
                startActivity(intent)
            }
        }
    }
}