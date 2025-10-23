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
