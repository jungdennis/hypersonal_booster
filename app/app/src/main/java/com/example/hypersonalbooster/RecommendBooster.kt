package com.example.hypersonalbooster

class RecommendBooster() {
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

    // Particular information
    var milk : Boolean = false
    var vegan : Boolean = false
    var caffeine : Boolean = false

    // setter
    fun set_basic(Age : Int, Sex: String, Pragent : Boolean) {
        this.age = Age
        this.sex = Sex
        this.pragent = Pragent
    }
    fun set_health(Height : Float, Weight : Float, Fat : Float, Muscle : Float) {
        this.now_height = Height
        this.now_weight = Weight
        this.now_fat = Fat
        this.now_muscle = Muscle
    }
    fun set_target(Weight : Float, Fat : Float, Muscle : Float) {
        this.target_weight = Weight
        this.target_fat = Fat
        this.target_muscle = Muscle
    }
    fun set_particular(Vegan : Boolean, Milk : Boolean, Caffeine : Boolean) {
        this.vegan = Vegan
        this.milk = Milk
        this.caffeine = Caffeine
    }
}