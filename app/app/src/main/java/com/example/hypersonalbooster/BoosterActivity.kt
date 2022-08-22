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
            val intent = Intent(this, BoosterActivity_Before :: class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        binding.moreAfter.setOnClickListener {
            val intent = Intent(this, BoosterActivity_After :: class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            overridePendingTransition(0, 0)
            finish()
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

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(0, 0)
        finish()
    }
}