package com.example.hypersonalbooster

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRecommendAfterBinding
import com.google.firebase.database.*

class RecommendActivity_After() : AppCompatActivity(), CloudCallbackListener {

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
        val taste_list = shared_cloud.getString("taste", "NoData")?.split(",")?.distinct()
        val company_list = shared_cloud.getString("company", "NoData")?.toString()?.split(",")
        Log.d("RecommendActivity_After", "taste_list : $taste_list / company_list : $company_list")

        milk = shared_cloud.getString("milk", "NoData").toString()
        caffeine = shared_cloud.getString("caffeine", "NoData").toString()
        vegan = shared_cloud.getString("vegan", "NoData").toString()

        // 운동 후 종류 지정
        if(now_weight < (normal_weight * 0.9) && now_weight < target_weight) {
            kind_after = "gainer"
        }
        else {
            if(vegan == "true") {
                kind_after = "vegan_protein"            // 비건 제공
            }
            else if(milk == "true") {
                kind_after = "anti_milk_protein"        // 유청분리, 비건 제공
            }
            else {
                kind_after = "normal_protein"           // 모든 종류 제공
            }
        }
        Toast.makeText(this, "After : $kind_after",Toast.LENGTH_SHORT).show()


        if(kind_after == "gainer") {      // 게이너 추천
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var list_taste1 = ArrayList<String>()
                    var sort_taste2 = ArrayList<String>()
                    var sort_taste1 = ArrayList<String>()
                    var sort_company = ArrayList<String>()
                    var result = ArrayList<String>()

                    // Texture로 1차 sort
                    if(feeling == "clean") {
                        for (snapshot in dataSnapshot.getChildren()) {
                            val taste2 = snapshot.child("taste2").getValue().toString().split(",")
                            val taste1 = snapshot.child("taste2").getValue().toString().split(",")

                            for (fav_taste in taste_list!!) {
                                if (fav_taste in taste2) {
                                    for (taste in taste1) {
                                        list_taste1.add(taste)
                                    }
                                    list_taste1.distinct()
                                }
                            }

                            if (snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "0") {
                                if (snapshot.child("texture").getValue().toString() == "clear") {
                                    for (taste in taste_list!!) {
                                        if (taste == "Nothing") {
                                            sort_taste2.add(snapshot.child("ID").getValue().toString())
                                            list_taste1.add(snapshot.child("taste1").getValue().toString())
                                            list_taste1.distinct()
                                        }
                                        else if (taste in taste2) {
                                            sort_taste2.add(snapshot.child("ID").getValue().toString())
                                        }
                                    }

                                    for (company in company_list!!) {
                                        if (company == "Nothing") {
                                            sort_company.add(snapshot.child("ID").getValue().toString())
                                        }
                                        else if (snapshot.child("brand").value.toString().contains(company)) {
                                            sort_company.add(snapshot.child("ID").getValue().toString())
                                        }
                                    }
                                }
                            }
                        }
                        for (taste in sort_taste2) {
                            for (company in sort_company) {
                                if (taste == company) {
                                    result.add(taste)
                                }
                            }
                        }

                        if(result.size < 3) {
                            for(snapshot in dataSnapshot.getChildren()) {
                                if(snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "0") {
                                    if(snapshot.child("texture").getValue().toString() == "clear"){
                                        val taste1 = snapshot.child("taste1").getValue().toString().split(",")

                                        for (taste in list_taste1!!) {
                                            if (taste in taste1) {
                                                sort_taste1.add(snapshot.child("ID").getValue().toString())
                                            }
                                        }
                                    }
                                }
                            }
                            for (taste in sort_taste1) {
                                for (company in sort_company) {
                                    if ((taste == company) && (taste !in result)) {
                                        result.add(taste)
                                    }
                                }
                            }
                        }

                        if(result.size < 3) {
                            for(taste in sort_taste2) {
                                if(taste !in result) {
                                    result.add(taste)
                                }
                            }
                        }

                        if(result.size < 3) {
                            for(taste in sort_taste1) {
                                if(taste !in result) {
                                    result.add(taste)
                                }
                            }
                        }
                    }
                    else {
                        for (snapshot in dataSnapshot.getChildren()) {
                            val taste2 = snapshot.child("taste2").getValue().toString().split(",")
                            val taste1 = snapshot.child("taste2").getValue().toString().split(",")

                            for (fav_taste in taste_list!!) {
                                if (fav_taste in taste2) {
                                    for (taste in taste1) {
                                        list_taste1.add(taste)
                                    }
                                    list_taste1.distinct()
                                }
                            }

                            if (snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "0") {
                                for (taste in taste_list!!) {
                                    if (taste == "Nothing") {
                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                        list_taste1.add(
                                            snapshot.child("taste1").getValue().toString()
                                        )
                                        list_taste1.distinct()
                                    } else if (taste in taste2) {
                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                    }
                                }

                                for (company in company_list!!) {
                                    if (company == "Nothing") {
                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                    } else if (snapshot.child("brand").value.toString()
                                            .contains(company)
                                    ) {
                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                    }
                                }
                            }
                        }
                        for (taste in sort_taste2) {
                            for (company in sort_company) {
                                if (taste == company) {
                                    result.add(taste)
                                }
                            }
                        }

                        if(result.size < 3) {
                            for(snapshot in dataSnapshot.getChildren()) {
                                if(snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "0") {
                                    val taste1 = snapshot.child("taste1").getValue().toString().split(",")

                                    for (taste in list_taste1!!) {
                                        if (taste in taste1) {
                                            sort_taste1.add(snapshot.child("ID").getValue().toString())
                                        }
                                    }
                                }
                            }
                            for (taste in sort_taste1) {
                                for (company in sort_company) {
                                    if ((taste == company) && (taste !in result)) {
                                        result.add(taste)
                                    }
                                }
                            }
                        }

                        if(result.size < 3) {
                            for(taste in sort_taste2) {
                                if(taste !in result) {
                                    result.add(taste)
                                }
                            }
                        }

                        if(result.size < 3) {
                            for(taste in sort_taste1) {
                                if(taste !in result) {
                                    result.add(taste)
                                }
                            }
                        }
                    }

                    result.distinct()

                    var booster_after = ""
                    for(id in result) {
                        if(booster_after.isEmpty()){
                            booster_after = booster_after + id
                        }
                        else{
                            booster_after = booster_after + "," + id
                        }
                    }
                    Log.d("RecommendActivity_After", "Recommend Result (After) : $result")

                    shared_cloud.edit().remove("booster_after").apply()
                    shared_cloud.edit().putString("booster_after", booster_after).apply()
                    val uid = shared_cloud.getString("uid", "NoUid")
                    if(uid != "NoUid") {
                        database.getReference("members").child(uid!!).child("booster_after").setValue(booster_after)
                    }

                    onCallback()
                }

