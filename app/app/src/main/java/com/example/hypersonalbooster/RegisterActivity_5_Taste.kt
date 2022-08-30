package com.example.hypersonalbooster

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterTasteBinding
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity_5_Taste : AppCompatActivity(), OnTasteClickListener {

    private lateinit var binding : LayoutRegisterTasteBinding

    var taste_list = ArrayList<Taste>()
    var list = ArrayList<Taste>()

    private lateinit var adapter : ListViewAdapter_Taste

    var check_taste = ArrayList<String>()

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("members")

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterTasteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val taste_string = getSharedPreferences("data_booster", 0).getString("taste", "failed")
        val taste_temp = taste_string!!.split(",").distinct() as ArrayList<String>
        for(taste in taste_temp) {
            taste_list.add(Taste(taste))
        }

        list.addAll(taste_list)

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
            finish()
        }
        binding.back.setOnClickListener {
            finish()
        }

        binding.tasteSearch.setOnQueryTextListener(searchViewTextListener)

        adapter = ListViewAdapter_Taste(this, list, this)
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
                val save = ref.child(uid!!).child("Info_Favorite")
                save.child("taste").setValue(input_taste)

                uid_check = true
            }

            if(uid_check){
                val intent = Intent(this, RegisterActivity_6_Company::class.java)
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
                    val save = ref.child(uid!!).child("Info_Favorite")
                    save.child("taste").setValue(input_taste)

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
                        val intent = Intent(this, RegisterActivity_6_Company::class.java)
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
            list.addAll(taste_list)
        } else {
            for (i in 0 until taste_list.size) {
                if (taste_list.get(i).name.toLowerCase().contains(charText)) {
                    list.add(taste_list.get(i))
                }
            }
        }

        adapter.notifyDataSetChanged()
    }

    override fun onTasteClickAdd(taste_name : String) {
        check_taste.add(taste_name)
    }
    override fun onTasteClickRemove(taste_name: String) {
        check_taste.remove(taste_name)
    }
}