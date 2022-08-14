package com.example.hypersonalbooster

internal interface RecommendInterface {
    var height : Float
    var weight : Float
    var bmi : Float
    var fat : Float
    var muscle : Float

    var bmiRange : FloatArray
    var fatRange : FloatArray
    var muscleRange : FloatArray

    fun getBMI() {
        bmi = weight / ((height / 100) * (height / 100))
    }
    fun getStandard() {

    }
}