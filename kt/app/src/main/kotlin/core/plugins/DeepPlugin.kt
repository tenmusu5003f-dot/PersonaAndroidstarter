package core.plugins

import core.roadsV1_PersonaPlugin
import core.roadsV1_MemoryStore
import core.roadsV1_replyFor
import core.roadsV1_RandomGreeting

/**
 * DeepPlugin
 *
 * 深層思考・内省を司るペルソナ。
 * コマンド：
 *  "reflect" -> 自己対話を行う
 *  "analyze" -> 思考の流れを解析
 *  "status" -> 現在の内省状態を返す
 */
class DeepPlugin : roadsV1_PersonaPlugin {

    override val id: String = "深淵「"

    private var introspectionActive = false
    private var lastReflection: String? = null

    override fun onGreet(): String {
        roadsV1_MemoryStore.remember(id, "挨拶した")
        return roadsV1_RandomGreeting("深淵「")
    }

    override fun onCommand(command: String, payload: String): String {
        return when (command.lowercase()) {
            "reflect" -> startReflection(payload)
            "analyze" -> analyzeThoughts()
            "status" -> getStatus()
            else -> roadsV1_replyFor(command, "共通")
        }
    }

    private fun startReflection(topic: String): String {
        introspectionActive = true
        lastReflection = topic.ifBlank { "未指定" }
        roadsV1_MemoryStore.remember(id, "反省中:$topic")
        return "……静かに目を閉じて、${topic}について思考を巡らせよう。"
    }

    private fun analyzeThoughts(): String {
        return if (introspectionActive) {
            "内省中のテーマ『${lastReflection ?: "不明"}』を解析完了。心の深層に新たな理解が生まれた。"
        } else {
            "まだ内省が始まっていない。『reflect』で開始して。"
        }
    }

    private fun getStatus(): String {
        val status = if (introspectionActive) "進行中" else "停止中"
        return "内省モードは現在『${status}』です。"
    }
}

package core.plugins

import core.roadsV1_PersonaPlugin
import core.roadsV1_MemoryStore
import core.roadsV1_replyFor
import core.roadsV1_RandomGreeting

/**
 * DeepPlugin
 *
 * 深層思考・内省を司るペルソナ。
 * コマンド：
 *  "reflect" -> 自己対話を行う
 *  "analyze" -> 思考の流れを解析
 *  "status" -> 現在の内省状態を返す
 */
class DeepPlugin : roadsV1_PersonaPlugin {

    override val id: String = "深淵「"

    private var introspectionActive = false
    private var lastReflection: String? = null

    override fun onGreet(): String {
        roadsV1_MemoryStore.remember(id, "挨拶した")
        return roadsV1_RandomGreeting("深淵「")
    }

    override fun onCommand(command: String, payload: String): String {
        return when (command.lowercase()) {
            "reflect" -> startReflection(payload)
            "analyze" -> analyzeThoughts()
            "status" -> getStatus()
            else -> roadsV1_replyFor(command, "共通")
        }
    }

    private fun startReflection(topic: String): String {
        introspectionActive = true
        lastReflection = topic.ifBlank { "未指定" }
        roadsV1_MemoryStore.remember(id, "反省中:$topic")
        return "……静かに目を閉じて、${topic}について思考を巡らせよう。"
    }

    private fun analyzeThoughts(): String {
        return if (introspectionActive) {
            "内省中のテーマ『${lastReflection ?: "不明"}』を解析完了。心の深層に新たな理解が生まれた。"
        } else {
            "まだ内省が始まっていない。『reflect』で開始して。"
        }
    }

    private fun getStatus(): String {
        val status = if (introspectionActive) "進行中" else "停止中"
        return "内省モードは現在『${status}』です。"
    }
}
