// region roadsV1_memory
data class roadsV1_MemoryItem(
    val persona: String,
    val type: String,
    val content: String,
    val ts: Long = System.currentTimeMillis()
)

object roadsV1_MemoryStore {
    private val buf = mutableListOf<roadsV1_MemoryItem>()
    @Synchronized fun log(item: roadsV1_MemoryItem) { buf += item }
    @Synchronized fun last(n: Int = 10, persona: String? = null): List<roadsV1_MemoryItem> =
        buf.asReversed().filter { persona == null || it.persona == persona }.take(n)
}
// endregion

package core

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf

/**
 * PersonaMemory.kt
 * -------------------------------------------------
 * 各ペルソナの記録・好感度・体力などを簡易永続化するクラス。
 * SharedPreferences ベースで軽量・安全。
 * オフラインでも利用可能。
 */
class PersonaMemory(private val personaName: String) {

    private val prefs by lazy {
        appContext.getSharedPreferences("persona_memory", Context.MODE_PRIVATE)
    }

    private val cache = mutableStateMapOf<String, Int>()

    fun getStat(key: String): Int {
        return cache[key] ?: prefs.getInt("$personaName.$key", 50).also {
            cache[key] = it
        }
    }

    fun updateStat(key: String, delta: Int) {
        val newValue = (getStat(key) + delta).coerceIn(0, 100)
        cache[key] = newValue
        prefs.edit().putInt("$personaName.$key", newValue).apply()
    }

    fun clearAll() {
        prefs.edit().apply {
            prefs.all.keys.filter { it.startsWith("$personaName.") }.forEach { remove(it) }
        }.apply()
        cache.clear()
    }

    companion object {
        /**
         * PersonaMemory 全体を初期化。
         * Application から最初に appContext を設定しておくこと。
         */
        lateinit var appContext: Context
            private set

        fun init(context: Context) {
            appContext = context.applicationContext
        }
    }
}

package core

/**
 * PersonaMemory.kt
 * ペルソナの短期・長期記憶を管理します。
 * 各ペルソナごとにコンテキストと履歴を保持し、
 * roadsV1_PluginRegistry で登録されたプラグインから参照されます。
 */

object roadsV1_MemoryStore {
    private val shortTerm = mutableMapOf<String, MutableList<String>>()
    private val longTerm = mutableMapOf<String, MutableList<String>>()

    fun remember(personaId: String, message: String, persist: Boolean = false) {
        val target = if (persist) longTerm else shortTerm
        val list = target.getOrPut(personaId) { mutableListOf() }
        list += message
    }

    fun recall(personaId: String, limit: Int = 5, fromLongTerm: Boolean = false): List<String> {
        val source = if (fromLongTerm) longTerm else shortTerm
        return source[personaId]?.takeLast(limit) ?: emptyList()
    }

    fun forget(personaId: String, all: Boolean = false) {
        if (all) {
            shortTerm.remove(personaId)
            longTerm.remove(personaId)
        } else {
            shortTerm[personaId]?.clear()
        }
    }

    fun exportSnapshot(personaId: String): Map<String, List<String>> {
        return mapOf(
            "shortTerm" to (shortTerm[personaId] ?: emptyList()),
            "longTerm" to (longTerm[personaId] ?: emptyList())
        )
    }
}
