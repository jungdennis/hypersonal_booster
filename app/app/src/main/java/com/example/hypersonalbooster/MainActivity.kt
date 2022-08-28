package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.hypersonalbooster.databinding.LayoutMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    private lateinit var binding : LayoutMainBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")

    private var end_time: Long = 0

    var booster_before = ArrayList<Booster>()
    var booster_after = ArrayList<Booster>()
    var booster_init = ArrayList<Booster>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val shared_health = getSharedPreferences("data_health", 0)
        val height = shared_health.getFloat("height", 0F)
        val weight = shared_health.getFloat("weight", 0F)
        val fat = shared_health.getFloat("fat", 0F)
        val muscle = shared_health.getFloat("muscle", 0F)

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val uid = shared_cloud.getString("uid", "NoUid")
        val name = shared_cloud.getString("name", "닉네임없음")
        val before = shared_cloud.getString("booster_before", "NoBooster")!!.split(",").distinct()
        val after = shared_cloud.getString("booster_after", "NoBooster")!!.split(",").distinct()

        for(boosterID in before) {
            booster_before.add(Booster(boosterID))
        }
        for(boosterID in after) {
            booster_after.add(Booster(boosterID))
        }
        for(boosterID in before) {
            booster_init.add(Booster(boosterID))
        }

        val bmi : Float = weight / ((height / 100) * (height / 100))

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

        binding.editHealth.setOnClickListener {
            val intent = Intent(this, RegisterActivity_2_Health::class.java)
            startActivity(intent)
            finish()
        }

        val mlAdapter = ListViewAdapter_Main(this, booster_init)
        binding.boosterList.adapter = mlAdapter

        binding.switch2.setOnCheckedChangeListener { CompoundButton, isChecked ->
            if (isChecked) {
                val mlAdapter = ListViewAdapter_Main(this, booster_after)
                binding.boosterList.adapter = mlAdapter
            }
            else {
                val mlAdapter = ListViewAdapter_Main(this, booster_before)
                binding.boosterList.adapter = mlAdapter
            }
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

        binding.basicSetting.setOnClickListener {
            val intent = Intent(this, RegisterActivity_1_Basic::class.java)
            startActivity(intent)
            finish()
        }
        binding.healthSetting.setOnClickListener {
            val intent = Intent(this, RegisterActivity_2_Health::class.java)
            startActivity(intent)
            finish()
        }
        binding.feelingSetting.setOnClickListener {
            val intent = Intent(this, RegisterActivity_4_Feeling::class.java)
            startActivity(intent)
            finish()
        }
        binding.tasteSetting.setOnClickListener {
            val intent = Intent(this, RegisterActivity_5_Taste::class.java)
            startActivity(intent)
            finish()
        }
        binding.companySetting.setOnClickListener {
            val intent = Intent(this, RegisterActivity_6_Company::class.java)
            startActivity(intent)
            finish()
        }
        binding.particularSetting.setOnClickListener {
            val intent = Intent(this, RegisterActivity_7_Particular::class.java)
            startActivity(intent)
            finish()
        }
        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val client = GoogleSignIn.getClient(this, opt)
            client.signOut()
            client.revokeAccess()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.deleteAccount.setOnClickListener {
            // sharedprefence 데이터 삭제
            shared_cloud.edit().clear().apply()
            shared_health.edit().clear().apply()

            // database 데이터 삭제
            val delete_ref = database.getReference("members/" + uid)
            delete_ref.removeValue()

            FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    //로그아웃처리
                    FirebaseAuth.getInstance().signOut()
                    val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    val client = GoogleSignIn.getClient(this, opt)
                    client.signOut()
                    client.revokeAccess()

                    Toast.makeText(this, "탈퇴가 완료되었습니다. 앱을 종료합니다.", Toast.LENGTH_LONG).show()
                    finishAffinity()
                }
                else{
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()

                }
            }
        }

        binding.close.setOnClickListener {
            binding.mainDrawerLayout.closeDrawer(GravityCompat.END)
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
