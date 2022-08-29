package com.example.hypersonalbooster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_welcome)

        getSharedPreferences("auto_login", 0).edit().putString("auto_login", "true").apply()

        var handler = Handler()
        handler.postDelayed( {
            var intent = Intent( this, CloudLoadingActivity_Booster::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, 2000)
    }
}