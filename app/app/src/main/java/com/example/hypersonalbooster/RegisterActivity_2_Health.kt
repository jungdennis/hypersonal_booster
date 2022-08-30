package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterHealthBinding

class RegisterActivity_2_Health : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterHealthBinding

    lateinit var input_height : String
    lateinit var input_weight : String
    var input_fat : String = "-1"
    var input_muscle : String = "-1"

    var intent_flag : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterHealthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirm.setVisibility(View.INVISIBLE)

        val shared = getSharedPreferences("data_health", 0)
        val editor = shared.edit()

        val health_check = shared.getString("health_check", "Nothing")

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

        binding.yes.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_frame, RegisterFragment_2_Inbody_Yes())
            fragmentTransaction.commit()

            input_fat = "-1"
            input_muscle = "-1"
            intent_flag = true

            binding.confirm.setVisibility(View.VISIBLE)
            binding.yes.setBackgroundResource(R.drawable.btn_main_color)
            binding.no.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        binding.no.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_frame, RegisterFragment_2_Inbody_No())
            fragmentTransaction.commit()

            input_fat = "0"
            input_muscle = "0"
            intent_flag = false

            binding.confirm.setVisibility(View.VISIBLE)
            binding.yes.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.no.setBackgroundResource(R.drawable.btn_main_color)
        }
        binding.confirm.setOnClickListener {
            input_height = binding.insertHeight.text.toString()
            input_weight = binding.insertWeight.text.toString()

            if(input_fat != "0" && input_muscle != "0") {
                val frag_yes : RegisterFragment_2_Inbody_Yes =
                    supportFragmentManager.findFragmentById(R.id.fragment_frame) as RegisterFragment_2_Inbody_Yes

                val frag_fat = frag_yes.binding.insertFat.text.toString()
                val frag_muscle = frag_yes.binding.insertMuscle.text.toString()


                if(frag_fat.isNotEmpty()) {
                    input_fat = frag_fat
                }
                if(frag_muscle.isNotEmpty()) {
                    input_muscle = frag_muscle
                }
            }

            var check_fat = input_fat.toFloat()
            var check_muscle = input_muscle.toFloat()

            if(input_weight.isBlank() || input_height.isBlank() || (check_fat < 0) || (check_muscle < 0)) {
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                editor.putFloat("height", input_height.toFloat())
                editor.putFloat("weight", input_weight.toFloat())
                editor.putFloat("fat", check_fat)
                editor.putFloat("muscle", check_muscle)
                editor.apply()

                if(intent_flag){
                    val intent = Intent(this, RegisterActivity_3_Target::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    val intent = Intent(this, RegisterActivity_3_Target_NoInbody::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}