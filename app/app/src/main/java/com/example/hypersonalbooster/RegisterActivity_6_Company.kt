package com.example.hypersonalbooster

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterCompanyBinding
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity_6_Company : AppCompatActivity(), OnCompanyClickListener {

    private lateinit var binding : LayoutRegisterCompanyBinding

    var company_list = ArrayList<Company>()
    var list = ArrayList<Company>()

    private lateinit var adapter : ListViewAdapter_Company

    var check_company = ArrayList<String>()

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("members")

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val company_string = getSharedPreferences("data_booster", 0).getString("company", "failed")
        val company_temp = company_string!!.split(",").distinct() as ArrayList<String>
        for(company in company_temp) {
            company_list.add(Company(company))
        }

        list.addAll(company_list)

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
        binding.back.setOnClickListener {
            finish()
        }
        binding.close.setOnClickListener {
            finish()
        }

        binding.companySearch.setOnQueryTextListener(searchViewTextListener)

        adapter = ListViewAdapter_Company(this, list, this)
        binding.companySelect.setAdapter(adapter)

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
                val save = ref.child(uid!!).child("Info_Favorite")
                save.child("company").setValue(input_company)

                uid_check = true
            }

            if(uid_check){
                val intent = Intent(this, RegisterActivity_7_Particular::class.java)
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
                    val save = ref.child(uid!!).child("Info_Favorite")
                    save.child("company").setValue(input_company)

                    uid_check = true
                }

                if(uid_check){
                    if(cloud_check == "true") {
                        val shared_flag = getSharedPreferences("data_cloud", 0).edit()
                        shared_flag.remove("flag_before").apply()
                        shared_flag.remove("flag_after").apply()

                        val intent = Intent(this, RecommendActivity_Before::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        val intent = Intent(this, RegisterActivity_7_Particular::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    var searchViewTextListener: SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            //텍스트 입력/수정시에 호출
            override fun onQueryTextChange(s: String): Boolean {
                search(s)
                return false
            }
        }

    private fun search(charText: String) {
        list.clear()

        if (charText.length == 0) {
            list.addAll(company_list)
        } else {
            for (i in 0 until company_list.size) {
                if (company_list.get(i).name.toLowerCase().contains(charText)) {
                    list.add(company_list.get(i))
                }
            }
        }

        adapter.notifyDataSetChanged()
    }

    override fun onCompanyClickAdd(company_name : String) {
        check_company.add(company_name)
    }
    override fun onCompanyClickRemove(company_name: String) {
        check_company.remove(company_name)
    }
}