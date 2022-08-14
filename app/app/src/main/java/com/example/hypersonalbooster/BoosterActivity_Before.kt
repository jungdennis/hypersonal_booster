package com.example.hypersonalbooster

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import com.example.hypersonalbooster.databinding.LayoutBoosterMainBinding
import android.widget.Button
import android.widget.Toast
import com.example.hypersonalbooster.databinding.LayoutBoosterAfterBinding
import com.example.hypersonalbooster.databinding.LayoutBoosterBeforeBinding


class BoosterActivity_Before : AppCompatActivity() {

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_booster_before)

        settingButton()
        settingButton2()
        settingButton3()
        settingButton4()


}
    fun settingButton(){
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val intent = Intent(this, LayoutBoosterBeforeBinding :: class.java)
            startActivity(intent)
        }
    }
    fun settingButton2(){
        val button = findViewById<Button>(R.id.button2)
        button.setOnClickListener{
            val intent = Intent(this, LayoutBoosterAfterBinding :: class.java)
            startActivity(intent)
        }
    }
    fun settingButton3(){
        val button = findViewById<Button>(R.id.button3)
        button.setOnClickListener{
            val intent = Intent(this, LayoutBoosterAfterBinding :: class.java)
            startActivity(intent)
        }
    }
    fun settingButton4(){
        val button = findViewById<Button>(R.id.button4)
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
