package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterInbodyBinding


class RegisterActivity_3_Inbody : AppCompatActivity() {

    private lateinit var binding: LayoutRegisterInbodyBinding

    lateinit var input_fat : String
    lateinit var input_muscle : String

    var intent_flag : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterInbodyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirm.setVisibility(View.INVISIBLE)

        binding.back.setOnClickListener {
            finish()
        }
        binding.yes.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_frame, RegisterFragment_3_Inbody_Yes())
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
            fragmentTransaction.replace(R.id.fragment_frame, RegisterFragment_3_Inbody_No())
            fragmentTransaction.commit()

            input_fat = "0"
            input_muscle = "0"
            intent_flag = false

            binding.confirm.setVisibility(View.VISIBLE)
            binding.yes.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.no.setBackgroundResource(R.drawable.btn_main_color)
        }
        binding.confirm.setOnClickListener {
            if(input_fat != "0" && input_muscle != "0") {
                val frag_yes : RegisterFragment_3_Inbody_Yes =
                    supportFragmentManager.findFragmentById(R.id.fragment_frame) as RegisterFragment_3_Inbody_Yes

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

            if((check_fat < 0) || (check_muscle < 0)) {
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                val shared = getSharedPreferences("data_health", 0)
                val editor = shared.edit()

                editor.putFloat("fat", check_fat)
                editor.putFloat("muscle", check_muscle)
                editor.apply()

                if(intent_flag){
                    val intent = Intent(this, RegisterActivity_4_Target::class.java)
                    startActivity(intent)
                }
                else{
                    val intent = Intent(this, RegisterActivity_4_Target_NoInbody::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
