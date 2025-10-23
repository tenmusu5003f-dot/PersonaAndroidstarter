package core.config

import core.security.PersonaSecurity
import java.io.File
import kotlinx.coroutines.*
import org.json.JSONObject

/**
 * PersonaConfigManager
 * -------------------------------------------------
 * 暗号化されたユーザー設定ファイルを管理。
 * - 自動保存／自動復号
 * - ペルソナごとの設定を安全に保持
 * - バックグラウンド更新をサポート
 */
object PersonaConfigManager {

    private val scope = CoroutineScope(Dispatchers.IO)
    private const val CONFIG_FILE = "persona_config.json"

    private var cache: MutableMap<String, String> = mutableMapOf()

    /** 設定を読み込む（復号付き） */
    fun loadConfig(context: File) {
        val file = File(context, CONFIG_FILE)
        if (!file.exists()) {
            println("⚙️ No config found, creating new one.")
            saveConfig(context)
            return
        }

        try {
            val encrypted = file.readText()
            val decrypted = PersonaSecurity.decrypt(encrypted)
            val json = JSONObject(decrypted)
            cache.clear()
            json.keys().forEach {
                cache[it] = json.getString(it)
            }
            println("✅ Config loaded successfully (${cache.size} entries).")
        } catch (e: Exception) {
            println("⚠️ Failed to load config: ${e.message}")
        }
    }

    /** 設定を保存（暗号化付き） */
    fun saveConfig(context: File) {
        val json = JSONObject()
        cache.forEach { (k, v) -> json.put(k, v) }
        val encrypted = PersonaSecurity.encrypt(json.toString())
        val file = File(context, CONFIG_FILE)
        file.writeText(encrypted)
        println("💾 Config saved (${cache.size} entries).")
    }

    /** 設定を更新 */
    fun update(key: String, value: String, context: File) {
        cache[key] = value
        scope.launch {
            saveConfig(context)
        }
    }

    /** 設定を取得 */
    fun get(key: String): String? = cache[key]

    /** 全リセット */
    fun reset(context: File) {
        cache.clear()
        saveConfig(context)
        println("🧹 Persona config reset.")
    }

    /** デバッグ出力 */
    fun debugDump() {
        println("🔍 PersonaConfig: ${cache.entries.joinToString { "${it.key}=${it.value}" }}")
    }
                }
