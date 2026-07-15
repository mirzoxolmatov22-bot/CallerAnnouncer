package com.mirzo.callerannounce

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class DialPadActivity : AppCompatActivity() {

    private lateinit var numberField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 80, 40, 40)
        }

        numberField = EditText(this).apply {
            textSize = 28f
            gravity = Gravity.CENTER
            hint = "Raqamni tering"
            isFocusable = false
        }
        root.addView(numberField)

        val grid = GridLayout(this).apply {
            columnCount = 3
            rowCount = 4
            setPadding(0, 60, 0, 60)
        }

        val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")
        for (key in keys) {
            val button = Button(this).apply {
                text = key
                textSize = 22f
                setOnClickListener { numberField.append(key) }
            }
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }
            grid.addView(button, params)
        }
        root.addView(grid)

        val actionRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        val callButton = Button(this).apply {
            text = "Qo'ng'iroq qilish"
            setOnClickListener {
                val number = numberField.text.toString()
                if (number.isNotBlank()) {
                    startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")))
                }
            }
        }

        val clearButton = Button(this).apply {
            text = "O'chirish"
            setOnClickListener {
                val text = numberField.text.toString()
                if (text.isNotEmpty()) {
                    numberField.setText(text.substring(0, text.length - 1))
                    numberField.setSelection(numberField.text.length)
                }
            }
        }

        actionRow.addView(clearButton)
        actionRow.addView(callButton)
        root.addView(actionRow)

        setContentView(root)
    }
}
