package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.hypersonalbooster.databinding.LayoutRegisterParticularBinding

class RegisterActivity_9_Particular : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterParticularBinding

    private var input_vegan : Boolean = false
    private var input_milk : Boolean = false
    private var input_caffeine : Boolean = false

    private var check_vegan : Int = 0
    private var check_milk : Int = 0
    private var check_caffeine : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterParticularBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        binding.veganYes.setOnClickListener {
            binding.veganYes.setBackgroundResource(R.drawable.btn_main_color)
            binding.veganNo.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_vegan = true
            check_vegan = 1
        }
        binding.veganNo.setOnClickListener {
            binding.veganNo.setBackgroundResource(R.drawable.btn_main_color)
            binding.veganYes.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_vegan = false
            check_vegan = 1
        }

        binding.milkYes.setOnClickListener {
            binding.milkYes.setBackgroundResource(R.drawable.btn_main_color)
            binding.milkNo.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_milk = true
            check_milk = 1
        }
        binding.milkNo.setOnClickListener {
            binding.milkNo.setBackgroundResource(R.drawable.btn_main_color)
            binding.milkYes.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_milk = false
            check_milk = 1
        }

        binding.caffeineYes.setOnClickListener {
            binding.caffeineYes.setBackgroundResource(R.drawable.btn_main_color)
            binding.caffeineNo.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_caffeine = true
            check_caffeine = 1
        }
        binding.caffeineNo.setOnClickListener {
            binding.caffeineNo.setBackgroundResource(R.drawable.btn_main_color)
            binding.caffeineYes.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_caffeine = false
            check_caffeine = 1
        }

        binding.confirm.setOnClickListener {
            val check = check_vegan * check_milk * check_caffeine
            if(check == 0) {
                Toast.makeText(this, "모든 질문에 답해주세요.", Toast.LENGTH_SHORT).show()
            }
            else if (check == 1){
                val shared = getSharedPreferences("data_cloud", 0)
                val editor = shared.edit()

                editor.putBoolean("vegan", input_vegan)
                editor.putBoolean("milk", input_milk)
                editor.putBoolean("caffeine", input_caffeine)
                editor.apply()

                Toast.makeText(this, "$input_vegan / $input_milk / $input_caffeine", Toast.LENGTH_SHORT).show()
                val intent_next = Intent(this, MainActivity::class.java)
                startActivity(intent_next)
            }
        }
    }
}