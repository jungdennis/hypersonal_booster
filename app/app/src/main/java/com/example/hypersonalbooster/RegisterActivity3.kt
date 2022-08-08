package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity3 : AppCompatActivity() {

    private lateinit var btn_confirm : Button
    private lateinit var insert_nickname : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register_name)

        btn_confirm = findViewById(R.id.confirm)
        insert_nickname = findViewById(R.id.insert_nickname)

        btn_confirm.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}