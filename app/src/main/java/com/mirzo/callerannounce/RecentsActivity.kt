package com.mirzo.callerannounce

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listView = ListView(this)
        setContentView(listView)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED
        ) {
            listView.adapter = ArrayAdapter(
                this, android.R.layout.simple_list_item_1,
                listOf("Qo'ng'iroqlar jurnaliga ruxsat berilmagan")
            )
            return
        }

        val entries = mutableListOf<String>()
        val numbers = mutableListOf<String>()

        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.TYPE),
            null, null,
            CallLog.Calls.DATE + " DESC"
        )

        val dateFormat = SimpleDateFormat("dd-MMM HH:mm", Locale.getDefault())

        cursor?.use {
            var count = 0
            while (it.moveToNext() && count < 100) {
                val number = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.NUMBER)) ?: "Noma'lum"
                val date = it.getLong(it.getColumnIndexOrThrow(CallLog.Calls.DATE))
                val type = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.TYPE))

                val typeLabel = when (type) {
                    CallLog.Calls.INCOMING_TYPE -> "Kelgan"
                    CallLog.Calls.OUTGOING_TYPE -> "Ketgan"
                    CallLog.Calls.MISSED_TYPE -> "O'tkazib yuborilgan"
                    else -> "Boshqa"
                }

                numbers.add(number)
                entries.add("$number\n$typeLabel  -  ${dateFormat.format(Date(date))}")
                count++
            }
        }

        if (entries.isEmpty()) {
            entries.add("Qo'ng'iroqlar tarixi bo'sh")
        }

        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, entries)
        listView.setOnItemClickListener { _, _, position, _ ->
            if (position < numbers.size) {
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:${numbers[position]}")))
            }
        }
    }
}
