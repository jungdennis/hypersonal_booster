package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.hypersonalbooster.databinding.LayoutRegisterParticularBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity_9_Particular : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterParticularBinding

    private var input_vegan : String = ""
    private var input_milk : String = ""
    private var input_caffeine : String = ""

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("members")

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

            input_vegan = "true"
        }
        binding.veganNo.setOnClickListener {
            binding.veganNo.setBackgroundResource(R.drawable.btn_main_color)
            binding.veganYes.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_vegan = "false"
        }

        binding.milkYes.setOnClickListener {
            binding.milkYes.setBackgroundResource(R.drawable.btn_main_color)
            binding.milkNo.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_milk = "true"
        }
        binding.milkNo.setOnClickListener {
            binding.milkNo.setBackgroundResource(R.drawable.btn_main_color)
            binding.milkYes.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_milk = "false"
        }

        binding.caffeineYes.setOnClickListener {
            binding.caffeineYes.setBackgroundResource(R.drawable.btn_main_color)
            binding.caffeineNo.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_caffeine = "true"
        }
        binding.caffeineNo.setOnClickListener {
            binding.caffeineNo.setBackgroundResource(R.drawable.btn_main_color)
            binding.caffeineYes.setBackgroundResource(R.drawable.btn_sub_color_light)

            input_caffeine = "false"
        }

        binding.confirm.setOnClickListener {
            if(input_vegan.isEmpty() || input_caffeine.isEmpty() || input_milk.isEmpty()) {
                Toast.makeText(this, "모든 질문에 답해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                val shared = getSharedPreferences("data_cloud", 0)
                val editor = shared.edit()

                editor.putString("vegan", input_vegan)
                editor.putString("milk", input_milk)
                editor.putString("caffeine", input_caffeine)
                editor.apply()

                val uid = shared.getString("uid", "NoUid")
                var uid_check : Boolean = false
                if(uid == "NoUid") {
                    Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    uid_check = false
                }
                else {
                    val save = ref.child(uid!!)
                    save.child("Info_Particular").child("vegan").setValue(input_vegan)
                    save.child("Info_Particular").child("milk").setValue(input_milk)
                    save.child("Info_Particular").child("caffeine").setValue(input_caffeine)

                    save.child("cloud_check").setValue("true")

                    uid_check = true
                }

                if(uid_check) {
                    Toast.makeText(this, "$input_vegan / $input_milk / $input_caffeine", Toast.LENGTH_SHORT).show()

                    val intent_next = Intent(this, MainActivity::class.java)
                    startActivity(intent_next)
                }
            }
        }
    }
}