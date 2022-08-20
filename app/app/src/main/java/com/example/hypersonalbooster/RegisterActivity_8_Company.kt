package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hypersonalbooster.databinding.LayoutRegisterCompanyBinding
import com.example.hypersonalbooster.databinding.LayoutRegisterCompanyRecyclerBinding
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity_8_Company : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterCompanyRecyclerBinding

    var company_list = ArrayList<String>()

    private lateinit var adapter : CompanyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterCompanyRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared = getSharedPreferences("data_booster", 0)
        val company_string = shared.getString("company", "failed")
        val company_list = company_string!!.split(",")
        Toast.makeText(this, "$company_list", Toast.LENGTH_SHORT).show()

        binding.back.setOnClickListener {
            finish()
        }

        binding.noCompany.setOnClickListener {
            val intent = Intent(this, RegisterActivity_9_Particular::class.java)
            startActivity(intent)
        }
        binding.confirm.setOnClickListener {
            val intent = Intent(this, RegisterActivity_9_Particular::class.java)
            startActivity(intent)
        }

        /*
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
        */

        adapter = CompanyRecyclerViewAdapter(company_list as ArrayList<String>)
        binding.companySelect.adapter = adapter
        binding.companySelect.layoutManager = LinearLayoutManager(this)


    }
}