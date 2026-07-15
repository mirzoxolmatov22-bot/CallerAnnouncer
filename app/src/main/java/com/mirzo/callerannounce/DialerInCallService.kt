package com.mirzo.callerannounce

import android.content.Intent
import android.os.Build
import android.telecom.Call
import android.telecom.InCallService

class DialerInCallService : InCallService() {

    companion object {
        var currentCall: Call? = null
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        currentCall = call

        val number = call.details?.handle?.schemeSpecificPart ?: ""

        if (call.state == Call.STATE_RINGING) {
            announceNumber(number)
            showIncomingCallScreen(number)
        }

        call.registerCallback(object : Call.Callback() {
            override fun onStateChanged(call: Call, state: Int) {
                if (state == Call.STATE_DISCONNECTED) {
                    if (currentCall == call) currentCall = null
                }
            }
        })
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        if (currentCall == call) currentCall = null
    }

    private fun announceNumber(number: String) {
        if (number.isBlank()) return
        val intent = Intent(this, CallAnnounceService::class.java)
        intent.putExtra("phone_number", number)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun showIncomingCallScreen(number: String) {
        val intent = Intent(this, IncomingCallActivity::class.java)
        intent.putExtra("phone_number", number)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
