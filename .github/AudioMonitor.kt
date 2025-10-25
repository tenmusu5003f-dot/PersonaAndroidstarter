package com.persona.core.security

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.util.Log
import android.widget.Toast

class AudioMonitor(private val context: Context) {

    private val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            when (intent?.action) {
                AudioManager.ACTION_HEADSET_PLUG,
                BluetoothDevice.ACTION_ACL_CONNECTED,
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> checkConnectedDevice()
            }
        }
    }

    fun startMonitoring() {
        val filter = IntentFilter().apply {
            addAction(AudioManager.ACTION_HEADSET_PLUG)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }
        context.registerReceiver(receiver, filter)
        Log.i("AudioMonitor", "Audio monitoring started.")
    }

    fun stopMonitoring() {
        try {
            context.unregisterReceiver(receiver)
            Log.i("AudioMonitor", "Audio monitoring stopped.")
        } catch (_: IllegalArgumentException) {
            // receiver not registered — safe to ignore
        }
    }

    private fun checkConnectedDevice() {
        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        val allowed = devices.any { device ->
            when (device.type) {
                AudioDeviceInfo.TYPE_WIRED_HEADPHONES,
                AudioDeviceInfo.TYPE_WIRED_HEADSET -> true
                else -> false
            }
        }

        if (!allowed) {
            Toast.makeText(
                context,
                "⚠️ イヤホン以外のオーディオ出力が検出されました。安全のため機能を停止します。",
                Toast.LENGTH_LONG
            ).show()
            AudioSafetyLog.record(context, "Unauthorized audio output detected – system paused.")
            disableAudioFeatures()
        } else {
            Log.i("AudioMonitor", "Authorized audio device connected.")
        }
    }

    private fun disableAudioFeatures() {
        // TODO: 音声再生・録音・AI音声通信などを安全に停止させる処理をここに記述
        // 例: AudioService.stopPlayback() / SpeechRecognizer.cancel() など
    }
}
