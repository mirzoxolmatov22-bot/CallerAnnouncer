package com.mirzo.callerannounce

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView

    private val requiredPermissions = mutableListOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.WRITE_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ANSWER_PHONE_CALLS
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }.toTypedArray()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { refreshStatus() }

    private val defaultDialerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { refreshStatus() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 100, 40, 40)
        }

        statusText = TextView(this).apply {
            textSize = 16f
            setPadding(0, 0, 0, 40)
        }
        root.addView(statusText)

        val permButton = Button(this).apply {
            text = "Ruxsatlarni berish"
            setOnClickListener { permissionLauncher.launch(requiredPermissions) }
        }
        root.addView(permButton)

        val defaultDialerButton = Button(this).apply {
            text = "Standart dialer sifatida tayinlash"
            setOnClickListener { requestDefaultDialer() }
        }
        root.addView(defaultDialerButton)

        val spacer = TextView(this).apply { setPadding(0, 40, 0, 0) }
        root.addView(spacer)

        val dialPadButton = Button(this).apply {
            text = "Raqam terish"
            setOnClickListener { startActivity(Intent(this@MainActivity, DialPadActivity::class.java)) }
        }
        root.addView(dialPadButton)

        val recentsButton = Button(this).apply {
            text = "So'nggi qo'ng'iroqlar"
            setOnClickListener { startActivity(Intent(this@MainActivity, RecentsActivity::class.java)) }
        }
        root.addView(recentsButton)

        val contactsButton = Button(this).apply {
            text = "Kontaktlar"
            setOnClickListener { startActivity(Intent(this@MainActivity, ContactsActivity::class.java)) }
        }
        root.addView(contactsButton)

        setContentView(root)
        refreshStatus()
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    private fun requestDefaultDialer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
            if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER)) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                defaultDialerLauncher.launch(intent)
            }
        } else {
            val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
            intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
            defaultDialerLauncher.launch(intent)
        }
    }

    private fun isDefaultDialer(): Boolean {
        val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        return telecomManager.defaultDialerPackage == packageName
    }

    private fun hasAllPermissi
