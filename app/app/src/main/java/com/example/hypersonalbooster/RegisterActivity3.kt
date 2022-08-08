package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterNameBinding

class RegisterActivity3 : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterNameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.confirm.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}