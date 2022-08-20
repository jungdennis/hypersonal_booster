package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.hypersonalbooster.databinding.LayoutMainBinding
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    private lateinit var binding : LayoutMainBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val shared_health = getSharedPreferences("data_health", 0)
        var height = shared_health.getFloat("height", 0F)
        var weight = shared_health.getFloat("weight", 0F)
        var fat = shared_health.getFloat("fat", 0F)
        var muscle = shared_health.getFloat("muscle", 0F)

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        var uid = shared_cloud.getString("uid", "NoUid")
        var name = shared_cloud.getString("name", "닉네임없음")

        var bmi : Float = weight / ((height / 100) * (height / 100))


        binding.displayBmi.text = "%.1f".format(bmi)
        binding.weightDisplay.text = weight.toString()
        binding.displayFat.text = fat.toString()
        binding.displayMuscle.text = muscle.toString()
        binding.userName.text = name

        if(fat <= 0 || muscle <= 0) {
            binding.frameFat.setVisibility(View.INVISIBLE)
            binding.infoFat.setVisibility(View.INVISIBLE)
            binding.frameMuscle.setVisibility(View.INVISIBLE)
            binding.infoMuscle.setVisibility(View.INVISIBLE)
            binding.messageNoFatMuscle.setVisibility(View.VISIBLE)
        }
        else {
            binding.frameFat.setVisibility(View.VISIBLE)
            binding.infoFat.setVisibility(View.VISIBLE)
            binding.frameMuscle.setVisibility(View.VISIBLE)
            binding.infoMuscle.setVisibility(View.VISIBLE)
            binding.messageNoFatMuscle.setVisibility(View.INVISIBLE)
        }

        binding.location.setOnClickListener {
            val map_intent = Intent(this, KioskActivity::class.java)
            map_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(map_intent)
        }
        binding.supply.setOnClickListener {
            val supply_intent = Intent(this, BoosterActivity::class.java)
            supply_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(supply_intent)
        }
        binding.qr.setOnClickListener {
            val qr_popup = MainFragment_QR()
            qr_popup.show(supportFragmentManager, qr_popup.tag)
        }
        binding.plusMenu.setOnClickListener {
            binding.mainDrawerLayout.openDrawer(GravityCompat.END)
        }

        binding.close.setOnClickListener {
            binding.mainDrawerLayout.closeDrawer(GravityCompat.END)
        }

        // 키오스크 테스트용 코드 (나중에 지울 것!)
        if(uid == "NoUid") {
            Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            val save_before = ref.child("apptest").child(uid!!).child("Booster_before")
            save_before.child("bp1").setValue("CLB0111-04,20")
            save_before.child("bp2").setValue("CLB0111-03,32")
            save_before.child("bp3").setValue("CLB0111-03,33")

            val save_after = ref.child("apptest").child(uid).child("Booster_after")
            save_after.child("ap1").setValue("CLB0111-04,41")
            save_after.child("ap2").setValue("CLB0111-03,50")
            save_after.child("ap3").setValue("CLB0111-03,60")
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed()

        if (System.currentTimeMillis() - end_time >= 2000) {
            end_time = System.currentTimeMillis()
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        else if (System.currentTimeMillis() - end_time < 2000) {
            finishAffinity()
        }
    }
}
