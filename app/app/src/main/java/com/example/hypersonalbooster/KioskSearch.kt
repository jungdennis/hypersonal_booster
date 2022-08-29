package com.example.hypersonalbooster

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutSearchKioskBinding
import com.google.firebase.database.FirebaseDatabase

class KioskSearch : AppCompatActivity(), OnKiosksClickListener {

    private lateinit var binding : LayoutSearchKioskBinding


    var kiosks_list = ArrayList<Kiosks>()
    var list = ArrayList<Kiosks>()

    private lateinit var adapter : ListViewAdapter_Kiosks

    var mInput_kiosk = ""
    var check_kiosk = ArrayList<String>()

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("kiosk")

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = LayoutSearchKioskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared_selName = getSharedPreferences("data_selectedName", 0)
        val editor_sel = shared_selName.edit()


        val shared_Kname = getSharedPreferences("data_kioskNameData", 0).getString("kioskNameData", "failed")
        Log.d("shared_Kname", "$shared_Kname")
        val kiosks_temp = shared_Kname!!.split("/").distinct() as ArrayList<String>
        Log.d("kiosks_temp", "$kiosks_temp")
        for(kiosk in kiosks_temp) {
            val name = kiosk.toString()
            kiosks_list.add(Kiosks(name))
        }

        /* 소중한 예제
        val booster_test = Booster("MPL3000-01")
        booster_test.name
        booster_test.class_2
        */

        list.addAll(kiosks_list)

        // val selectedKiosk = getSharedPreferences("data_selKiosk", 0)
        // val selKeditor = selectedKiosk.edit()

        binding.back.setOnClickListener {
            finish()
        }

        binding.kioskSearch.setOnQueryTextListener(searchViewTextListener)

        adapter = ListViewAdapter_Kiosks(this, list, this)
        binding.kioskSelect.setAdapter(adapter)

        binding.confirm.setOnClickListener{
            if(check_kiosk.isEmpty()){
                Toast.makeText(this, "키오스크를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else if(check_kiosk.size > 1){
                Toast.makeText(this, "한개만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                mInput_kiosk = check_kiosk[0].toString()

                editor_sel.putString("kiosk",mInput_kiosk)
                editor_sel.apply()
                finish()
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
            list.addAll(kiosks_list)
        } else {
            for (i in 0 until kiosks_list.size) {
                if (kiosks_list.get(i).name.toLowerCase().contains(charText)) {
                    list.add(kiosks_list.get(i))
                }
            }
        }

        adapter.notifyDataSetChanged()
    }

    override fun onKiosksClickAdd(Kiosks_name: String) {
        check_kiosk.add(Kiosks_name)
    }
    override fun onKiosksClickRemove(Kiosks_name: String){
        check_kiosk.remove(Kiosks_name)
    }

}


