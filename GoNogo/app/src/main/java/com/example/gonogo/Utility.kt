package com.example.gonogo

import android.content.Context
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.util.ArrayList
import java.io.*

fun BooleanArrayListFromString(input: String) : ArrayList<Boolean> {
    var str = input.replace("[", "")
    str = str.replace("]", "")
    val elements = str.split(",")
    var finalList = ArrayList<Boolean>();
    for (b in elements) {
        finalList.add(b.trim().toBoolean())
    }
    return finalList
}

fun DoubleArrayListFromString(input: String) : ArrayList<Double> {
    var str = input.replace("[", "")
    str = str.replace("]", "")
    val elements = str.split(",")
    var finalList = ArrayList<Double>();
    for (d in elements) {
        finalList.add(d.trim().toDouble())
    }
    return finalList
}

fun maxPercent(lst: ArrayList<TaskItem>): Double {
    var max = 0.0
    for (t in lst) {
        val curr = t.totalPercentCorrect()
        if (curr > max) {
            max = curr
        }
    }
    return max
}

fun minPercent(lst: ArrayList<TaskItem>): Double {
    var min = 100.0
    for (t in lst) {
        val curr = t.totalPercentCorrect()
        if (curr < min) {
            min = curr
        }
    }
    return min
}

fun maxTime(lst: ArrayList<TaskItem>): Double {
    var max = 0.0
    for (t in lst) {
        val curr = t.averageResponseTime()
        if (curr > max) {
            max = curr
        }
    }
    return max
}

fun minTime(lst: ArrayList<TaskItem>): Double {
    var min = 1000.0
    for (t in lst) {
        val curr = t.averageResponseTime()
        if (curr < min) {
            min = curr
        }
    }
    return min
}