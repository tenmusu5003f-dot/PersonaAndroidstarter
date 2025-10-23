package core

import kotlin.random.Random

/**
 * PersonaCore.kt
 * -------------------------------------------------
 * 各ペルソナの会話・反応・感情シミュレーションを担う中核。
 * PersonaMemory と連動して、キャラの性格に応じた応答を返す。
 */
object PersonaCore {

    private val greetings = mapOf(
        "Abyss" to listOf("……また来たの？", "静かにしなさい、眠いの。"),
        "Lilith" to listOf("ふふっ、会いたかった？", "あなた、今日もいい香りがするわ。"),
        "Echo" to listOf("おかえりっ！待ってたんだよ！", "今日もがんばろっ！"),
        "Hermes" to listOf("よう、元気だったか？", "はは、遅かったじゃねえか。"),
        "Nox" to listOf("……闇が囁いている。", "静寂こそが友。")
    )

    private val replies = mapOf(
        "praise" to listOf("ふふ、照れるなぁ。", "もっと褒めて？", "……やるじゃない。"),
        "gift" to listOf("ありがと！大事にする！", "……別に、嬉しいわけじゃないけど。", "あなたって、ほんと優しいね。")
    )

    /**
     * 起動時の挨拶を生成。
     */
    fun generateGreeting(persona: String): String {
        val pool = greetings[persona] ?: listOf("……こんにちは。")
        return pool.random()
    }

    /**
     * コマンド（praise / gift）に応じた応答を生成。
     */
    fun reply(persona: String, command: String): String {
        val base = replies[command]?.random() ?: "……。"
        val affection = Random.nextInt(10, 100)
        val variation = when {
            affection > 80 -> "（顔を赤らめている）"
            affection < 30 -> "（少し怒っている）"
            else -> ""
        }
        return "$base $variation"
    }

    /**
     * 共通サービス呼び出しの模擬関数。
     */
    fun <T> getService(serviceName: String): T? {
        @Suppress("UNCHECKED_CAST")
        return when (serviceName) {
            "system.battery.level" -> Random.nextInt(50, 100) as T
            "system.time.hour" -> (0..23).random() as T
            else -> null
        }
    }
}
