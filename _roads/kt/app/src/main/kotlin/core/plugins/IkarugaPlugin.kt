
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
