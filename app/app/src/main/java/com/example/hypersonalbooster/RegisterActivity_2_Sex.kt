package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.hypersonalbooster.databinding.LayoutRegisterSexBinding

class RegisterActivity_2_Sex : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterSexBinding
    private lateinit var input_sex : String
    private  var input_pragent : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = LayoutRegisterSexBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.framePragent.setVisibility(View.INVISIBLE)
        binding.confirm.setVisibility(View.INVISIBLE)

        binding.man.setOnClickListener {
            binding.man.setBackgroundResource(R.drawable.btn_main_color)
            binding.woman.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_sex = "man"
            input_pragent = false

            binding.framePragent.setVisibility(View.INVISIBLE)
            binding.confirm.setVisibility(View.VISIBLE)
        }
        binding.woman.setOnClickListener {
            binding.man.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.woman.setBackgroundResource(R.drawable.btn_main_color)

            input_sex = "woman"

            binding.framePragent.setVisibility(View.VISIBLE)
        }

        binding.yes.setOnClickListener {
            binding.yes.setBackgroundResource(R.drawable.btn_main_color)
            binding.no.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_pragent = true

            binding.confirm.setVisibility(View.VISIBLE)
        }
        binding.no.setOnClickListener {
            binding.yes.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.no.setBackgroundResource(R.drawable.btn_main_color)

            input_pragent = false

            binding.confirm.setVisibility(View.VISIBLE)
        }

        binding.confirm.setOnClickListener {
            val shared = getSharedPreferences("data_cloud", 0)
            val editor = shared.edit()

            editor.putString("sex", input_sex)
            editor.putBoolean("pragent", input_pragent)
            editor.apply()

            Toast.makeText(this, "$input_sex / $input_pragent", Toast.LENGTH_SHORT).show()

            val intent_next = Intent(this, RegisterActivity_3_HeightWeight::class.java)
            startActivity(intent_next)
        }
    }
}