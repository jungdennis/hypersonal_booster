package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterFeelingBinding
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity_4_Feeling : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterFeelingBinding
    private var input_feeling : String = ""

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("members")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterFeelingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared = getSharedPreferences("data_cloud", 0)
        val editor = shared.edit()


        val cloud_check = shared.getString("cloud_check", "Nothing")
        if(cloud_check =="true") {
            binding.close.setVisibility(View.VISIBLE)
            binding.back.setVisibility(View.INVISIBLE)
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

        binding.milky.setOnClickListener {
            binding.milky.setBackgroundResource(R.drawable.btn_main_color)
            binding.clean.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.anything.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_feeling = "milky"
        }
        binding.clean.setOnClickListener {
            binding.milky.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.clean.setBackgroundResource(R.drawable.btn_main_color)
            binding.anything.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_feeling = "clean"
        }
        binding.anything.setOnClickListener {
            binding.milky.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.clean.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.anything.setBackgroundResource(R.drawable.btn_main_color)

            input_feeling = "anything"
        }

        binding.confirm.setOnClickListener {
            if(input_feeling.isEmpty()) {
                Toast.makeText(this, "원하시는 느낌을 하나 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                editor.putString("Feeling", input_feeling)
                editor.apply()

                val uid = shared.getString("uid", "NoUid")
                var uid_check : Boolean = false
                if(uid == "NoUid") {
                    Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    uid_check = false
                }
                else {
                    val save = ref.child(uid!!).child("Info_Favorite")
                    save.child("feeling").setValue(input_feeling)

                    uid_check = true
                }

                if(uid_check) {
                    if(cloud_check == "true") {
                        val intent = Intent(this, RecommendActivity_Before::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        val intent = Intent(this, RegisterActivity_5_Taste::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}