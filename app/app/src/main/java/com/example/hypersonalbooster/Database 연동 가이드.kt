package com.example.hypersonalbooster

/*
class Database 연동 가이드 {
    1. 반드시 onCreate 밖에서 선언해줘야 할 것들
    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    2. 데이터 저장하기 : child로 연속 사용하여 tree 형태로 접근 후, setValue로 값 저장
    override fun onCreate() {

        ref.child("branch 이름"). ... .child("key 이름").setValue(value)

    }

    3. 데이터 찾기 :
    override fun onCreate() {

            ref.child("kiosk").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.getChildren()) {
                    val brand = snapshot.getValue()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}})

    }
}
*/

// ${it.value}