package com.mirzo.callerannounce

object UzbekNumberConverter {

    private val digitWords = mapOf(
        '0' to "nol",
        '1' to "bir",
        '2' to "ikki",
        '3' to "uch",
        '4' to "to'rt",
        '5' to "besh",
        '6' to "olti",
        '7' to "yetti",
        '8' to "sakkiz",
        '9' to "to'qqiz"
    )

    fun toSpokenDigits(phoneNumber: String): String {
        return phoneNumber
            .filter { it.isDigit() }
            .map { ch -> digitWords[ch] ?: "" }
            .filter { it.isNotEmpty() }
            .joinToString(" ")
    }
}
