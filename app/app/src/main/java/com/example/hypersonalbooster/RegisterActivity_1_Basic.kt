package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterBasicBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity_1_Basic : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterBasicBinding
    private lateinit var input_age : String
    private lateinit var input_sex : String
    private var input_pragent : String = ""

    var check_age : Int = 0
    var check_sex : Int = 0
    var check_pragent : Int = 0

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("members")

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
            input_pragent = "false"

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

            input_pragent = "true"

            check_pragent = 1
        }
        binding.pragentNo.setOnClickListener {
            binding.pragentYes.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.pragentNo.setBackgroundResource(R.drawable.btn_main_color)

            input_pragent = "false"

            check_pragent = 1
        }

        binding.confirm.setOnClickListener {
            if(binding.insertAge.text.isNotEmpty()) {
                input_age = binding.insertAge.text.toString()
                check_age = 1
            }

            if(check_age * check_pragent * check_sex != 1) {
                Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                val shared = getSharedPreferences("data_cloud", 0)
                val editor = shared.edit()
                editor.putInt("age", input_age.toInt())
                editor.putString("sex", input_sex)
                editor.putString("pragent", input_pragent)
                editor.apply()

                val uid = shared.getString("uid", "NoUid")
                var uid_check : Boolean = false
                if(uid == "NoUid") {
                    Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    uid_check = false
                }
                else {
                    val save = ref.child(uid!!).child("Info_Basic")
                    save.child("age").setValue(input_age.toInt())
                    save.child("sex").setValue(input_sex)
                    save.child("pragent").setValue(input_pragent)

                    uid_check = true
                }

                if(uid_check) {
                    Toast.makeText(this, "$input_age / $input_sex / $input_pragent", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, RegisterActivity_2_Health::class.java)
                    startActivity(intent)
                }
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