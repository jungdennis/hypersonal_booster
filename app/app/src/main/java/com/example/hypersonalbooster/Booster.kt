package com.example.hypersonalbooster

import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class Booster(id : String) {
    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster")

    // 데이터베이스로 주로 사용할 변수들
    var name : String = ""              // 이름
    var class_0 : String = ""           // 분류0 : 가루, 액체, 그외
    var class_1 : String = ""           // 분류1 : 운동 전/후
    var class_2 : String = ""           // 분류2 : 상세분류
    var amount : Float = 0.0F           // 1회 제공량
    var amount_string : String = ""     // 1회 제공량 단위까지 붙여서 표시
    var calories : Int = 0              // 칼로리
    var carb : Float = 0.0F             // 탄수화물
    var sugar : Float = 0.0F            // 당
    var fat : Float = 0.0F              // 지방
    var sat_fat : Float = 0.0F          // 포화지방
    var protein : Float = 0.0F          // 단백질
    var link : String = ""              // 구매링크
    var etc : String = ""               // 특이사항

    // 엥간하면 사용할 일 없는 변수들
    var company : String = ""           // 회사
    //var taste_1 = ArrayList<String>()   // 맛1 : 대분류
    //var taste_2 : ArrayList<String>()   // 맛2 : 상세분류
    var texture : String = ""           // 느낌

    init {
        ref.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                name = snapshot.child("name").getValue().toString()
                Log.d("Booster Init", "Start Init : $name")

                texture = snapshot.child("texture").getValue().toString()
                company = snapshot.child("brand").getValue().toString()
                Log.d("Booster Init", "Texture : $texture")

                var temp_0 = snapshot.child("class0(가루0액체1그외2)").getValue()
                var temp_1 = snapshot.child("class1(전0후1)").getValue()
                var temp_2 = snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue()
                var temp_3 = snapshot.child("class3(카페인x0카페인o1)(WPC0WPI1WPH2비건3카제인4)").getValue()
                Log.d("Booster Init", "read complete : $temp_0, $temp_1, $temp_2, $temp_3")

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
                        else if(temp_3 == 1L) {
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
                        class_2 += "게이너"
                    }
                    else if(temp_2 == 1L) {
                        if(texture == "thick") {
                            if(temp_3 == 1L || temp_3 == 2L) {
                                class_2 += "분리 유청 프로틴"
                            }
                            else if(temp_3 == 0L || temp_3 == 3L) {
                                class_2 += "프로틴"
                            }
                        }
                        else if(texture == "clear") {
                            if(temp_3 == 1L || temp_3 == 2L) {
                                class_2 += "분리 유청 클리어웨이"
                            }
                            else if(temp_3 == 0L || temp_3 == 3L) {
                                class_2 += "클리어웨이"
                            }
                        }
                    }
                }
                Log.d("Booster Init", "Sort Complete : $class_0, $class_1, $class_2")

                amount = snapshot.child("amount(1회제공량(가루g액체ml))").getValue().toString().toFloat()
                if(class_0 == "liquid") {
                    amount_string += (amount.toString() + "ml")
                }
                else {
                    amount_string += (amount.toString() + "g")
                }
                Log.d("Booster Init", "Amount : $amount, $amount_string")

                calories = snapshot.child("calories(kcal)").getValue().toString().toInt()
                carb = snapshot.child("carb(g)").getValue().toString().toFloat()
                sugar = snapshot.child("sugars(g)").getValue().toString().toFloat()
                fat = snapshot.child("fat(g)").getValue().toString().toFloat()
                sat_fat = snapshot.child("sat fat(g)").getValue().toString().toFloat(

                )
                protein = snapshot.child("protein(g)").getValue().toString().toFloat()

                Log.d("Booster Init", "Nutruent : $calories, $carb, $sugar, $fat, $sat_fat, $protein")

                link = snapshot.child("link").getValue().toString()
                etc = snapshot.child("비고").getValue().toString()
            }

            override fun onCancelled(error: DatabaseError) { } })
    }
}