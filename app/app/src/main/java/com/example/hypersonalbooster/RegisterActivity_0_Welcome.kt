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

class RegisterActivity_0_Welcome : AppCompatActivity() {

    private lateinit var binding : LayoutRegisterWelcomeBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref_booster = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")
    val ref_members = database.getReference("members")




    // 어플리케이션 테스트용 (나중에 BoosterRecommend로 넘어갈 예정)
    var booster_before = ""
    var booster_after = ""

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRegisterWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val editor_cloud = shared_cloud.edit()

        val uid = shared_cloud.getString("uid", "Nouid")
        Log.d("RegisterActivity_0", "uid : $uid")
        if(uid == "Nouid") {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            val health_check = getSharedPreferences("data_health", 0).getString("health_check", "Nothing")
            val cloud_check = shared_cloud.getString("cloud_check", "Nothing")
            Log.d("RegisterActivity_0", "health_check, cloud_check : $health_check, $cloud_check")

            if(health_check == "true" && cloud_check == "true") {
                val intent = Intent(this, RecommendActivity_Before::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.start.setOnClickListener {
            val intent = Intent(this, RegisterActivity_1_Basic::class.java)
            startActivity(intent)
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