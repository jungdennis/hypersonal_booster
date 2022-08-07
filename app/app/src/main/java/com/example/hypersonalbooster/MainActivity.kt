package com.example.hypersonalbooster

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutMainFrameBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding : LayoutMainFrameBinding

    val mainFragment : MainFragment = MainFragment();

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutMainFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val main_fragmentTransaction = supportFragmentManager.beginTransaction()
        main_fragmentTransaction.replace(R.id.main_frame, MainFragment())
        main_fragmentTransaction.commit()

        binding.location.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_frame, Fragment2())
            fragmentTransaction.commit()
        }
        binding.supply.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_frame, Fragment3())
            fragmentTransaction.commit()
        }
        binding.qr.setOnClickListener {
            FirebaseAuth.getInstance().getCurrentUser()?.delete()        // 회원 탈퇴
            finishAffinity()
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
