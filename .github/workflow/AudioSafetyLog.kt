package com.persona.core.security

import android.content.Context
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object AudioSafetyLog {

    private const val TAG = "AudioSafety"
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun record(context: Context, message: String) {
        val timestamp = dateFormat.format(Date())
        val logMessage = "[$timestamp] $message\n"
        Log.w(TAG, logMessage)

        try {
            val logFile = File(context.filesDir, "audio_safety_log.txt")
            logFile.appendText(logMessage)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write safety log", e)
        }
    }

    fun reportLastEntries(context: Context, lines: Int = 10): String {
        val logFile = File(context.filesDir, "audio_safety_log.txt")
        if (!logFile.exists()) return "(no logs yet)"
        return logFile.readLines().takeLast(lines).joinToString("\n")
    }
}
