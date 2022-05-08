package com.example.gonogo

//import android.R
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


class GoNoGoActivity : AppCompatActivity() {

    var circle: ImageView ? = null
    var changeColor: GradientDrawable ? = null
    var circleText: TextView? = null
    var scoreText: TextView? = null
    var modeText: TextView? = null
    var timeText: TextView? = null

    var mTask: TaskItem? = null
    private var startTrialTime = 0L;

    private val interval = 1 // 1 second timer
    private var mHandler: Handler? = null
    private var totalTime = 0L // time in 60ths of a second
    private var WAIT_TIME = 400L // time between colors
    private var STD_SCORE = 10 // number of buttons to click in Standard mode

    // recommended ACTION_TIME of less than 15000ms
    private var ACTION_TIME = 1000L // time to press a button / time red lasts

    var go = true // true when circle is green
    var score = 0
    var mode = ""
    var done = false
    var active = true

    //private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gonogoactivity)
        circle = findViewById(R.id.circle_view)
        circleText = findViewById(R.id.circle_text)
        scoreText = findViewById(R.id.score)
        modeText = findViewById(R.id.mode)
        timeText = findViewById(R.id.time)

        var menuBtn = findViewById<Button>(R.id.menuBtn)
        var quitBtn = findViewById<Button>(R.id.quitBtn)

        mTask = TaskItem(0, Date().toString(), ArrayList<Boolean>(), ArrayList<Boolean>(), ArrayList<Double>(), false)

        // Get mode from the intent that launched activity
        val goMode = intent.getStringExtra("Mode").toString()

        startGame(goMode)
        startTimer()


        // menu button finishes activity
        menuBtn.setOnClickListener {
            // testing code, to be deleted
            // modeText?.text = mode

            finish()
        }

        // quit button closes app
        quitBtn.setOnClickListener {
            exitProcess(1)
        }

        circle?.setOnClickListener {
            if (active) {
                if (go) {
                    mTask!!.types.add(true)
                    mTask!!.responses.add(true)
                    mTask!!.times.add(toSeconds(totalTime - startTrialTime))
                    if (mode == "Endless") {
                        addPoint()
                    } else {
                        subPoint()
                    }
                } else {
                    mTask!!.types.add(false)
                    mTask!!.responses.add(false)
                    mTask!!.times.add(toSeconds(totalTime - startTrialTime))

                    if (mode == "Endless") {
                        done = true
                        saveItemsToFile(mTask!!)
                        goToStats()
                        addPoint()
                    } else {
                        subPoint()
                    }

                }
                nextColor()
            }
        }
    }

    private fun startGame(inputMode: String) {
        if (inputMode == "Endless") {
            mode = "Endless"
            score = 0

            // sets up text for score
            scoreText?.text = "Score: " + score
        } else {
            mode = "Standard"
            score = STD_SCORE

            // sets up text for score
            scoreText?.text = "Remaining: " + score
        }

        // sets up text for mode
        modeText?.text = mode

        nextColor()
    }

    private fun nextColor() {
        // clears text in the circle
        circleText?.text = ""

        // changes background color to white
        changeColor = circle?.background?.mutate() as GradientDrawable
        changeColor?.setColor(Color.WHITE)


        var r = Random()
        var nextColor = r.nextInt(100)

        active = false
        // recommended noGo% of less than 15%
        Handler().postDelayed({
            active = true
            // 15% chance of noGo
            if (nextColor < 15) {
                noGo()
            } else {
                go()
            }
        }, WAIT_TIME)

    }

    // used for Endless mode
    private fun addPoint() {
        score++
        scoreText?.text = "Score: " + score
    }

    // used for Standard mode
    private fun subPoint() {
        score--
        if (score != 0) {
            scoreText?.text = "Remaining: " + score
        } else {
            done = true
            saveItemsToFile(mTask!!)
            goToStats()
        }

    }

    private fun go() {
        // change color
        changeColor = circle?.background?.mutate() as GradientDrawable
        changeColor?.setColor(Color.GREEN)

        // change circle text
        circleText?.text = "Go!"

        go = true
        startTrialTime = totalTime

        // if score does not change after ACTION_TIME milliseconds the user loses
        var prevScore = score
        if (!done) {
            Handler().postDelayed({
                if (score == prevScore) {
                    mTask!!.types.add(true)
                    mTask!!.responses.add(false)
                    mTask!!.times.add(ACTION_TIME.toDouble() / 1000)
                    if (mode == "Endless") {
                        addPoint()
                        done = true
                        saveItemsToFile(mTask!!)
                        goToStats()

                    } else {
                        subPoint()
                    }

                    nextColor()
                }
            }, ACTION_TIME)
        }
    }

    private fun noGo() {
        // change color
        changeColor = circle?.background?.mutate() as GradientDrawable
        changeColor?.setColor(Color.RED)

        // change circle text
        circleText?.text = "No Go!"

        go = false
        startTrialTime = totalTime

        var prevScore = score
        // adds point after ACTION_TIME milliseconds pass
        if (!done) {
            Handler().postDelayed({
                if (score == prevScore) {
                    mTask!!.types.add(false)
                    mTask!!.responses.add(true)
                    mTask!!.times.add(ACTION_TIME.toDouble() / 1000)

                    if (mode == "Endless") {
                        addPoint()

                    } else {
                        subPoint()
                    }
                    nextColor()
                }
            }, ACTION_TIME)
        }
    }

    private fun startTimer() {
        mHandler = Handler(Looper.getMainLooper())
        mStatusChecker.run()
    }

    private var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                totalTime += 1
                updateTimeView(totalTime)
            } finally {
                mHandler!!.postDelayed(this, interval.toLong())
            }
        }
    }

    private fun updateTimeView(passedTime: Long) {
        var buildTimeText = "Time Elapsed: "
        buildTimeText += (passedTime/3600).toString().padStart(2,'0') + ":"
        buildTimeText += ((passedTime%3600)/60).toString().padStart(2,'0') + "."
        buildTimeText += (((passedTime%60)/60.0)*1000).toInt().toString().padStart(3,'0')

        timeText?.text = buildTimeText
    }

    private fun toSeconds(time: Long): Double {
        return (time.toDouble()/60)
    }

    private fun goToStats() {
        var intent = Intent(this, TaskManagerActivity::class.java);
        ContextCompat.startActivity(this, intent, null);
    }

    fun saveItemsToFile(task: TaskItem) {
        var writer: PrintWriter? = null
        try {
            val fos = openFileOutput("TaskManagerActivityData.txt", Context.MODE_APPEND)
            writer = PrintWriter(
                BufferedWriter(
                    OutputStreamWriter(
                        fos)
                )
            )

            writer.println(task)

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            writer?.close()
        }
    }
}