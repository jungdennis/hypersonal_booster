package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity1 : AppCompatActivity() {

    private lateinit var btn_confirm : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register_heightweight)

        btn_confirm = findViewById(R.id.confirm)

        btn_confirm.setOnClickListener {
            val intent_next = Intent(this, RegisterActivity2::class.java)
            startActivity(intent_next)
        }
    }

    // 뒤로가기 막기
    override fun onBackPressed() {
        // super.onBackPressed();
    }
}