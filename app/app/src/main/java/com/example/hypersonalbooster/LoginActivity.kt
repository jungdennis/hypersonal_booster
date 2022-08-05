package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {

    private lateinit var btn_login : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)

        btn_login = findViewById(R.id.google_login)

        btn_login.setOnClickListener {
            val intent_login = Intent(this, RegisterActivity1::class.java)
            startActivity(intent_login)
        }
    }
}