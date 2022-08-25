package com.example.hypersonalbooster
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutBoosterMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BoosterActivity : AppCompatActivity() {

    private lateinit var binding : LayoutBoosterMainBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")


    private var end_time: Long = 0

    var booster_before = ArrayList<Booster>()
    var booster_after = ArrayList<Booster>()
    var booster_init = ArrayList<Booster>()


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = LayoutBoosterMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val name = shared_cloud.getString("name", "닉네임없음")
        val before = shared_cloud.getString("booster_before", "NoBooster")!!.split(",")
        val after = shared_cloud.getString("booster_after", "NoBooster")!!.split(",")
        val uid = shared_cloud.getString("uid", "NoUid")

        binding.userName.text = name

        setContentView(binding.root)

        for(boosterID in before) {
            booster_before.add(Booster(boosterID))
        }
        for(boosterID in after) {
            booster_after.add(Booster(boosterID))
        }
        for(boosterID in before) {
            booster_init.add(Booster(boosterID))
        }

        val mlAdapter = ListViewAdapter_Main(this, booster_before)

        binding.boosterListBefore.adapter = mlAdapter

        val mlAdapter2 = ListViewAdapter_Main(this, booster_after)

        binding.boosterListAfter.adapter = mlAdapter2


        binding.moreBefore.setOnClickListener {
            val intent = Intent(this, BoosterActivity_Before :: class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        binding.moreAfter.setOnClickListener {
            val intent = Intent(this, BoosterActivity_After :: class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            overridePendingTransition(0, 0)
            finish()
        }
        binding.location.setOnClickListener {
            val location_intent = Intent(this, KioskActivity::class.java)
            location_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(location_intent)
        }
        binding.qr.setOnClickListener {
            val qr_popup = MainFragment_QR()
            qr_popup.show(supportFragmentManager, qr_popup.tag)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(0, 0)
        finish()
    }
}