package core.plugins

import core.roadsV1_PersonaPlugin
import core.roadsV1_MemoryStore
import core.roadsV1_replyFor
import core.roadsV1_randomGreeting

/**
 * NoxPlugin
 * -------------------------------------------------
 * 夜間の守護・静音モード・ログ/キャッシュ掃除を担当。
 * コマンド:
 *  - "night"    : 夜間監視開始（静音＋省電力の想定）
 *  - "clean"    : ログや一時メモリの掃除（擬似）
 *  - "status"   : 夜間ガードの状態を返す
 *  - その他     : 既定の応答（ダーク寄りの口調）
 */
class NoxPlugin : roadsV1_PersonaPlugin {

    override val id: String = "Nox"

    private var nightGuardEnabled = false
    private var muted = false

    override fun onGreet(): String {
        roadsV1_MemoryStore.remember(id, "greeted", persist = false)
        return roadsV1_randomGreeting("ノックス")
    }

    override fun onCommand(command: String, payload: String?): String {
        return when (command.lowercase()) {
            "night" -> enableNight()
            "clean" -> cleanTraces()
            "status" -> report()
            "mute" -> { muted = true; "……静寂を。通知は抑える。" }
            "unmute" -> { muted = false; "……目覚めの刻。通知を戻す。" }
            else -> roadsV1_replyFor(command, "共通")
        }
    }

    private fun enableNight(): String {
        nightGuardEnabled = true
        roadsV1_MemoryStore.remember(id, "night_on", persist = true)
        return "……夜を受け入れよう。静音と省電力を意識する。"
    }

    private fun cleanTraces(): String {
        // 擬似クリーニング：短期メモリ側の記録に“clean”イベントを残すだけ
        roadsV1_MemoryStore.remember(id, "clean_exec", persist = false)
        System.gc()
        return "……痕跡を薄めた。ログと一時資源を整理。"
    }

    private fun report(): String {
        val guard = if (nightGuardEnabled) "ON" else "OFF"
        val m = if (muted) "MUTED" else "ACTIVE"
        return "🌙 NightGuard: $guard / 通知: $m"
    }
}
