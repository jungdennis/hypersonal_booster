package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterBasicBinding

class RegisterActivity_1_Basic : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterBasicBinding
    private lateinit var input_name : String
    private lateinit var input_age : String
    private lateinit var input_sex : String
    private var input_pragent : Boolean = false

    var check_name : Int = 0
    var check_age : Int = 0
    var check_sex : Int = 0
    var check_pragent : Int = 0

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterBasicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.framePragent.setVisibility(View.INVISIBLE)

        binding.sexMan.setOnClickListener {
            binding.sexMan.setBackgroundResource(R.drawable.btn_main_color)
            binding.sexWoman.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_sex = "man"
            input_pragent = false

            check_sex = 1
            check_pragent = 1

            binding.framePragent.setVisibility(View.INVISIBLE)
        }
        binding.sexWoman.setOnClickListener {
            binding.sexMan.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.sexWoman.setBackgroundResource(R.drawable.btn_main_color)

            input_sex = "woman"

            check_sex = 1
            check_pragent = 0

            binding.framePragent.setVisibility(View.VISIBLE)
        }

        binding.pragentYes.setOnClickListener {
            binding.pragentYes.setBackgroundResource(R.drawable.btn_main_color)
            binding.pragentNo.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_pragent = true

            check_pragent = 1
        }
        binding.pragentNo.setOnClickListener {
            binding.pragentYes.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.pragentNo.setBackgroundResource(R.drawable.btn_main_color)

            input_pragent = false

            check_pragent = 1
        }

        binding.confirm.setOnClickListener {
            if(binding.insertName.text.isNotEmpty()) {
                input_name = binding.insertName.text.toString()
                check_name = 1
            }

            if(binding.insertAge.text.isNotEmpty()) {
                input_age = binding.insertAge.text.toString()
                check_age = 1
            }

            if(check_age * check_pragent * check_sex * check_name != 1) {
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                val shared = getSharedPreferences("data_cloud", 0)
                val editor = shared.edit()
                editor.putString("name", input_name)
                editor.putInt("age", input_age.toInt())
                editor.putString("sex", input_sex)
                editor.putBoolean("pragent", input_pragent)
                editor.apply()

                Toast.makeText(this, "$input_name / $input_age / $input_sex / $input_pragent", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, RegisterActivity_2_HeightWeight::class.java)
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