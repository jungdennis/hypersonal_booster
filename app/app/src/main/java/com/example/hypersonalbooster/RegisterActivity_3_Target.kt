package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterTargetBinding
import kotlin.math.roundToInt

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
        val age = shared_cloud.getInt("age", 0)

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
            val intent = Intent(this, RegisterActivity_2_Health::class.java)
            startActivity(intent)
            finish()
        }

        binding.nowWeight.text = now_weight.toString()
        binding.nowFat.text = now_fat.toString()
        binding.nowMuscle.text = now_muscle.toString()

        binding.targetWeight.hint = now_weight.toString()
        binding.targetFat.hint = now_fat.toString()
        binding.targetMuscle.hint = now_muscle.toString()

        var fat_boundary = ArrayList<Float>()


        if(sex == "man") {
            normal_weight = (now_height / 100) * (now_height / 100) * 22
            normal_muscle = (normal_weight * 0.45).toFloat()

            if(age < 18) {
                normal_fat = 16.0F
                fat_boundary.add(9.0F)
                fat_boundary.add(23.0F)
            }
            else if(age < 40) {
                normal_fat = 16.0F
                fat_boundary.add(11.0F)
                fat_boundary.add(21.0F)
            }
            else if(age < 60) {
                normal_fat = 17.0F
                fat_boundary.add(12.0F)
                fat_boundary.add(22.0F)
            }
            else {
                normal_fat = 20.0F
                fat_boundary.add(15.0F)
                fat_boundary.add(25.0F)
            }
        }
        else if(sex=="woman") {
            normal_weight = (now_height / 100) * (now_height / 100) * 21
            normal_muscle = (normal_weight * 0.36).toFloat()

            if(age < 18) {
                normal_fat = 26.5F
                fat_boundary.add(18.0F)
                fat_boundary.add(35.0F)
            }
            else if(age < 40) {
                fat_boundary.add(21.0F)
                fat_boundary.add(34.0F)
                normal_fat = 27.5F
            }
            else if(age < 60) {
                fat_boundary.add(22.0F)
                fat_boundary.add(35.0F)
                normal_fat = 28.5F
            }
            else {
                fat_boundary.add(23.0F)
                fat_boundary.add(36.0F)
                normal_fat = 29.5F
            }
        }

        val weight_boundary = arrayOf(normal_weight*0.9.toFloat(), normal_weight*1.1.toFloat())
        val muscle_boundary = arrayOf(normal_muscle*0.9.toFloat(), normal_muscle*1.1.toFloat())

        weight_display(binding, now_weight, weight_boundary[0], weight_boundary[1])
        fat_display(binding, now_fat, fat_boundary[0], fat_boundary[1])
        muscle_display(binding, now_muscle, muscle_boundary[0], muscle_boundary[1])

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
                val shared_flag = getSharedPreferences("data_cloud", 0).edit()
                shared_flag.remove("flag_before").apply()
                shared_flag.remove("flag_after").apply()

                val intent = Intent(this, RecommendActivity_Before::class.java)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, RegisterActivity_4_Feeling::class.java)
                startActivity(intent)
            }
        }
    }

    fun weight_display(binding: LayoutRegisterTargetBinding, weight : Float, boundary1 : Float, boundary2 : Float) {
        binding.weightLess.setVisibility(View.INVISIBLE)
        binding.weightBoundary1.setVisibility(View.INVISIBLE)
        binding.weightNormal.setVisibility(View.INVISIBLE)
        binding.weightBoundary2.setVisibility(View.INVISIBLE)
        binding.weightMuch.setVisibility(View.INVISIBLE)

        binding.weightNormalMin.text = "%.1f".format(boundary1) + "kg"
        binding.weightNormalMax.text = "%.1f".format(boundary2) + "kg"

        val display_weight = ((weight * 10.0).roundToInt() / 10.0).toFloat()

        if(display_weight < boundary1) {
            binding.weightLess.setVisibility(View.VISIBLE)
            binding.weightDisplayMin.text = "%.1f".format(display_weight) + "kg"
        }
        else if(display_weight == boundary1) {
            binding.weightBoundary1.setVisibility(View.VISIBLE)
            binding.weightDisplayBoundary1.text = "%.1f".format(display_weight) + "kg"
        }
        else if(display_weight > boundary1 && display_weight < boundary2) {
            binding.weightNormal.setVisibility(View.VISIBLE)
            binding.weightDisplayNormal.text = "%.1f".format(display_weight) + "kg"
        }
        else if(display_weight == boundary2) {
            binding.weightBoundary2.setVisibility(View.VISIBLE)
            binding.weightDisplayBoundary2.text = "%.1f".format(display_weight) + "kg"
        }
        else {
            binding.weightMuch.setVisibility(View.VISIBLE)
            binding.weightDisplayMuch.text = "%.1f".format(display_weight) + "kg"
        }
    }

    fun fat_display(binding: LayoutRegisterTargetBinding, fat : Float, boundary1 : Float, boundary2 : Float) {
        binding.fatLess.setVisibility(View.INVISIBLE)
        binding.fatBoundary1.setVisibility(View.INVISIBLE)
        binding.fatNormal.setVisibility(View.INVISIBLE)
        binding.fatBoundary2.setVisibility(View.INVISIBLE)
        binding.fatMuch.setVisibility(View.INVISIBLE)

        binding.fatNormalMin.text = "%.1f".format(boundary1) + "%"
        binding.fatNormalMax.text = "%.1f".format(boundary2) + "%"

        val display_fat = ((fat * 10.0).roundToInt() / 10.0).toFloat()

        if(display_fat < boundary1) {
            binding.fatLess.setVisibility(View.VISIBLE)
            binding.fatDisplayMin.text = "%.1f".format(display_fat) + "%"
        }
        else if(display_fat == boundary1) {
            binding.fatBoundary1.setVisibility(View.VISIBLE)
            binding.fatDisplayBoundary1.text = "%.1f".format(display_fat) + "%"
        }
        else if(display_fat > boundary1 && display_fat < boundary2) {
            binding.fatNormal.setVisibility(View.VISIBLE)
            binding.fatDisplayNormal.text = "%.1f".format(display_fat) + "%"
        }
        else if(display_fat == boundary2) {
            binding.fatBoundary2.setVisibility(View.VISIBLE)
            binding.fatDisplayBoundary2.text = "%.1f".format(display_fat) + "%"
        }
        else {
            binding.fatMuch.setVisibility(View.VISIBLE)
            binding.fatDisplayMuch.text = "%.1f".format(display_fat) + "%"
        }
    }

    fun muscle_display(binding: LayoutRegisterTargetBinding, muscle : Float, boundary1 : Float, boundary2 : Float) {
        binding.muscleLess.setVisibility(View.INVISIBLE)
        binding.muscleBoundary1.setVisibility(View.INVISIBLE)
        binding.muscleNormal.setVisibility(View.INVISIBLE)
        binding.muscleBoundary2.setVisibility(View.INVISIBLE)
        binding.muscleMuch.setVisibility(View.INVISIBLE)

        binding.muscleNormalMin.text = "%.1f".format(boundary1) + "kg"
        binding.muscleNormalMax.text = "%.1f".format(boundary2) + "kg"

        val display_muscle = ((muscle * 10.0).roundToInt() / 10.0).toFloat()

        if(display_muscle < boundary1) {
            binding.muscleLess.setVisibility(View.VISIBLE)
            binding.muscleDisplayMin.text = "%.1f".format(display_muscle) + "kg"
        }
        else if(display_muscle == boundary1) {
            binding.muscleBoundary1.setVisibility(View.VISIBLE)
            binding.muscleDisplayBoundary1.text = "%.1f".format(display_muscle) + "kg"
        }
        else if(display_muscle > boundary1 && display_muscle < boundary2) {
            binding.muscleNormal.setVisibility(View.VISIBLE)
            binding.muscleDisplayNormal.text = "%.1f".format(display_muscle) + "kg"
        }
        else if(display_muscle == boundary2) {
            binding.muscleBoundary2.setVisibility(View.VISIBLE)
            binding.muscleDisplayBoundary2.text = "%.1f".format(display_muscle) + "kg"
        }
        else {
            binding.muscleMuch.setVisibility(View.VISIBLE)
            binding.muscleDisplayMuch.text = "%.1f".format(display_muscle) + "kg"
        }
    }
}