                override fun onCancelled(databaseError: DatabaseError) {}})
        }
        else {                      // 프로틴 추천
            if(now_fat > target_fat) {
                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var list_taste1 = ArrayList<String>()
                        var sort_taste2 = ArrayList<String>()
                        var sort_taste1 = ArrayList<String>()
                        var sort_company = ArrayList<String>()
                        var result = ArrayList<String>()

                        // Texture로 1차 sort
                        if(feeling == "clean") {
                            for (snapshot in dataSnapshot.getChildren()) {
                                val taste2 = snapshot.child("taste2").getValue().toString().split(",")
                                val taste1 = snapshot.child("taste2").getValue().toString().split(",")

                                for (fav_taste in taste_list!!) {
                                    if (fav_taste in taste2) {
                                        for (taste in taste1) {
                                            list_taste1.add(taste)
                                        }
                                        list_taste1.distinct()
                                    }
                                }

                                if(snapshot.child("fat(g)").getValue().toString().toFloat() <= 1.6F) {
                                    if (snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                        if (snapshot.child("texture").getValue().toString() == "clear") {
                                            if(kind_after == "vegan_protein") {
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                    for (taste in taste_list!!) {
                                                        if (taste == "Nothing") {
                                                            sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                            list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                            list_taste1.distinct()
                                                        }
                                                        else if (taste in taste2) {
                                                            sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }

                                                    for (company in company_list!!) {
                                                        if (company == "Nothing") {
                                                            sort_company.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                        else if (snapshot.child("brand").value.toString().contains(company)) {
                                                            sort_company.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                            else if(kind_after == "anti_milk_protein"){
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                    for (taste in taste_list!!) {
                                                        if (taste == "Nothing") {
                                                            sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                            list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                            list_taste1.distinct()
                                                        }
                                                        else if (taste in taste2) {
                                                            sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }

                                                    for (company in company_list!!) {
                                                        if (company == "Nothing") {
                                                            sort_company.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                        else if (snapshot.child("brand").value.toString().contains(company)) {
                                                            sort_company.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }

                                            }
                                            else{
                                                for (taste in taste_list!!) {
                                                    if (taste == "Nothing") {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                        list_taste1.distinct()
                                                    }
                                                    else if (taste in taste2) {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }

                                                for (company in company_list!!) {
                                                    if (company == "Nothing") {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                    else if (snapshot.child("brand").value.toString().contains(company)) {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            for (taste in sort_taste2) {
                                for (company in sort_company) {
                                    if (taste == company) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(snapshot in dataSnapshot.getChildren()) {
                                    if(snapshot.child("fat(g)").getValue().toString().toFloat() <= 1.6F) {
                                        if(snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                            if(snapshot.child("texture").getValue().toString() == "clear"){
                                                val taste1 = snapshot.child("taste1").getValue().toString().split(",")

                                                if(kind_after == "vegan_protein") {
                                                    if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                        for (taste in list_taste1!!) {
                                                            if (taste in taste1) {
                                                                sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                            }
                                                        }
                                                    }
                                                }
                                                else if(kind_after == "anti_milk_protein"){
                                                    if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                        for (taste in list_taste1!!) {
                                                            if (taste in taste1) {
                                                                sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                            }
                                                        }
                                                    }
                                                }
                                                else{
                                                    for (taste in list_taste1!!) {
                                                        if (taste in taste1) {
                                                            sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                for (taste in sort_taste1) {
                                    for (company in sort_company) {
                                        if ((taste == company) && (taste !in result)) {
                                            result.add(taste)
                                        }
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste2) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste1) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }
                        }
                        else {
                            for (snapshot in dataSnapshot.getChildren()) {
                                val taste2 = snapshot.child("taste2").getValue().toString().split(",")
                                val taste1 = snapshot.child("taste2").getValue().toString().split(",")

                                for (fav_taste in taste_list!!) {
                                    if (fav_taste in taste2) {
                                        for (taste in taste1) {
                                            list_taste1.add(taste)
                                        }
                                        list_taste1.distinct()
                                    }
                                }

                                if(snapshot.child("fat(g)").getValue().toString().toFloat() <= 1.6F) {
                                    if (snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                        if(kind_after == "vegan_protein") {
                                            if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                for (taste in taste_list!!) {
                                                    if (taste == "Nothing") {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                        list_taste1.distinct()
                                                    }
                                                    else if (taste in taste2) {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }

                                                for (company in company_list!!) {
                                                    if (company == "Nothing") {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                    else if (snapshot.child("brand").value.toString().contains(company)) {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }
                                        }
                                        else if(kind_after == "anti_milk_protein"){
                                            if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                for (taste in taste_list!!) {
                                                    if (taste == "Nothing") {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                        list_taste1.distinct()
                                                    }
                                                    else if (taste in taste2) {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }

                                                for (company in company_list!!) {
                                                    if (company == "Nothing") {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                    else if (snapshot.child("brand").value.toString().contains(company)) {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }

                                        }
                                        else{
                                            for (taste in taste_list!!) {
                                                if (taste == "Nothing") {
                                                    sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                    list_taste1.distinct()
                                                }
                                                else if (taste in taste2) {
                                                    sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                }
                                            }

                                            for (company in company_list!!) {
                                                if (company == "Nothing") {
                                                    sort_company.add(snapshot.child("ID").getValue().toString())
                                                }
                                                else if (snapshot.child("brand").value.toString().contains(company)) {
                                                    sort_company.add(snapshot.child("ID").getValue().toString())
                                                }
                                            }
                                        }
                                    }
                                }


                            }
                            for (taste in sort_taste2) {
                                for (company in sort_company) {
                                    if (taste == company) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(snapshot in dataSnapshot.getChildren()) {
                                    if(snapshot.child("fat(g)").getValue().toString().toFloat() <= 1.6F) {
                                        if(snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                            val taste1 = snapshot.child("taste1").getValue().toString().split(",")

                                            if(kind_after == "vegan_protein") {
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                    for (taste in list_taste1!!) {
                                                        if (taste in taste1) {
                                                            sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                            else if(kind_after == "anti_milk_protein"){
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                    for (taste in list_taste1!!) {
                                                        if (taste in taste1) {
                                                            sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                            else{
                                                for (taste in list_taste1!!) {
                                                    if (taste in taste1) {
                                                        sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                                for (taste in sort_taste1) {
                                    for (company in sort_company) {
                                        if ((taste == company) && (taste !in result)) {
                                            result.add(taste)
                                        }
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste2) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste1) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }
                        }

                        result.distinct()

                        var booster_after = ""
                        for(id in result) {
                            if(booster_after.isEmpty()){
                                booster_after = booster_after + id
                            }
                            else{
                                booster_after = booster_after + "," + id
                            }
                        }
                        Log.d("RecommendActivity_After", "Recommend Result (After) : $result")

                        shared_cloud.edit().remove("booster_after").apply()
                        shared_cloud.edit().putString("booster_after", booster_after).apply()
                        val uid = shared_cloud.getString("uid", "NoUid")
                        if(uid != "NoUid") {
                            database.getReference("members").child(uid!!).child("booster_after").setValue(booster_after)
                        }

                        onCallback()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}})
            }
            else if(now_muscle > target_muscle) {
                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var list_taste1 = ArrayList<String>()
                        var sort_taste2 = ArrayList<String>()
                        var sort_taste1 = ArrayList<String>()
                        var sort_company = ArrayList<String>()
                        var result = ArrayList<String>()

                        // Texture로 1차 sort
                        if(feeling == "clean") {
                            for (snapshot in dataSnapshot.getChildren()) {
                                val taste2 = snapshot.child("taste2").getValue().toString().split(",")
                                val taste1 = snapshot.child("taste2").getValue().toString().split(",")

                                for (fav_taste in taste_list!!) {
                                    if (fav_taste in taste2) {
                                        for (taste in taste1) {
                                            list_taste1.add(taste)
                                        }
                                        list_taste1.distinct()
                                    }
                                }

                                if(snapshot.child("protein(g)").getValue().toString().toFloat() >= 19.2F) {
                                    if (snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                        if (snapshot.child("texture").getValue().toString() == "clear") {
                                            if(kind_after == "vegan_protein") {
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                    for (taste in taste_list!!) {
                                                        if (taste == "Nothing") {
                                                            sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                            list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                            list_taste1.distinct()
                                                        }
                                                        else if (taste in taste2) {
                                                            sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }

                                                    for (company in company_list!!) {
                                                        if (company == "Nothing") {
                                                            sort_company.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                        else if (snapshot.child("brand").value.toString().contains(company)) {
                                                            sort_company.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                            else if(kind_after == "anti_milk_protein"){
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                    for (taste in taste_list!!) {
                                                        if (taste == "Nothing") {
                                                            sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                            list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                            list_taste1.distinct()
                                                        }
                                                        else if (taste in taste2) {
                                                            sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }

                                                    for (company in company_list!!) {
                                                        if (company == "Nothing") {
                                                            sort_company.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                        else if (snapshot.child("brand").value.toString().contains(company)) {
                                                            sort_company.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }

                                            }
                                            else{
                                                for (taste in taste_list!!) {
                                                    if (taste == "Nothing") {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                        list_taste1.distinct()
                                                    }
                                                    else if (taste in taste2) {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }

                                                for (company in company_list!!) {
                                                    if (company == "Nothing") {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                    else if (snapshot.child("brand").value.toString().contains(company)) {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                            }
                            for (taste in sort_taste2) {
                                for (company in sort_company) {
                                    if (taste == company) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(snapshot in dataSnapshot.getChildren()) {
                                    if(snapshot.child("protein(g)").getValue().toString().toFloat() >= 19.2F) {
                                        if(snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                            if(snapshot.child("texture").getValue().toString() == "clear"){
                                                val taste1 = snapshot.child("taste1").getValue().toString().split(",")

                                                if(kind_after == "vegan_protein") {
                                                    if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                        for (taste in list_taste1!!) {
                                                            if (taste in taste1) {
                                                                sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                            }
                                                        }
                                                    }
                                                }
                                                else if(kind_after == "anti_milk_protein"){
                                                    if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                        for (taste in list_taste1!!) {
                                                            if (taste in taste1) {
                                                                sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                            }
                                                        }
                                                    }
                                                }
                                                else{
                                                    for (taste in list_taste1!!) {
                                                        if (taste in taste1) {
                                                            sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                for (taste in sort_taste1) {
                                    for (company in sort_company) {
                                        if ((taste == company) && (taste !in result)) {
                                            result.add(taste)
                                        }
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste2) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste1) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }
                        }
                        else {
                            for (snapshot in dataSnapshot.getChildren()) {
                                val taste2 = snapshot.child("taste2").getValue().toString().split(",")
                                val taste1 = snapshot.child("taste2").getValue().toString().split(",")

                                for (fav_taste in taste_list!!) {
                                    if (fav_taste in taste2) {
                                        for (taste in taste1) {
                                            list_taste1.add(taste)
                                        }
                                        list_taste1.distinct()
                                    }
                                }

                                if(snapshot.child("protein(g)").getValue().toString().toFloat() >= 19.2F) {
                                    if (snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                        if(kind_after == "vegan_protein") {
                                            if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                for (taste in taste_list!!) {
                                                    if (taste == "Nothing") {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                        list_taste1.distinct()
                                                    }
                                                    else if (taste in taste2) {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }

                                                for (company in company_list!!) {
                                                    if (company == "Nothing") {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                    else if (snapshot.child("brand").value.toString().contains(company)) {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }
                                        }
                                        else if(kind_after == "anti_milk_protein"){
                                            if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                for (taste in taste_list!!) {
                                                    if (taste == "Nothing") {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                        list_taste1.distinct()
                                                    }
                                                    else if (taste in taste2) {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }

                                                for (company in company_list!!) {
                                                    if (company == "Nothing") {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                    else if (snapshot.child("brand").value.toString().contains(company)) {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }

                                        }
                                        else{
                                            for (taste in taste_list!!) {
                                                if (taste == "Nothing") {
                                                    sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                    list_taste1.distinct()
                                                }
                                                else if (taste in taste2) {
                                                    sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                }
                                            }

                                            for (company in company_list!!) {
                                                if (company == "Nothing") {
                                                    sort_company.add(snapshot.child("ID").getValue().toString())
                                                }
                                                else if (snapshot.child("brand").value.toString().contains(company)) {
                                                    sort_company.add(snapshot.child("ID").getValue().toString())
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            for (taste in sort_taste2) {
                                for (company in sort_company) {
                                    if (taste == company) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(snapshot in dataSnapshot.getChildren()) {
                                    if(snapshot.child("protein(g)").getValue().toString().toFloat() >= 19.2F) {
                                        if(snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                            val taste1 = snapshot.child("taste1").getValue().toString().split(",")

                                            if(kind_after == "vegan_protein") {
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                    for (taste in list_taste1!!) {
                                                        if (taste in taste1) {
                                                            sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                            else if(kind_after == "anti_milk_protein"){
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                    for (taste in list_taste1!!) {
                                                        if (taste in taste1) {
                                                            sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                            else{
                                                for (taste in list_taste1!!) {
                                                    if (taste in taste1) {
                                                        sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }

                                        }
                                    }

                                }
                                for (taste in sort_taste1) {
                                    for (company in sort_company) {
                                        if ((taste == company) && (taste !in result)) {
                                            result.add(taste)
                                        }
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste2) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste1) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }
                        }

                        result.distinct()

                        var booster_after = ""
                        for(id in result) {
                            if(booster_after.isEmpty()){
                                booster_after = booster_after + id
                            }
                            else{
                                booster_after = booster_after + "," + id
                            }
                        }
                        Log.d("RecommendActivity_After", "Recommend Result (After) : $result")

                        shared_cloud.edit().remove("booster_after").apply()
                        shared_cloud.edit().putString("booster_after", booster_after).apply()
                        val uid = shared_cloud.getString("uid", "NoUid")
                        if(uid != "NoUid") {
                            database.getReference("members").child(uid!!).child("booster_after").setValue(booster_after)
                        }

                        onCallback()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}})
            }
            else {
                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var list_taste1 = ArrayList<String>()
                        var sort_taste2 = ArrayList<String>()
                        var sort_taste1 = ArrayList<String>()
                        var sort_company = ArrayList<String>()
                        var result = ArrayList<String>()

                        // Texture로 1차 sort
                        if(feeling == "clean") {
                            for (snapshot in dataSnapshot.getChildren()) {
                                val taste2 = snapshot.child("taste2").getValue().toString().split(",")
                                val taste1 = snapshot.child("taste2").getValue().toString().split(",")

                                for (fav_taste in taste_list!!) {
                                    if (fav_taste in taste2) {
                                        for (taste in taste1) {
                                            list_taste1.add(taste)
                                        }
                                        list_taste1.distinct()
                                    }
                                }

                                if (snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                    if (snapshot.child("texture").getValue().toString() == "clear") {
                                        if(kind_after == "vegan_protein") {
                                            if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                for (taste in taste_list!!) {
                                                    if (taste == "Nothing") {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                        list_taste1.distinct()
                                                    }
                                                    else if (taste in taste2) {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }

                                                for (company in company_list!!) {
                                                    if (company == "Nothing") {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                    else if (snapshot.child("brand").value.toString().contains(company)) {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }
                                        }
                                        else if(kind_after == "anti_milk_protein"){
                                            if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                for (taste in taste_list!!) {
                                                    if (taste == "Nothing") {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                        list_taste1.distinct()
                                                    }
                                                    else if (taste in taste2) {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }

                                                for (company in company_list!!) {
                                                    if (company == "Nothing") {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                    else if (snapshot.child("brand").value.toString().contains(company)) {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }

                                        }
                                        else{
                                            for (taste in taste_list!!) {
                                                if (taste == "Nothing") {
                                                    sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                    list_taste1.distinct()
                                                }
                                                else if (taste in taste2) {
                                                    sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                }
                                            }

                                            for (company in company_list!!) {
                                                if (company == "Nothing") {
                                                    sort_company.add(snapshot.child("ID").getValue().toString())
                                                }
                                                else if (snapshot.child("brand").value.toString().contains(company)) {
                                                    sort_company.add(snapshot.child("ID").getValue().toString())
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            for (taste in sort_taste2) {
                                for (company in sort_company) {
                                    if (taste == company) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(snapshot in dataSnapshot.getChildren()) {
                                    if(snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                        if(snapshot.child("texture").getValue().toString() == "clear"){
                                            val taste1 = snapshot.child("taste1").getValue().toString().split(",")

                                            if(kind_after == "vegan_protein") {
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                    for (taste in list_taste1!!) {
                                                        if (taste in taste1) {
                                                            sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                            else if(kind_after == "anti_milk_protein"){
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                    for (taste in list_taste1!!) {
                                                        if (taste in taste1) {
                                                            sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                            else{
                                                for (taste in list_taste1!!) {
                                                    if (taste in taste1) {
                                                        sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                for (taste in sort_taste1) {
                                    for (company in sort_company) {
                                        if ((taste == company) && (taste !in result)) {
                                            result.add(taste)
                                        }
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste2) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste1) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }
                        }
                        else {
                            for (snapshot in dataSnapshot.getChildren()) {
                                val taste2 = snapshot.child("taste2").getValue().toString().split(",")
                                val taste1 = snapshot.child("taste2").getValue().toString().split(",")

                                for (fav_taste in taste_list!!) {
                                    if (fav_taste in taste2) {
                                        for (taste in taste1) {
                                            list_taste1.add(taste)
                                        }
                                        list_taste1.distinct()
                                    }
                                }

                                if (snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                    if(kind_after == "vegan_protein") {
                                            if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                for (taste in taste_list!!) {
                                                    if (taste == "Nothing") {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                        list_taste1.distinct()
                                                    }
                                                    else if (taste in taste2) {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }

                                                for (company in company_list!!) {
                                                    if (company == "Nothing") {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                    else if (snapshot.child("brand").value.toString().contains(company)) {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }
                                        }
                                    else if(kind_after == "anti_milk_protein"){
                                            if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                for (taste in taste_list!!) {
                                                    if (taste == "Nothing") {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                        list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                        list_taste1.distinct()
                                                    }
                                                    else if (taste in taste2) {
                                                        sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }

                                                for (company in company_list!!) {
                                                    if (company == "Nothing") {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                    else if (snapshot.child("brand").value.toString().contains(company)) {
                                                        sort_company.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }

                                        }
                                    else{
                                            for (taste in taste_list!!) {
                                                if (taste == "Nothing") {
                                                    sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                    list_taste1.add(snapshot.child("taste1").getValue().toString())
                                                    list_taste1.distinct()
                                                }
                                                else if (taste in taste2) {
                                                    sort_taste2.add(snapshot.child("ID").getValue().toString())
                                                }
                                            }

                                            for (company in company_list!!) {
                                                if (company == "Nothing") {
                                                    sort_company.add(snapshot.child("ID").getValue().toString())
                                                }
                                                else if (snapshot.child("brand").value.toString().contains(company)) {
                                                    sort_company.add(snapshot.child("ID").getValue().toString())
                                                }
                                            }
                                        }
                                }
                            }
                            for (taste in sort_taste2) {
                                for (company in sort_company) {
                                    if (taste == company) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(snapshot in dataSnapshot.getChildren()) {
                                    if(snapshot.child("class1(전0후1)").getValue().toString() == "1" && snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue().toString() == "1") {
                                        val taste1 = snapshot.child("taste1").getValue().toString().split(",")

                                        if(kind_after == "vegan_protein") {
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() == "3") {
                                                    for (taste in list_taste1!!) {
                                                        if (taste in taste1) {
                                                            sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                        else if(kind_after == "anti_milk_protein"){
                                                if(snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "0" && snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue().toString() != "4") {
                                                    for (taste in list_taste1!!) {
                                                        if (taste in taste1) {
                                                            sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                        }
                                                    }
                                                }
                                            }
                                        else{
                                                for (taste in list_taste1!!) {
                                                    if (taste in taste1) {
                                                        sort_taste1.add(snapshot.child("ID").getValue().toString())
                                                    }
                                                }
                                            }

                                    }
                                }
                                for (taste in sort_taste1) {
                                    for (company in sort_company) {
                                        if ((taste == company) && (taste !in result)) {
                                            result.add(taste)
                                        }
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste2) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }

                            if(result.size < 3) {
                                for(taste in sort_taste1) {
                                    if(taste !in result) {
                                        result.add(taste)
                                    }
                                }
                            }
                        }

                        result.distinct()

                        var booster_after = ""
                        for(id in result) {
                            if(booster_after.isEmpty()){
                                booster_after = booster_after + id
                            }
                            else{
                                booster_after = booster_after + "," + id
                            }
                        }
                        Log.d("RecommendActivity_After", "Recommend Result (After) : $result")

                        shared_cloud.edit().remove("booster_after").apply()
                        shared_cloud.edit().putString("booster_after", booster_after).apply()
                        val uid = shared_cloud.getString("uid", "NoUid")
                        if(uid != "NoUid") {
                            database.getReference("members").child(uid!!).child("booster_after").setValue(booster_after)
                        }

                        onCallback()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}})
            }
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed()
    }

    override fun onCallback() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }
}