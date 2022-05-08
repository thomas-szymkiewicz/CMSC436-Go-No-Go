package com.example.gonogo

class TrialItem {
    var type : Boolean = true
    var response : Boolean = true
    var time : Double = 0.0


    internal constructor(type: Boolean, response: Boolean, time: Double) {
        this.type = type
        this.response = response
        this.time = time
    }
}