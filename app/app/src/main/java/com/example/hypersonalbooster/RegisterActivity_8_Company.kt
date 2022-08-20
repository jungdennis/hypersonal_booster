package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterCompanyBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RegisterActivity_8_Company : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterCompanyBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    var company_list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared = getSharedPreferences("data_booster", 0)
        val company_string = shared.getString("company", "failed")
        val company_list = company_string!!.split(",")
        Toast.makeText(this, "$company_list", Toast.LENGTH_SHORT).show()

        val len = company_list.size
        for(i in 0 until len) {
            when (i) {
                0 -> binding.booster1.text = company_list[0].toString()
                1 -> binding.booster2.text = company_list[1].toString()
                2 -> binding.booster3.text = company_list[2].toString()
                3 -> binding.booster4.text = company_list[3].toString()
                4 -> binding.booster5.text = company_list[4].toString()
            }
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.booster1.setOnClickListener {
            binding.booster1.setBackgroundResource(R.drawable.btn_main_color)
            binding.booster2.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster3.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster4.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster5.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        binding.booster2.setOnClickListener {
            binding.booster1.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster2.setBackgroundResource(R.drawable.btn_main_color)
            binding.booster3.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster4.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster5.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        binding.booster3.setOnClickListener {
            binding.booster1.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster2.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster3.setBackgroundResource(R.drawable.btn_main_color)
            binding.booster4.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster5.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        binding.booster4.setOnClickListener {
            binding.booster1.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster2.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster3.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster4.setBackgroundResource(R.drawable.btn_main_color)
            binding.booster5.setBackgroundResource(R.drawable.btn_sub_color_light)
        }
        binding.booster5.setOnClickListener {
            binding.booster1.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster2.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster3.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster4.setBackgroundResource(R.drawable.btn_sub_color_light)
            binding.booster5.setBackgroundResource(R.drawable.btn_main_color)
        }

        binding.noCompany.setOnClickListener {
            val intent = Intent(this, RegisterActivity_9_Particular::class.java)
            startActivity(intent)
        }
        binding.confirm.setOnClickListener {
            val intent = Intent(this, RegisterActivity_9_Particular::class.java)
            startActivity(intent)
        }
    }
}