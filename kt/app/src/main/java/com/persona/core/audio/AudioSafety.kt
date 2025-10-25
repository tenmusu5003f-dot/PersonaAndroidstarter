package com.persona.androidstarter.core.audio

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager

object AudioSafety {
    fun isHeadphonesConnected(ctx: Context): Boolean {
        val am = ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val outs = am.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        return outs.any { dev ->
            when (dev.type) {
                AudioDeviceInfo.TYPE_WIRED_HEADPHONES,
                AudioDeviceInfo.TYPE_WIRED_HEADSET,
                AudioDeviceInfo.TYPE_BLUETOOTH_A2DP,
                AudioDeviceInfo.TYPE_BLUETOOTH_SCO,
                AudioDeviceInfo.TYPE_USB_HEADSET -> true
                else -> false
            }
        }
    }

    /** イヤホンなしなら強制的に無音ルートを選ぶ（TTS/音出し側でこれを参照） */
    fun enforceMuteIfNoHeadphones(ctx: Context, am: AudioManager? = null) {
        val audio = am ?: (ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
        if (!isHeadphonesConnected(ctx)) {
            // ストリーム音量を0に近づける（必要に応じて元音量の記憶も）
            audio.adjustVolume(AudioManager.ADJUST_MUTE, 0)
        }
    }
}
