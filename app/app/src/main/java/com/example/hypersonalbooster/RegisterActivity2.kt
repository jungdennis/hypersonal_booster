package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class RegisterActivity2 : AppCompatActivity() {

    private lateinit var btn_back : Button
    private lateinit var btn_yes : Button
    private lateinit var btn_no : Button
    private lateinit var btn_confirm : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register_inbody)

        btn_back = findViewById(R.id.back)
        btn_yes = findViewById(R.id.yes)
        btn_no = findViewById(R.id.no)
        btn_confirm = findViewById(R.id.confirm)

        btn_confirm.setVisibility(View.INVISIBLE)

        btn_back.setOnClickListener {
            finish()
        }
        btn_yes.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_frame, RegisterFragment2_1())
            fragmentTransaction.commit()
            btn_yes.setBackgroundResource(R.drawable.btn_main_color)
            btn_no.setBackgroundResource(R.drawable.btn_sub_color_light)
            btn_confirm.setVisibility(View.VISIBLE)
        }
        btn_no.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_frame, RegisterFragment2_2())
            fragmentTransaction.commit()
            btn_yes.setBackgroundResource(R.drawable.btn_sub_color_light)
            btn_no.setBackgroundResource(R.drawable.btn_main_color)
            btn_confirm.setVisibility(View.VISIBLE)
        }
        btn_confirm.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}