package com.example.hypersonalbooster

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.FragmentMapRequestSearchBinding
import com.google.firebase.database.FirebaseDatabase


class KioskFragmentDetailSearch : AppCompatActivity() {

    private lateinit var binding: FragmentMapRequestSearchBinding

    private lateinit var adapter : ListViewAdapter_BoostReq



    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)


        binding = FragmentMapRequestSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.request.setOnClickListener {
            Toast.makeText(this, "보충제를 요청하였습니다", Toast.LENGTH_SHORT)
                .show()
        }

        binding.cancel.setOnClickListener {
            finish()
        }

        binding.recommend.setOnClickListener {
            val ReqRecomm_intent = Intent(this, KioskRequestRecommand::class.java)
            ReqRecomm_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(ReqRecomm_intent)
        }

    }
    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(0, 0)
        finish()
    }
}








