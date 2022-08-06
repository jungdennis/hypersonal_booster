package com.example.hypersonalbooster

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var btn_qr: Button
    private lateinit var btn_supply : Button
    private lateinit var btn_location : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main_frame)

        btn_qr = findViewById(R.id.qr)
        btn_location = findViewById(R.id.location)
        btn_supply = findViewById(R.id.supply)

        val main_fragmentTransaction = supportFragmentManager.beginTransaction()
        main_fragmentTransaction.replace(R.id.main_frame, Fragment1())
        main_fragmentTransaction.commit()

        btn_location.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_frame, Fragment2())
            fragmentTransaction.commit()
        }
        btn_supply.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_frame, Fragment3())
            fragmentTransaction.commit()
        }
    }

    private var end_time: Long = 0

    override fun onBackPressed() {
        // super.onBackPressed()

        if (System.currentTimeMillis() - end_time >= 2000) {
            end_time = System.currentTimeMillis()
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        else if (System.currentTimeMillis() - end_time < 2000) {
            finishAffinity()
        }
    }

}
