package com.example.hypersonalbooster

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutRecommendAfterBinding
import com.example.hypersonalbooster.databinding.LayoutRecommendSaveBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class RecommendActivity_Save() : AppCompatActivity(), CloudCallbackListener {

    //  클릭 확인용 변수
    var check: Boolean = false

    private lateinit var binding : LayoutRecommendSaveBinding

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutRecommendSaveBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val flag = getSharedPreferences("data_cloud", 0).getString("flag_save", "false")
        if(flag == "true") {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        val shared_cloud = getSharedPreferences("data_cloud", 0)
        val shared_before = getSharedPreferences("booster_before", 0)
        val editor_before = shared_before.edit()
        val shared_after = getSharedPreferences("booster_after", 0)
        val editor_after = shared_after.edit()

        editor_before.clear().apply()
        editor_after.clear().apply()

        var booster_before = ArrayList<String>()
        val before_list = shared_cloud.getString("booster_before", "Nothing").toString().split(",")
        for(booster in before_list) {
            booster_before.add(booster)
        }

        var booster_after = ArrayList<String>()
        val after_list = shared_cloud.getString("booster_after", "Nothing").toString().split(",")
        for(booster in after_list) {
            booster_after.add(booster)
        }

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.getChildren()) {

                    val cloud_id = snapshot.child("ID").getValue().toString()

                    if(cloud_id in booster_before) {
                        var ID : String = ""
                        var name : String = ""              // 이름
                        var taste : String = ""
                        var class_0 : String = ""           // 분류0 : 가루, 액체, 그외
                        var class_1 : String = ""           // 분류1 : 운동 전/후
                        var class_2 : String = ""           // 분류2 : 상세분류
                        var amount : Float = 0.0F           // 1회 제공량
                        var amount_string : String = ""     // 1회 제공량 단위까지 붙여서 표시
                        var calories : String = ""              // 칼로리
                        var carb : String = ""            // 탄수화물
                        var sugar : String = ""            // 당
                        var fat : String = ""              // 지방
                        var sat_fat : String = ""          // 포화지방
                        var protein : String = ""          // 단백질
                        var link : String = ""              // 구매링크
                        var etc : String = ""               // 특이사항
                        var company : String = ""           // 회사
                        var texture : String = ""           // 느낌

                        ID += cloud_id
                        name = snapshot.child("name").getValue().toString()
                        Log.d("Booster Save", "Start Init : $name")

                        company = snapshot.child("brand").getValue().toString()
                        taste = snapshot.child("taste2").getValue().toString()
                        if(taste.contains(",")) {
                            taste.replace(",", ", ")
                        }
                        texture = snapshot.child("texture").getValue().toString()
                        Log.d("Booster Init", "$company, $taste, $texture")
                        val info_basic = company + "*" + taste + "*" + texture

                        var temp_0 = snapshot.child("class0(가루0액체1그외2)").getValue()
                        var temp_1 = snapshot.child("class1(전0후1)").getValue()
                        var temp_2 = snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue()
                        var temp_3 = snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue()
                        Log.d("Booster Save", "read complete : $temp_0, $temp_1, $temp_2, $temp_3")

                        if(temp_0 == 0L) {
                            class_0 += "powder"
                        }
                        else if(temp_0 == 1L) {
                            class_0 += "liquid"
                        }
                        else if(temp_0 == 2L) {
                            class_0 += "else"
                        }

                        if(temp_1 == 0L) {
                            class_1 += "before"

                            if(temp_2 == 0L) {
                                if(temp_3 == 0L) {
                                    class_2 += "무카페인 BCAA"
                                }
                                else if(temp_3 == 1L || temp_3 == 3L) {
                                    class_2 += "BCAA"
                                }
                            }
                            else if(temp_2 == 1L) {
                                if(temp_3 == 0L) {
                                    class_2 += "무카페인 부스터"
                                }
                                else if(temp_3 == 1L) {
                                    class_2 += "부스터"
                                }
                            }
                        }
                        else if(temp_1 == 1L) {
                            class_1 += "after"

                            if(temp_2 == 0L) {
                                if(texture == "thick") {
                                    class_2 += "게이너"
                                }
                                else if(texture =="clear") {
                                    class_2 +="클리어웨이 게이너"
                                }
                            }
                            else if(temp_2 == 1L) {
                                if(texture == "thick") {
                                    if(temp_3 == 1L || temp_3 == 2L) {
                                        class_2 += "분리 유청 프로틴"
                                    }
                                    else if(temp_3 == 0L || temp_3 == 4L) {
                                        class_2 += "프로틴"
                                    }
                                    else if(temp_3 == 3L) {
                                        class_2 += "비건 프로틴"
                                    }
                                }
                                else if(texture == "clear") {
                                    if(temp_3 == 1L || temp_3 == 2L) {
                                        class_2 += "분리 유청 클리어웨이 프로틴"
                                    }
                                    else if(temp_3 == 0L || temp_3 == 4L) {
                                        class_2 += "클리어웨이 프로틴"
                                    }
                                    else if(temp_3 == 3L) {
                                        class_2 += "비건 클리어웨이 프로틴"
                                    }
                                }
                            }
                        }
                        Log.d("Booster Save", "Class : $class_0, $class_1, $class_2")
                        val info_class = class_0 + "*" + class_1 + "*" + class_2

                        amount = snapshot.child("amount(1회제공량(가루g액체ml))").getValue().toString().toFloat()
                        if(class_0 == "liquid") {
                            amount_string += (amount.toString() + "ml")
                        }
                        else {
                            amount_string += (amount.toString() + "g")
                        }
                        Log.d("Booster Save", "Amount : $amount, $amount_string")
                        val info_amount = amount_string

                        calories = snapshot.child("calories(kcal)").getValue().toString()
                        carb = snapshot.child("carb(g)").getValue().toString()
                        sugar = snapshot.child("sugars(g)").getValue().toString()
                        fat = snapshot.child("fat(g)").getValue().toString()
                        sat_fat = snapshot.child("sat fat(g)").getValue().toString()
                        protein = snapshot.child("protein(g)").getValue().toString()

                        Log.d("Booster Save", "Nutruent : $calories, $carb, $sugar, $fat, $sat_fat, $protein")
                        val info_nutrient = calories + "*" + carb + "*" + sugar + "*" + fat + "*" +sat_fat + "*" +protein


                        link = snapshot.child("link").getValue().toString()

                        val info = ID + "*" + name + "*" + info_basic + "*" +  info_class + "*" +  info_amount + "*" +  info_nutrient + "*" + link
                        editor_before.putString(ID, info).apply()
                    }
                    if(cloud_id in booster_after) {
                        var ID : String = ""
                        var name : String = ""              // 이름
                        var taste : String = ""
                        var class_0 : String = ""           // 분류0 : 가루, 액체, 그외
                        var class_1 : String = ""           // 분류1 : 운동 전/후
                        var class_2 : String = ""           // 분류2 : 상세분류
                        var amount : Float = 0.0F           // 1회 제공량
                        var amount_string : String = ""     // 1회 제공량 단위까지 붙여서 표시
                        var calories : String = ""              // 칼로리
                        var carb : String = ""            // 탄수화물
                        var sugar : String = ""            // 당
                        var fat : String = ""              // 지방
                        var sat_fat : String = ""          // 포화지방
                        var protein : String = ""          // 단백질
                        var link : String = ""              // 구매링크
                        var etc : String = ""               // 특이사항
                        var company : String = ""           // 회사
                        var texture : String = ""           // 느낌

                        ID += cloud_id
                        name = snapshot.child("name").getValue().toString()
                        Log.d("Booster Save", "Start Init : $name")

                        company = snapshot.child("brand").getValue().toString()
                        taste = snapshot.child("taste2").getValue().toString()
                        if(taste.contains(",")) {
                            taste.replace(",", ", ")
                        }
                        texture = snapshot.child("texture").getValue().toString()
                        Log.d("Booster Init", "$company, $taste, $texture")
                        val info_basic = company + "*" + taste + "*" + texture

                        var temp_0 = snapshot.child("class0(가루0액체1그외2)").getValue()
                        var temp_1 = snapshot.child("class1(전0후1)").getValue()
                        var temp_2 = snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue()
                        var temp_3 = snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue()
                        Log.d("Booster Save", "read complete : $temp_0, $temp_1, $temp_2, $temp_3")

                        if(temp_0 == 0L) {
                            class_0 += "powder"
                        }
                        else if(temp_0 == 1L) {
                            class_0 += "liquid"
                        }
                        else if(temp_0 == 2L) {
                            class_0 += "else"
                        }

                        if(temp_1 == 0L) {
                            class_1 += "before"

                            if(temp_2 == 0L) {
                                if(temp_3 == 0L) {
                                    class_2 += "무카페인 BCAA"
                                }
                                else if(temp_3 == 1L || temp_3 == 3L) {
                                    class_2 += "BCAA"
                                }
                            }
                            else if(temp_2 == 1L) {
                                if(temp_3 == 0L) {
                                    class_2 += "무카페인 부스터"
                                }
                                else if(temp_3 == 1L) {
                                    class_2 += "부스터"
                                }
                            }
                        }
                        else if(temp_1 == 1L) {
                            class_1 += "after"

                            if(temp_2 == 0L) {
                                if(texture == "thick") {
                                    class_2 += "게이너"
                                }
                                else if(texture =="clear") {
                                    class_2 +="클리어웨이 게이너"
                                }
                            }
                            else if(temp_2 == 1L) {
                                if(texture == "thick") {
                                    if(temp_3 == 1L || temp_3 == 2L) {
                                        class_2 += "분리 유청 프로틴"
                                    }
                                    else if(temp_3 == 0L || temp_3 == 4L) {
                                        class_2 += "프로틴"
                                    }
                                    else if(temp_3 == 3L) {
                                        class_2 += "비건 프로틴"
                                    }
                                }
                                else if(texture == "clear") {
                                    if(temp_3 == 1L || temp_3 == 2L) {
                                        class_2 += "분리 유청 클리어웨이 프로틴"
                                    }
                                    else if(temp_3 == 0L || temp_3 == 4L) {
                                        class_2 += "클리어웨이 프로틴"
                                    }
                                    else if(temp_3 == 3L) {
                                        class_2 += "비건 클리어웨이 프로틴"
                                    }
                                }
                            }
                        }
                        Log.d("Booster Save", "Class : $class_0, $class_1, $class_2")
                        val info_class = class_0 + "*" + class_1 + "*" + class_2

                        amount = snapshot.child("amount(1회제공량(가루g액체ml))").getValue().toString().toFloat()
                        if(class_0 == "liquid") {
                            amount_string += (amount.toString() + "ml")
                        }
                        else {
                            amount_string += (amount.toString() + "g")
                        }
                        Log.d("Booster Save", "Amount : $amount, $amount_string")
                        val info_amount = amount_string

                        calories = snapshot.child("calories(kcal)").getValue().toString()
                        carb = snapshot.child("carb(g)").getValue().toString()
                        sugar = snapshot.child("sugars(g)").getValue().toString()
                        fat = snapshot.child("fat(g)").getValue().toString()
                        sat_fat = snapshot.child("sat fat(g)").getValue().toString()
                        protein = snapshot.child("protein(g)").getValue().toString()

                        Log.d("Booster Save", "Nutruent : $calories, $carb, $sugar, $fat, $sat_fat, $protein")
                        val info_nutrient = calories + "*" + carb + "*" + sugar + "*" + fat + "*" +sat_fat + "*" +protein


                        link = snapshot.child("link").getValue().toString()

                        val info = ID + "*" + name + "*" + info_basic + "*" +  info_class + "*" +  info_amount + "*" +  info_nutrient + "*" + link
                        editor_after.putString(ID, info).apply()
                        shared_cloud.edit().remove("flag_save").apply()
                        shared_cloud.edit().putString("flag_save", "true").apply()
                        onCallback()
                    }
                }


            }

            override fun onCancelled(error: DatabaseError) { } })
    }

    override fun onBackPressed() {
        // super.onBackPressed()
    }

    override fun onCallback() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }
}