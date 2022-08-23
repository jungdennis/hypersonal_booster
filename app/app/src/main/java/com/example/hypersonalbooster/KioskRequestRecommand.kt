package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hypersonalbooster.databinding.LayoutMapRequestRecommandBinding

class KioskRequestRecommand : AppCompatActivity() {

    private lateinit var binding : LayoutMapRequestRecommandBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutMapRequestRecommandBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.request.setOnClickListener {
            Toast.makeText(this, "보충제를 요청하였습니다", Toast.LENGTH_SHORT)
                .show()
        }

        binding.cancel.setOnClickListener {
            finish()
        }

        binding.search.setOnClickListener {
            val ReqSearch_intent = Intent(this, KioskFragmentDetailSearch::class.java)
            ReqSearch_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(ReqSearch_intent)
        }


    }
    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(0, 0)
        finish()
    }

}