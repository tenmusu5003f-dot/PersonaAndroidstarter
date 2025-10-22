package com.example.persona

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.util.LinkedHashMap

// ============ 入口（ゲート） ============
class PersonaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Gate.boot(this)
    }
}
object Gate {
    private var booted = false
    lateinit var cfg: JSONObject
        private set
    fun boot(ctx: Context) {
        if (booted) return
        cfg = JSONObject(readAsset(ctx, "config.json"))
        booted = true
    }
    fun isOpen() = booted
    private fun readAsset(ctx: Context, path: String): String =
        ctx.assets.open(path).bufferedReader().use(BufferedReader::readText)
}

// ============ データモデル ============
data class Persona(
    val id: String,
    val displayName: String,
    val role: String,
    val tone: String,
    val formality: String,
    val capabilities: List<String>,
    val color: String?,
    val icon: String?,
    val bgm: String?,
    val prompts: Map<String, String>
)

// ============ キャッシュ（LRU・メモリ） ============
class LruCache<K, V>(private val maxSize: Int) : LinkedHashMap<K, V>(16, 0.75f, true) {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean = size > maxSize
}

// ============ マネージャ（単一JSONから全員ロード） ============
object Personas {
    private const val ASSET = "personas.json"
    private var loaded = false
    private val list = mutableListOf<Persona>()
    private lateinit var prefs: SharedPreferences
    private lateinit var cache: LruCache<String, String> // key: personaId:key

    fun ensure(ctx: Context) {
        if (loaded) return
        require(Gate.isOpen()) { "Gate closed" }
        prefs = ctx.getSharedPreferences("persona_mem", Context.MODE_PRIVATE)
        cache = LruCache(maxSize = Gate.cfg.getJSONObject("cache").getInt("memoryLimit"))

        val root = JSONObject(readAsset(ctx, ASSET))
        val arr = root.getJSONArray("personas")
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val style = o.getJSONObject("style")
            val theme = o.optJSONObject("theme")
            val prompts = o.optJSONObject("prompts") ?: JSONObject()
            list += Persona(
                id = o.getString("id"),
                displayName = o.getString("displayName"),
                role = o.getString("role"),
                tone = style.getString("tone"),
                formality = style.getString("formality"),
                capabilities = o.getJSONArray("capabilities").toList(),
                color = theme?.optString("color"),
                icon = theme?.optString("icon"),
                bgm = theme?.optString("bgm"),
                prompts = prompts.toMap()
            )
        }
        loaded = true
    }

    fun all(ctx: Context): List<Persona> { ensure(ctx); return list }
    fun get(ctx: Context, id: String): Persona? { ensure(ctx); return list.find { it.id == id } }

    // 簡易メモリ（SharedPreferencesにまとめて書く＝ファイル増やさない）
    fun remember(id: String, key: String, value: String) {
        val k = "$id:$key"
        cache[k] = value
        prefs.edit().putString(k, value).apply()
    }
    fun recall(id: String, key: String): String? {
        val k = "$id:$key"
        return cache[k] ?: prefs.getString(k, null)?.also { cache[k] = it }
    }

    private fun readAsset(ctx: Context, path: String): String =
        ctx.assets.open(path).bufferedReader().use(BufferedReader::readText)
}

// ============ 拡張：JSONArray/JSONObjectユーティリティ ============
private fun JSONArray.toList(): List<String> = buildList {
    for (i in 0 until length()) add(getString(i))
}
private fun JSONObject.toMap(): Map<String, String> = buildMap {
    val it = keys()
    while (it.hasNext()) {
        val k = it.next()
        put(k, getString(k))
    }
}

// ============ 画面（最小UI） ============
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PersonaHome() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonaHome() {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    Personas.ensure(ctx)
    val items = remember { Personas.all(ctx) }
    var idx by remember { mutableStateOf(0) }
    val p = items.getOrNull(idx)
    var status by remember { mutableStateOf("入口OK / ${items.size}人") }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Persona Entrance") }) }) { pad ->
        Surface(Modifier.padding(pad).fillMaxSize()) {
            Column(Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                if (items.isNotEmpty()) {
                    // 超シンプルな切替（左右ボタン）
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = { if (idx > 0) idx-- }) { Text("←") }
                        Text(p?.displayName ?: "-")
                        Button(onClick = { if (idx < items.lastIndex) idx++ }) { Text("→") }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("役割: ${p?.role ?: "-"} / 口調: ${p?.tone ?: "-"}")
                } else {
                    Text("personas.json に誰もいません")
                }

                Spacer(Modifier.height(16.dp))
                Text(status)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = {
                        p?.let { Personas.remember(it.id, "last_greet", it.prompts["greet"] ?: "こんにちは") }
                        status = "保存: last_greet"
                    }) { Text("保存") }
                    Button(onClick = {
                        p?.let { status = "復元: " + (Personas.recall(it.id, "last_greet") ?: "なし") }
                    }) { Text("復元") }
                }
            }
        }
    }
}
