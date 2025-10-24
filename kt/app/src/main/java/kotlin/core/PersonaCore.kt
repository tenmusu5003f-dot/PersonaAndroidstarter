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

package core.plugins

import core.Capability

/**
 * IkarugaPlugin
 * - Persona プラグインのサンプル実装。
 * - プラグインは Capability を宣言し、PersonaCore に登録されて利用される。
 */
class IkarugaPlugin : PersonaPlugin {

  override val id: String = "ikaruga"
  override val displayName: String = "斑鳩"
  override val description: String = "静寂の心を持つペルソナ。音声や状態制御を担当する。"

  override fun getCapabilities(): List<Capability> = listOf(
    Capability.Greeting,
    Capability.Status
  )

  override fun onActivate() {
    println("[$displayName] 起動完了。")
  }

  override fun onDeactivate() {
    println("[$displayName] 停止。")
  }

  override fun execute(capability: Capability, input: String?): String {
    return when (capability) {
      Capability.Greeting -> "……こんばんは。準備はできています。"
      Capability.Status -> "システム稼働率：100%。エラーなし。"
    }
  }
}

/**
 * PersonaPlugin ベースインターフェース。
 * どのペルソナもこれを実装する。
 */
interface PersonaPlugin {
  val id: String
  val displayName: String
  val description: String

  fun getCapabilities(): List<Capability>
  fun onActivate()
  fun onDeactivate()
  fun execute(capability: Capability, input: String? = null): String
}


package core

/**
 * アプリ全体の軽量なコア。ここで「撮影可否」などのグローバル方針を握る。
 * UI側は PersonaCore.onScreenshotAttempt() を見て分岐するだけ。
 */
object PersonaCore {
    @Volatile
    private var allowScreenshots: Boolean = false

    fun enableScreenshots() { allowScreenshots = true }
    fun disableScreenshots() { allowScreenshots = false }

    /** スクショを試みる前に呼ぶ。trueなら許可、falseなら抑止UIへ。 */
    fun onScreenshotAttempt(): Boolean = allowScreenshots
}
