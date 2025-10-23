// region ro
adsV1_osbridge
object roadsV1_OSBridge {
    fun greet(persona: String): String = roadsV1_randomGreeting(persona)
    fun command(persona: String, cmd: String, payload: String? = null): String =
        roadsV1_PluginRegistry.get(persona)?.onCommand(cmd, payload)
            ?: roadsV1_replyFor(cmd, "共通")
}
// endregion

package core

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

/**
 * PersonaOSBridge
 * -------------------------------------------------
 * OSのイベントをPersonaCoreへ橋渡しする。
 * - アプリのライフサイクル
 * - 電源・バッテリー・画面ON/OFF
 * - OSアップデート通知や安全終了など
 */
object PersonaOSBridge : DefaultLifecycleObserver {

    private var app: Application? = null
    private var receiverRegistered = false

    fun attach(app: Application) {
        this.app = app

        // アプリ全体のライフサイクルを監視
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        // OSブロードキャスト受信登録
        registerSystemReceivers(app)
    }

    private fun registerSystemReceivers(app: Application) {
        if (receiverRegistered) return

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SHUTDOWN)
        }

        app.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                handleBroadcast(intent)
            }
        }, filter)

        receiverRegistered = true
    }

    private fun handleBroadcast(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BATTERY_CHANGED -> {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                PersonaCore.registerService("system.battery.level", level)
            }
            Intent.ACTION_SCREEN_ON -> {
                PersonaCore.registerService("system.screen.on", true)
            }
            Intent.ACTION_SCREEN_OFF -> {
                PersonaCore.registerService("system.screen.on", false)
            }
            Intent.ACTION_SHUTDOWN -> {
                PersonaCore.shutdown()
            }
        }
    }

    // ---- アプリライフサイクル ----

    override fun onStart(owner: LifecycleOwner) {
        log("App visible")
        PersonaCore.registerService("app.state", "foreground")
    }

    override fun onStop(owner: LifecycleOwner) {
        log("App hidden")
        PersonaCore.registerService("app.state", "background")
    }

    // ---- OSアップデート通知 ----

    fun osInfo(): Map<String, String> = mapOf(
        "version" to Build.VERSION.RELEASE,
        "sdk" to Build.VERSION.SDK_INT.toString(),
        "device" to Build.MODEL,
        "brand" to Build.BRAND
    )

    private fun log(msg: String) {
        println("[PersonaOSBridge] $msg")
    }
}

package core

/**
 * PersonaOS と Android OS の橋渡しを行うモジュール。
 * - センサー情報やシステムイベントを監視し、PersonaCore に転送
 * - 端末依存の処理を抽象化して提供
 */
object PersonaOSBridge {

    fun initialize() {
        // TODO: センサーやシステムイベントの購読を開始する
    }

    fun sendSystemEvent(event: String, data: Map<String, Any>? = null) {
        // TODO: PersonaCore へ中継する
        println("SystemEvent: $event data=$data")
    }

    fun shutdown() {
        // TODO: 登録解除などのクリーンアップ
    }
}
