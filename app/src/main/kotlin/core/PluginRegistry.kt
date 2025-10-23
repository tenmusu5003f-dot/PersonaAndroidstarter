package core

import core.plugins.*

/**
 * PluginRegistry
 *
 * PersonaCore と各プラグインを連結する中継ハブ。
 * 各ペルソナAIの登録・呼び出し・コマンド実行を管理。
 */
object PluginRegistry {

    private val plugins: MutableMap<String, roadsV1_PersonaPlugin> = mutableMapOf()

    init {
        // 起動時に主要ペルソナを登録
        register(HermesPlugin())
        register(NoxPlugin())
        register(DeepPlugin())
    }

    fun register(plugin: roadsV1_PersonaPlugin) {
        plugins[plugin.id] = plugin
    }

    fun getAllPlugins(): List<roadsV1_PersonaPlugin> = plugins.values.toList()

    fun findById(id: String): roadsV1_PersonaPlugin? = plugins[id]

    fun handleCommand(id: String, command: String, payload: String = ""): String {
        return plugins[id]?.onCommand(command, payload)
            ?: "⚠️ ペルソナが見つかりません: $id"
    }
}
