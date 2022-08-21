package com.example.hypersonalbooster

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hypersonalbooster.databinding.LayoutRegisterTasteBinding
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity_7_Taste : AppCompatActivity(), OnTasteClickListener {

    private lateinit var binding : LayoutRegisterTasteBinding

    var taste_list = ArrayList<String>()

    private lateinit var adapter : RecyclerViewAdapter_Taste

    var check_taste = ArrayList<String>()

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterTasteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taste_string = getSharedPreferences("data_booster", 0).getString("taste", "failed")
        taste_list = taste_string!!.split(",") as ArrayList<String>

        val editor = getSharedPreferences("data_cloud", 0).edit()

        binding.back.setOnClickListener {
            finish()
        }

        binding.tasteSearch.setOnQueryTextListener(searchViewTextListener)

        binding.tasteSelect.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter_Taste(taste_list, this)
        binding.tasteSelect.setAdapter(adapter)

        binding.noTaste.setOnClickListener {
            var input_taste = "Nothing"

            editor.putString("taste", input_taste)
            editor.apply()

            val uid = getSharedPreferences("data_cloud", 0).getString("uid", "NoUid")
            var uid_check : Boolean = false
            if(uid == "NoUid") {
                Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                uid_check = false
            }
            else {
                val save = ref.child("apptest").child(uid!!).child("Info_Favorite")
                save.child("taste").setValue(input_taste)

                uid_check = true
            }

            if(uid_check){
                val intent = Intent(this, RegisterActivity_8_Company::class.java)
                startActivity(intent)
            }
        }
        binding.confirm.setOnClickListener {
            if(check_taste.isEmpty()){
                Toast.makeText(this, "하나 이상 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                var input_taste : String = ""
                for(taste in check_taste) {
                    if(input_taste.isEmpty()) {
                        input_taste = input_taste + taste
                    }
                    else {
                        input_taste = input_taste + "," + taste
                    }
                }

                editor.putString("taste", input_taste)
                editor.apply()

                val uid = getSharedPreferences("data_cloud", 0).getString("uid", "NoUid")
                var uid_check : Boolean = false
                if(uid == "NoUid") {
                    Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    uid_check = false
                }
                else {
                    val save = ref.child("apptest").child(uid!!).child("Info_Favorite")
                    save.child("taste").setValue(input_taste)

                    uid_check = true
                }

                if(uid_check){
                    val intent = Intent(this, RegisterActivity_8_Company::class.java)
                    startActivity(intent)
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
                adapter.filter.filter(s)
                Log.d("RegisterActivity_8_Taste", "SearchView Text is Changed : $s")
                return false
            }
        }

    override fun onTasteClickAdd(taste_name : String) {
        check_taste.add(taste_name)
    }
    override fun onTasteClickRemove(taste_name: String) {
        check_taste.remove(taste_name)
    }
}