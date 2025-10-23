package core

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * PersonaMemory
 * -------------------------------------------------
 * シンプルかつ安全なデータ永続化レイヤー。
 * - 設定や内部状態を保存
 * - 高速キャッシュとしても機能
 * - RoomやFirebaseより軽量で、端末負荷が低い
 */

object PersonaMemory {

    private const val STORE_NAME = "persona_memory"
    private lateinit var context: Context
    private val Context.dataStore by preferencesDataStore(name = STORE_NAME)

    private var initialized = false

    fun initialize(ctx: Context) {
        if (initialized) return
        context = ctx.applicationContext
        initialized = true
    }

    fun isReady(): Boolean = initialized

    /** 🔹 値を保存 */
    fun putString(key: String, value: String) = runBlocking {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { prefs ->
            prefs[dataStoreKey] = value
        }
    }

    /** 🔹 値を取得（非同期で監視） */
    fun getStringFlow(key: String): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(key)
        return context.dataStore.data.map { prefs ->
            prefs[dataStoreKey]
        }
    }

    /** 🔹 即座に値を取得（同期ブロック） */
    fun getStringNow(key: String): String? = runBlocking {
        var result: String? = null
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.data.map { prefs ->
            prefs[dataStoreKey]
        }.collect { result = it }
        result
    }

    /** 🔹 全データ削除 */
    fun clear() = runBlocking {
        context.dataStore.edit { it.clear() }
    }

    private fun log(msg: String) {
        println("[PersonaMemory] $msg")
    }
}
