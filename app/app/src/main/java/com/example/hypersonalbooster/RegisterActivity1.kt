package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterHeightweightBinding

class RegisterActivity1 : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterHeightweightBinding

    lateinit var input_height : String
    lateinit var input_weight : String

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterHeightweightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirm.setOnClickListener {
            input_height = binding.insertHeight.text.toString()
            input_weight = binding.insertWeight.text.toString()

            if(input_weight.isBlank() || input_height.isBlank()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                val shared = getSharedPreferences("data_health", 0)
                val editor = shared.edit()

                editor.putFloat("height", input_height.toFloat())
                editor.putFloat("weight", input_weight.toFloat())
                editor.apply()

                val intent_next = Intent(this, RegisterActivity2::class.java)
                startActivity(intent_next)
            }
        }
    }

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