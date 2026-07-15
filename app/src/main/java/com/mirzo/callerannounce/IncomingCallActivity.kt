package com.mirzo.callerannounce

import android.graphics.Color
import android.os.Bundle
import android.telecom.Call
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class IncomingCallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        val number = intent.getStringExtra("phone_number") ?: "Noma'lum raqam"

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#1B1B1B"))
            setPadding(60, 60, 60, 60)
        }

        val title = TextView(this).apply {
            text = "Kiruvchi qo'ng'iroq"
            textSize = 20f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val numberView = TextView(this).apply {
            text = number
            textSize = 32f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 40, 0, 80)
        }

        val buttonRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        val answerButton = Button(this).apply {
            text = "Jav
