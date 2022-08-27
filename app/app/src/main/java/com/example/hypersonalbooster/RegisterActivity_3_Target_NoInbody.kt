package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.hypersonalbooster.databinding.LayoutRegisterTargetNoInbodyBinding

class RegisterActivity_3_Target_NoInbody : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterTargetNoInbodyBinding

    private lateinit var input_target_weight : String

    private var normal_weight : Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterTargetNoInbodyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared_health = getSharedPreferences("data_health", 0)
        val shared_cloud = getSharedPreferences("data_cloud", 0)

        var now_weight = shared_health.getFloat("weight", 0F)
        var now_height = shared_health.getFloat("height", 0F)
        val sex = shared_cloud.getString("sex", "man")

        val health_check = shared_health.getString("health_check", "Nothing")
        val cloud_check = shared_cloud.getString("cloud_check", "Nothing")

        if(health_check =="true") {
            binding.close.setVisibility(View.VISIBLE)
            binding.back.setVisibility(View.INVISIBLE)
        }
        else {
            binding.close.setVisibility(View.INVISIBLE)
            binding.back.setVisibility(View.VISIBLE)
        }

        binding.close.setOnClickListener {
            finish()
        }
        binding.back.setOnClickListener {
            finish()
        }

        binding.nowWeight.text = now_weight.toString()
        binding.targetWeight.hint = now_weight.toString()


        if (sex == "man") {
            normal_weight = (now_height / 100) * (now_height / 100) * 22
        } else if (sex == "woman") {
            normal_weight = (now_height / 100) * (now_height / 100) * 21
        }

        binding.noTraget.setOnClickListener {
            input_target_weight = String.format("%.1f", normal_weight)

            val editor = shared_health.edit()
            editor.putFloat("target_weight", input_target_weight.toFloat())
            editor.putFloat("target_fat", 0.0F)
            editor.putFloat("target_muscle", 0.0F)
            editor.putString("health_check", "true")
            editor.apply()

            Toast.makeText(this, "$input_target_weight", Toast.LENGTH_SHORT).show()

            if(cloud_check == "true") {
                if(health_check == "false") {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            else {
                val intent = Intent(this, RegisterActivity_4_Feeling::class.java)
                startActivity(intent)
            }
        }
        binding.confirm.setOnClickListener {
            input_target_weight = binding.targetWeight.text.toString()

            if (input_target_weight.isEmpty()) {
                input_target_weight = String.format("%.1f", normal_weight)
            }

            val editor = shared_health.edit()
            editor.putFloat("target_weight", input_target_weight.toFloat())
            editor.putFloat("target_fat", 0.0F)
            editor.putFloat("target_muscle", 0.0F)
            editor.putString("health_check", "true")
            editor.apply()

            Toast.makeText(this, "$input_target_weight", Toast.LENGTH_SHORT).show()

            if(cloud_check == "true") {
                if(health_check == "false") {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            else {
                val intent = Intent(this, RegisterActivity_4_Feeling::class.java)
                startActivity(intent)
            }
        }
    }
}