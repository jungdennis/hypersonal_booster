package com.example.hypersonalbooster

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.hypersonalbooster.databinding.LayoutBoosterBeforeBinding


class BoosterActivity_Before : AppCompatActivity() {

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_booster_before)

        settingButton()
        settingButton2()
        settingButton3()
        settingButton4()

        /*
        binding.back.setOnClickListener {
            val main_intent = Intent(this, MainActivity::class.java)
            main_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(main_intent)
        }
        binding.location.setOnClickListener {
            val location_intent = Intent(this, KioskActivity::class.java)
            location_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(location_intent)
        }
        binding.qr.setOnClickListener {
            val qr_popup = MainFragment_QR()
            qr_popup.show(supportFragmentManager, qr_popup.tag)
        }

         */
}
    fun settingButton(){
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val intent = Intent(this, BoosterActivity_Before :: class.java)
            startActivity(intent)
        }
    }
    fun settingButton2(){
        val button = findViewById<Button>(R.id.button2)
        button.setOnClickListener{
            val intent = Intent(this, BoosterActivity_After :: class.java)
            startActivity(intent)
        }
    }
    fun settingButton3(){
        val button = findViewById<Button>(R.id.button3)
        button.setOnClickListener{
            val intent = Intent(this, BoosterActivity_After :: class.java)
            startActivity(intent)
        }
    }
    fun settingButton4(){
        val button = findViewById<Button>(R.id.button4)
        button.setOnClickListener{
            val intent = Intent(this, BoosterActivity_After :: class.java)
            startActivity(intent)
        }
    }
}
