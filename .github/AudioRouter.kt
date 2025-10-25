// package は実際のものに合わせて変更してね
package com.persona.androidstarter.core

import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build

object AudioRouter {

    // ヘッドセット／イヤホン系と見なすデバイスタイプ集合
    private val allowedTypes = setOf(
        AudioDeviceInfo.TYPE_WIRED_HEADSET,       // ヘッドセット（マイク付き有線）
        AudioDeviceInfo.TYPE_WIRED_HEADPHONES,   // イヤホン/ヘッドホン（有線）
        AudioDeviceInfo.TYPE_BLUETOOTH_A2DP,     // Bluetooth (A2DP)
        AudioDeviceInfo.TYPE_BLUETOOTH_SCO,      // Bluetooth SCO (通話)
        AudioDeviceInfo.TYPE_HEARING_AID          // 補聴器など（必要なら許可）
    )

    /** 現在、出力先に"イヤホン／ヘッドセット(許可対象)"が含まれているか */
    fun isAllowedHeadsetConnected(context: Context): Boolean {
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val outs = am.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            if (outs.isEmpty()) return false
            return outs.any { allowedTypes.contains(it.type) }
        } else {
            // 古いAPI: 有線ヘッドセットのみ簡易検知
            return am.isWiredHeadsetOn || am.isBluetoothA2dpOn
        }
    }

    /**
     * ランタイムで接続変更を監視するコールバックを返す。
     * Activity/Service 側で登録・解除して使ってね。
     */
    fun createDeviceCallback(onChange: () -> Unit): AudioDeviceCallback =
        object : AudioDeviceCallback() {
            override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>?) {
                onChange()
            }
            override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
                onChange()
            }
        }
}
