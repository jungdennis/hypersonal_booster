package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRegisterWelcomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates

class RegisterActivity_0_Welcome : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterWelcomeBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster")

    private var end_time: Long = 0

    var company_list = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            val intent = Intent(this, RegisterActivity_1_Basic::class.java)
            startActivity(intent)
        }

        Thread{
            var data_length : Long = 0

            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    data_length = dataSnapshot.childrenCount
                    for (snapshot in dataSnapshot.getChildren()) {
                        val brand = snapshot.child("brand").getValue().toString()
                        if(brand.isNotEmpty()) {
                            company_list.add(brand)
                            Log.d("RegisterActiviy_0_Welcome", "found : " + snapshot.child("brand").getValue() + " / brand : " + brand)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }.run()

        // do{ } while(company_list.size.toLong() != data_length)

        val print = company_list.size
        Log.d("RegisterActiviy_0_Welcome", "length : $print")
    }

    override fun onBackPressed() {
        // super.onBackPressed()

        if (System.currentTimeMillis() - end_time >= 2000) {
            end_time = System.currentTimeMillis()
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            val print = company_list.size
            Log.d("RegisterActiviy_0_Welcome", "length : $print")
        } else if (System.currentTimeMillis() - end_time < 2000) {
            finishAffinity()
        }
    }
}