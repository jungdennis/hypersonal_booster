package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutBoosterMainBinding
import android.widget.Button
import android.widget.Toast
import com.example.hypersonalbooster.databinding.LayoutBoosterAfterBinding
import com.example.hypersonalbooster.databinding.LayoutBoosterBeforeBinding

class BoosterActivity : AppCompatActivity() {

    private lateinit var binding : LayoutBoosterMainBinding

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = LayoutBoosterMainBinding.inflate(layoutInflater)


        setContentView(binding.root)

        binding.moreBefore.setOnClickListener {
            val intent = Intent(this, LayoutBoosterBeforeBinding :: class.java)
            startActivity(intent)
        }
        binding.moreAfter.setOnClickListener {
            val intent = Intent(this, LayoutBoosterAfterBinding :: class.java)
            startActivity(intent)
        }

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
    }
}