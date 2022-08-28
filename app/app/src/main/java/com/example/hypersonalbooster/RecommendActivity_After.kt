package com.example.hypersonalbooster

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRecommendAfterBinding
import com.google.firebase.database.*

class RecommendActivity_After() : AppCompatActivity() {

    private lateinit var binding : LayoutRecommendAfterBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster")

    // basic information
    var age : Int = 0
    var sex : String = ""
    var pragent : String = ""

    // health information
    var now_height : Float = 0.0F
    var now_weight : Float = 0.0F
    var now_fat : Float = 0.0F
    var now_muscle : Float = 0.0F

    // normal information
    var normal_weight : Float = 0.0F
    var normal_muscle : Float = 0.0F
    var normal_target_muscle : Float = 0.0F
    var normal_fat : Float = 0.0F

    // target information
    var target_weight : Float = 0.0F
    var target_fat : Float = 0.0F
    var target_muscle : Float = 0.0F

    // favorite information
    var feeling : String = ""
    // val taste = ArrayList<String>()
    // val company = ArrayList<String>()

    // Particular information
    var milk : String = ""
    var vegan : String = ""
    var caffeine : String = ""

    // booster kind
    var kind_before : String = ""
    var kind_after : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRecommendAfterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // 데이터 불러오기
        val shared_health = getSharedPreferences("data_health", 0)
        val shared_cloud = getSharedPreferences("data_cloud", 0)

        age = shared_cloud.getInt("age", 0)
        sex = shared_cloud.getString("sex", "None").toString()
        pragent = shared_cloud.getString("pragent", "Nothing").toString()

        now_height = shared_health.getFloat("height", 0.0F)
        now_weight = shared_health.getFloat("weight", 0.0F)

        if(sex == "man") {
            normal_weight = (now_height / 100) * (now_height / 100) * 22
            normal_muscle = (now_weight * 0.45).toFloat()
            normal_target_muscle = (normal_weight * 0.45).toFloat()

            if(age < 40) {
                normal_fat = 16.0F
            }
            else if(age < 60) {
                normal_fat = 17.0F
            }
            else {
                normal_fat = 20.0F
            }
        }
        else if(sex == "woman") {
            normal_weight = (now_height / 100) * (now_height / 100) * 21
            normal_muscle = (now_weight * 0.36).toFloat()
            normal_target_muscle = (normal_weight * 0.36).toFloat()

            if(age < 18) {
                normal_fat = 26.5F
            }
            else if(age < 40) {
                normal_fat = 27.5F
            }
            else if(age < 60) {
                normal_fat = 28.5F
            }
            else {
                normal_fat = 29.5F
            }
        }

        val temp_fat = shared_health.getFloat("fat", 0.0F)
        if(temp_fat != 0.0F) {
            now_fat = temp_fat
        }
        else {
            now_fat = normal_fat
        }
        val temp_muscle = shared_health.getFloat("muscle", 0.0F)
        if(temp_muscle != 0.0F) {
            now_muscle = temp_muscle
        }
        else {
            now_muscle = normal_muscle
        }

        val temp_target_weight = shared_health.getFloat("target_weight", 0.0F)
        if(temp_target_weight != 0.0F) {
            target_weight = temp_target_weight
        }
        else {
            target_weight = normal_weight
        }
        val temp_target_fat = shared_health.getFloat("target_fat", 0.0F)
        if(temp_target_fat != 0.0F) {
            target_fat = temp_target_fat
        }
        else {
            target_fat = normal_fat
        }
        val temp_target_muscle = shared_health.getFloat("target_muscle", 0.0F)
        if(temp_muscle == 0.0F){
            target_muscle = normal_muscle
        }
        else {
            if(temp_target_muscle != 0.0F) {
                target_muscle = temp_target_muscle
            }
            else {
                target_muscle = normal_target_muscle
            }
        }

        feeling = shared_cloud.getString("feeling", "NoData").toString()
        val taste = shared_cloud.getString("taste", "NoData")?.split(",")?.distinct()
        val company = shared_cloud.getString("comapny", "NoData")?.toString()?.split(",")

        milk = shared_cloud.getString("milk", "NoData").toString()
        caffeine = shared_cloud.getString("caffeine", "NoData").toString()
        vegan = shared_cloud.getString("vegan", "NoData").toString()

        // 운동 후 종류 지정
        if(now_weight < target_weight) {
            kind_after = "gainer"
        }
        else {
            if(vegan == "true") {
                kind_after = "vegan"
            }
            else if(milk == "true") {
                kind_after = "anti_milk"
            }
            else {
                kind_after = "normal"
            }
        }
        Toast.makeText(this, "After : $kind_after",Toast.LENGTH_SHORT).show()

        var query_1 : Query = ref

        if(feeling == "clean") {
            query_1 = ref.orderByChild("texture").equalTo("clear")
        }
        else if(feeling == "milky") {
            query_1 = ref.orderByChild("texture").equalTo("thick")
        }

        /*
        query_1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot) {

                }
            }

                override fun onCancelled(databaseError: DatabaseError) {}})

         */

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        // super.onBackPressed()
    }
}