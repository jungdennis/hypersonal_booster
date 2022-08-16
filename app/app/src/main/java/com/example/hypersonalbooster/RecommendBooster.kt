package com.example.hypersonalbooster

class RecommendBooster() {
    init {
        // basic information
        var age : Int
        var sex : String
        var pragent : Boolean

        // health information
        var height : Float
        var weight : Float
        var fat : Float
        var muscle : Float

        // favorite information


        // Particular information
        var milk : Boolean
        var vegan : Boolean
        var caffeine : Boolean
    }

    // getter
    fun get_basic(Age : Int, Sex: String, Pragent : Boolean) {
        this.age = Age
        this.sex = Sex
        this.pragent = Pragent
    }
    fun get_health(Height : Float, Weight : Float, Fat : Float, Muscle : Float) {
        this.now_height = Height
        this.now_weight = Weight
        this.now_fat = Fat
        this.now_muscle = Muscle
    }
    fun get_target(Weight : Float, Fat : Float, Muscle : Float) {
        this.now_weight = Weight
        this.now_fat = Fat
        this.now_muscle = Muscle
    }
    fun get_particular(Vegan : Boolean, Milk : Boolean, Caffeine : Boolean) {
        this.vegan = Vegan
        this.milk = Milk
        this.caffeine = Caffeine
    }

}