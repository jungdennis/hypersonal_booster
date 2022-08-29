package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterTargetBinding

class RegisterActivity_3_Target : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterTargetBinding

    private lateinit var input_target_weight : String
    private lateinit var input_target_fat : String
    private lateinit var input_target_muscle : String

    private var normal_weight : Float = 0.0F
    private var normal_fat : Float = 0.0F
    private var normal_muscle : Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterTargetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared_health = getSharedPreferences("data_health", 0)
        val shared_cloud = getSharedPreferences("data_cloud", 0)

        var now_weight = shared_health.getFloat("weight", 0F)
        var now_fat = shared_health.getFloat("fat", 0F)
        var now_muscle = shared_health.getFloat("muscle", 0F)
        var now_height = shared_health.getFloat("height", 0F)
        val sex = shared_cloud.getString("sex", "man")

        val health_check = shared_health.getString("health_check", "Nothing")
        val cloud_check = shared_cloud.getString("cloud_check", "Nothing")

        if(health_check =="true") {
            binding.close.setVisibility(View.VISIBLE)
            binding.back.setVisibility(View.VISIBLE)
        }
        else {
            binding.close.setVisibility(View.INVISIBLE)
            binding.back.setVisibility(View.VISIBLE)
        }

        binding.close.setOnClickListener {
            val intent = Intent(this, RecommendActivity_After::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
        binding.back.setOnClickListener {
            finish()
        }

        binding.nowWeight.text = now_weight.toString()
        binding.nowFat.text = now_fat.toString()
        binding.nowMuscle.text = now_muscle.toString()

        binding.targetWeight.hint = now_weight.toString()
        binding.targetFat.hint = now_fat.toString()
        binding.targetMuscle.hint = now_muscle.toString()


        if(sex == "man") {
            normal_weight = (now_height / 100) * (now_height / 100) * 22
            normal_fat = 15.0F
            normal_muscle = (normal_weight * 0.45).toFloat()
        }
        else {
            normal_weight = (now_height / 100) * (now_height / 100) * 21
            normal_fat = 15.0F
            normal_muscle = (normal_weight * 0.375).toFloat()
        }

        binding.noTraget.setOnClickListener {
            input_target_weight = String.format("%.1f", normal_weight)
            input_target_fat = String.format("%.1f", normal_fat)
            input_target_muscle = String.format("%.1f", normal_muscle)

            val editor = shared_health.edit()
            editor.putFloat("target_weight", input_target_weight.toFloat())
            editor.putFloat("target_fat", input_target_fat.toFloat())
            editor.putFloat("target_muscle", input_target_muscle.toFloat())
            editor.putString("health_check", "true")
            editor.apply()

            Toast.makeText(this, "$input_target_weight / $input_target_fat / $input_target_muscle", Toast.LENGTH_SHORT).show()

            if(cloud_check == "true") {
                val intent = Intent(this, RecommendActivity_Before::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, RegisterActivity_4_Feeling::class.java)
                startActivity(intent)
            }
        }
        binding.confirm.setOnClickListener {
            input_target_weight = binding.targetWeight.text.toString()
            input_target_fat = binding.targetFat.text.toString()
            input_target_muscle = binding.targetMuscle.text.toString()

            if(input_target_weight.isEmpty()){
                input_target_weight = String.format("%.1f", normal_weight)
            }
            if(input_target_fat.isEmpty()){
                input_target_fat = String.format("%.1f", normal_fat)
            }
            if(input_target_muscle.isEmpty()) {
                input_target_muscle = String.format("%.1f", normal_muscle)
            }

            val editor = shared_health.edit()
            editor.putFloat("target_weight", input_target_weight.toFloat())
            editor.putFloat("target_fat", input_target_fat.toFloat())
            editor.putFloat("target_muscle", input_target_muscle.toFloat())
            editor.putString("health_check", "true")
            editor.apply()

            Toast.makeText(this, "$input_target_weight / $input_target_fat / $input_target_muscle", Toast.LENGTH_SHORT).show()

            if(cloud_check == "true") {
                val intent = Intent(this, RecommendActivity_Before::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, RegisterActivity_4_Feeling::class.java)
                startActivity(intent)
            }
        }
    }
}