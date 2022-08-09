package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterNameBinding

class RegisterActivity3 : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterNameBinding
    private lateinit var input_nickmane : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirm.setOnClickListener {
            input_nickmane = binding.insertNickname.text.toString()

            val shared = getSharedPreferences("data_health", 0)
            val editor = shared.edit()
            editor.putString("nickname", input_nickmane)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}