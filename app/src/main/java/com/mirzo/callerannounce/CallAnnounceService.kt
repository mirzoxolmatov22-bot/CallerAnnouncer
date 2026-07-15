package com.mirzo.callerannounce

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class CallAnnounceService : Service(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var pendingNumber: String? = null
    private val channelId = "caller_announce_channel"

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        pendingNumber = intent?.getStringExtra("phone_number")
        startForeground(1, buildNotification())

        if (tts == null) {
            tts = TextToSpeech(this, this)
        } else {
            speakNumber()
        }
        return START_NOT_STICKY
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            speakNumber()
        } else {
            stopSelf()
        }
    }

    private fun speakNumber() {
        val number = pendingNumber
        if (number.isNullOrBlank()) {
            stopSelf()
            return
        }

        val uzLocale = Locale("uz", "UZ")
        val result = tts?.setLanguage(uzLocale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.language = Locale.getDefault()
        }

        val spokenText = UzbekNumberConverter.toSpokenDigits(number) +
            " raqamidan qo'ng'iroq qilinmoqda"

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) { stopSelf() }
            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) { stopSelf() }
        })

        tts?.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null, "call_announce_utterance")
    }

    private fun buildNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Qo'ng'iroq e'lonchisi", NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
        return Notification.Builder(this, channelId)
            .setContentTitle("Qo'ng'iroq raqami o'qilmoqda")
            .setSmallIcon(android.R.drawable.ic_menu_call)
            .build()
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
