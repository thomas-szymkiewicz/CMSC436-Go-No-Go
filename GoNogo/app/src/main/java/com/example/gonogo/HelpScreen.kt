package com.example.gonogo

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

class HelpScreen : Activity(){
    private lateinit var returnButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {

        // Required call through to Activity.onCreate()
        // Restore any saved instance state
        super.onCreate(savedInstanceState)

        // Set up the application's user interface (content view)
        setContentView(R.layout.helpscreen)

        returnButton = findViewById(R.id.back_button)

    }

    // Set an OnClickListener for the standard Go-No Go
    // Starts the standard activity
    fun backClick(v: View) {

        // Go back to previous activity on the stack
        finish()

    }
}