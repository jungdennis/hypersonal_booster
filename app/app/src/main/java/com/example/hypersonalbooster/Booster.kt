package com.example.hypersonalbooster

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Booster(id : String) {
    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster")

    var name : String = ""
    var class_1 : String = ""
    var class_2 : String = ""

    init {
        ref.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                name = snapshot.child("name").getValue().toString()
                Log.d("Booster Init", "Start Init : $name")

                var temp_1 = snapshot.child("class1(전0후1)").getValue()
                var temp_2 = snapshot.child("class2(BCAA0부스터류1)(게이너0그외1)").getValue()
                Log.d("Booster Init", "read complete : $temp_1, $temp_2")

                if(temp_1 == 0L) {
                    class_1 += "before"
                    if(temp_2 == 0L) {
                        class_2 += "BCAA"
                    }
                    else if(temp_2 == 1L) {
                        class_2 += "부스터"
                    }
                }
                else if(temp_1 == 1L) {
                    class_1 += "after"
                    if(temp_2 == 0L) {
                        class_2 += "게이너"
                    }
                    else if(temp_2 == 1L) {
                        class_2 += "프로틴"
                    }
                }
                Log.d("Booster Init", "Sort Complete : $class_1, $class_2")

            }

            override fun onCancelled(error: DatabaseError) { } })
    }
}