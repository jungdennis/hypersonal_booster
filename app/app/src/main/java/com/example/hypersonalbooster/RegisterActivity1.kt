package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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

    private var end_time: Long = 0

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