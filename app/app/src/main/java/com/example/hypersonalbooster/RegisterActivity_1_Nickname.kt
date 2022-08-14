package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterNameBinding

class RegisterActivity_1_Nickname : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterNameBinding
    private lateinit var input_nickmane : String

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirm.setOnClickListener {
            input_nickmane = binding.insertNickname.text.toString()
            val input_length : Int = binding.insertNickname.text.toString().length

            if (input_length > 8) {
                Toast.makeText(this, "정해진 글자수를 초과하였습니다. / $input_length", Toast.LENGTH_SHORT).show()
            }
            else{
                val shared = getSharedPreferences("data_cloud", 0)
                val editor = shared.edit()
                editor.putString("nickname", input_nickmane)
                editor.apply()

                Toast.makeText(this, "$input_length", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, RegisterActivity_2_Sex::class.java)
                startActivity(intent)
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