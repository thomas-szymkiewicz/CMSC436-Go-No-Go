package com.example.gonogo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

class StartMenu : Activity(){
    private lateinit var standardButton: Button
    private lateinit var endlessButton: Button
    private lateinit var statsButton: Button
    private lateinit var helpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startmenu)

        // Get a reference to the activity buttons
        standardButton = findViewById(R.id.standard_button)
        endlessButton = findViewById(R.id.endless_button)
        statsButton = findViewById(R.id.stats_button)
        helpButton = findViewById(R.id.help_button)
    }

    // Set an OnClickListener for the standard Go-No Go
    // Starts the standard activity
    fun standardClick(v: View) {

        // Intent for the Standard
        val standardModeIntent = Intent(
            this@StartMenu,
            GoNoGoActivity::class.java
        )

        standardModeIntent.putExtra("Mode", "Standard")

        // Use the Intent to start the Standard Go-No Go Activity
        startActivity(standardModeIntent)

    }

    // Set an OnClickListener for the endless Go-No Go
    // Starts the endless activity
    fun endlessClick(v: View) {

        // Intent for the Endless
        val endlessModeIntent = Intent(
            this@StartMenu,
            GoNoGoActivity::class.java
        )

        endlessModeIntent.putExtra("Mode", "Endless")

        // Use the Intent to start the Endless Go-No Go Android Activity
        startActivity(endlessModeIntent)
    }

    // Set an OnClickListener for the statistics button
    // Opens up the stats page
    fun statsClick(v: View) {

        //Todo: Create an explicit Intent for starting the Statistics Page Activity
        val statsPageIntent = Intent(
            this@StartMenu,
            TaskManagerActivity::class.java
        )

        // Use the Intent to start the HelloAndroid Activity
        startActivity(statsPageIntent)


    }

    // Set an OnClickListener for the Help Button
    // Opens up the help page
    fun helpClick(v: View) {

        // Create an explicit Intent for displaying the help page Activity
        val helpPageIntent = Intent(
            this@StartMenu,
            HelpScreen::class.java
        )

        // Use the Intent to start the Help Activity
        startActivity(helpPageIntent)
    }
}