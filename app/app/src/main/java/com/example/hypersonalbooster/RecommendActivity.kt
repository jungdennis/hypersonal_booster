package com.example.hypersonalbooster

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRecommendBinding

class RecommendActivity() : AppCompatActivity() {

    private lateinit var binding : LayoutRecommendBinding

    // basic information
    var age : Int = 0
    var sex : String = ""
    var pragent : Boolean = false

    // health information
    var now_height : Float = 0.0F
    var now_weight : Float = 0.0F
    var now_fat : Float = 0.0F
    var now_muscle : Float = 0.0F

    // target information
    var target_weight : Float = 0.0F
    var target_fat : Float = 0.0F
    var target_muscle : Float = 0.0F

    // favorite information
    var feeling : String = ""
    var taste = ArrayList<String>()
    var company = ArrayList<String>()

    // Particular information
    var milk : Boolean = false
    var vegan : Boolean = false
    var caffeine : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRecommendBinding.inflate(layoutInflater)

        setContentView(binding.root)


        val shared_health = getSharedPreferences("data_health", 0)
        val shared_cloud = getSharedPreferences("data_cloud", 0)


    }
}