package core.plugins

import core.roadsV1_PersonaPlugin
import core.roadsV1_MemoryStore
import core.roadsV1_replyFor
import core.roadsV1_randomGreeting

/**
 * HermesPlugin
 * -------------------------------------------------
 * システム通信・最適化を司る「伝令AI」。
 * - 起動時にシステムの状態をチェックして報告
 * - コマンドでパフォーマンス最適化を実施
 * - DuplicateGuard 経由で登録され、冗長化を防ぐ
 */
class HermesPlugin : roadsV1_PersonaPlugin {

    override val id: String = "Hermes"

    override fun onGreet(): String {
        roadsV1_MemoryStore.remember(id, "greeted", persist = false)
        return "伝令、ヘルメス。システムは安定稼働中です。"
    }

    override fun onCommand(command: String, payload: String?): String {
        return when (command.lowercase()) {
            "status" -> checkStatus()
            "optimize" -> optimize()
            else -> roadsV1_replyFor(command, "Hermes")
        }
    }

    private fun checkStatus(): String {
        val memoryUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        val kb = memoryUsed / 1024
        return "📡 現在のメモリ使用量: ${kb}KB\nシステムは正常に稼働中。"
    }

    private fun optimize(): String {
        System.gc()
        return "⚙️ 最適化完了。不要なリソースを解放しました。"
    }
}

package core.plugins

import core.roadsV1_PersonaPlugin
import core.roadsV1_MemoryStore
import core.roadsV1_replyFor
import core.roadsV1_RandomGreeting

/**
 * HermesPlugin
 *
 * システム通信・最適化を司る伝令AI。
 * 起動時にシステムの状態をチェックして報告。
 * コマンド：
 *  "status" -> システム状態を確認
 *  "optimize" -> リソースを開放して最適化
 */
class HermesPlugin : roadsV1_PersonaPlugin {

    override val id: String = "ヘルメス「"

    override fun onGreet(): String {
        roadsV1_MemoryStore.remember(id, "挨拶した")
        return roadsV1_RandomGreeting("ヘルメス「")
    }

    override fun onCommand(command: String, payload: String): String {
        return when (command.lowercase()) {
            "status" -> checkSystemStatus()
            "optimize" -> optimizeSystem()
            else -> roadsV1_replyFor(command, "共通")
        }
    }

    private fun checkSystemStatus(): String {
        val runtime = Runtime.getRuntime()
        val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024
        return "📡 現在のメモリ使用量: ${usedMemory}KB。システムは正常に稼働中です。"
    }

    private fun optimizeSystem(): String {
        System.gc()
        return "⚙️ 最適化完了。不要なリソースを解放しました。"
    }
}
