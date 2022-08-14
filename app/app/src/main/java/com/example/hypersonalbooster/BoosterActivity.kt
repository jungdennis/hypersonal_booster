package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutBoosterMainBinding
import android.widget.Button
import android.widget.Toast
import com.example.hypersonalbooster.databinding.LayoutBoosterAfterBinding
import com.example.hypersonalbooster.databinding.LayoutBoosterBeforeBinding

private var end_time: Long = 0

class BoosterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_booster_main)
        settingButon()
        settingButton2()
    }

<<<<<<< Updated upstream
        binding = LayoutBoosterMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shared = getSharedPreferences("data_cloud", 0)

        var nickname = shared.getString("nickname", "닉네임없음")
        binding.userName.text = nickname.toString()

        binding.moreBefore.setOnClickListener{
            val intent = Intent(this, BoosterActivity_Before::class.java)
=======
    fun settingButon(){
        val button = findViewById<Button>(R.id.more_before)
        button.setOnClickListener{
            val intent = Intent(this, LayoutBoosterBeforeBinding :: class.java)
>>>>>>> Stashed changes
            startActivity(intent)
        }
    }
    fun settingButton2(){
        val button = findViewById<Button>(R.id.more_after)
        button.setOnClickListener{
            val intent = Intent(this, LayoutBoosterAfterBinding :: class.java)
            startActivity(intent)
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