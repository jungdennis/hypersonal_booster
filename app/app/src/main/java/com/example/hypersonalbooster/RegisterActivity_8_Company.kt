package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hypersonalbooster.databinding.LayoutRegisterCompanyRecyclerBinding
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity_8_Company : AppCompatActivity(), OnCompanyClickListener {

    private lateinit var binding : LayoutRegisterCompanyRecyclerBinding

    var company_list = ArrayList<String>()

    private lateinit var adapter : CompanyRecyclerViewAdapter

    var check_company = ArrayList<String>()

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterCompanyRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val company_string = getSharedPreferences("data_booster", 0).getString("company", "failed")
        val company_list = company_string!!.split(",")

        val editor = getSharedPreferences("data_cloud", 0).edit()

        binding.back.setOnClickListener {
            finish()
        }

        binding.noCompany.setOnClickListener {
            var input_company = "Nothing"

            editor.putString("company", input_company)
            editor.apply()

            val uid = getSharedPreferences("data_cloud", 0).getString("uid", "NoUid")
            var uid_check : Boolean = false
            if(uid == "NoUid") {
                Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                uid_check = false
            }
            else {
                val save = ref.child("apptest").child(uid!!).child("Info_Favorite")
                save.child("company").setValue(input_company)

                uid_check = true
            }

            if(uid_check){
                val intent = Intent(this, RegisterActivity_9_Particular::class.java)
                startActivity(intent)
            }
        }
        binding.confirm.setOnClickListener {
            if(check_company.isEmpty()){
                Toast.makeText(this, "하나 이상 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                var input_company : String = ""
                for(company in check_company) {
                    if(input_company.isEmpty()) {
                        input_company = input_company + company
                    }
                    else {
                        input_company = input_company + "," + company
                    }
                }

                editor.putString("company", input_company)
                editor.apply()

                val uid = getSharedPreferences("data_cloud", 0).getString("uid", "NoUid")
                var uid_check : Boolean = false
                if(uid == "NoUid") {
                    Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    uid_check = false
                }
                else {
                    val save = ref.child("apptest").child(uid!!).child("Info_Favorite")
                    save.child("company").setValue(input_company)

                    uid_check = true
                }

                if(uid_check){
                    val intent = Intent(this, RegisterActivity_9_Particular::class.java)
                    startActivity(intent)
                }
            }

        }

        adapter = CompanyRecyclerViewAdapter(company_list as ArrayList<String>, this)
        binding.companySelect.adapter = adapter
        binding.companySelect.layoutManager = LinearLayoutManager(this)
    }

    override fun onCompanyClickAdd(company_name : String) {
        check_company.add(company_name)
    }
    override fun onCompanyClickRemove(company_name: String) {
        check_company.remove(company_name)
    }
}