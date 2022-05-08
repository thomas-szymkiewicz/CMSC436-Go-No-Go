package com.example.gonogo

import java.util.*
import kotlin.collections.ArrayList

class TaskItem {
    var num : Int = 0
    var date : String
    var types : ArrayList<Boolean>
    var responses : ArrayList<Boolean>
    var times : ArrayList<Double>
    var new = false
    var mode = false


    internal constructor(num: Int, date: String, types: ArrayList<Boolean>, responses: ArrayList<Boolean>, times: ArrayList<Double>, new: Boolean, mode: Boolean) {
        this.num = num
        this.date = date
        this.types = types
        this.responses = responses
        this.times = times
        this.new = new
        this.mode = mode
    }

    override fun toString(): String {
        return (num.toString() + ITEM_SEP + date + ITEM_SEP +
                types.toString() + ITEM_SEP + responses.toString() +
                ITEM_SEP + times.toString() + ITEM_SEP + mode.toString())
    }

    fun averageResponseTime(): Double {
        var total = 0.0
        var count = 0
        for (i in 0..types.size-1) {
            if (types.get(i)) {
                total += times.get(i)
                count += 1
            }
        }
        return total / count
    }

    fun totalPercentCorrect(): Double {
        var count = 0.0
        for (b in responses) {
            if (b) {
                count += 1
            }
        }
        return count / responses.size
    }

    fun goPercentCorrect(): Double {
        var total = 0
        var count = 0.0
        for (i in 0..types.size-1) {
            if (types.get(i)) {
                total += 1
                if (responses.get(i)) {
                    count += 1
                }
            }
        }
        return count / total
    }

    fun nogoPercentCorrect(): Double {
        var total = 0
        var count = 0.0
        for (i in 0..types.size-1) {
            if (!types.get(i)) {
                total += 1
                if (responses.get(i)) {
                    count += 1
                }
            }
        }
        return count / total
    }
    fun numWrong(): Int {
        var count = 0
        for (b in responses) {
            if (!b) {
                count += 1
            }
        }

        return count
    }

    companion object {
        val ITEM_SEP: String? = System.getProperty("line.separator")
    }
}