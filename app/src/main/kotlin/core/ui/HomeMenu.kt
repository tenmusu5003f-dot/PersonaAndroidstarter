package core.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import core.roadsV1_PluginRegistry
import core.roadsV1_PersonaPlugin

/**
 * HomeMenu
 * -------------------------------------------------
 * Personaプラグイン一覧を表示するUI。
 * - 登録済みプラグインの名前と説明を動的に生成
 * - タップで挨拶 or 状態確認を呼び出す簡易UI
 */
class HomeMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scroll = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val title = TextView(this).apply {
            text = "🌐 Persona Plugins"
            textSize = 22f
        }
        layout.addView(title)

        val plugins = roadsV1_PluginRegistry.all()
        if (plugins.isEmpty()) {
            val none = TextView(this).apply {
                text = "No persona plugins registered."
            }
            layout.addView(none)
        } else {
            plugins.forEach { p ->
                layout.addView(pluginItem(p))
            }
        }

        scroll.addView(layout)
        setContentView(scroll)
    }

    private fun pluginItem(plugin: roadsV1_PersonaPlugin): TextView {
        return TextView(this).apply {
            text = "🧩 ${plugin.id}"
            textSize = 18f
            setPadding(0, 16, 0, 16)
            setOnClickListener {
                val greet = plugin.onGreet()
                showToast(greet)
            }
        }
    }

    private fun showToast(msg: String) {
        android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_SHORT).show()
    }
}

package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.PluginRegistry

@Composable
fun HomeMenu() {
    val pluginList = remember { PluginRegistry.getAllPlugins() }
    var output by remember { mutableStateOf("ペルソナを選択してください。") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Persona Control Center", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(output, modifier = Modifier.padding(8.dp))

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(pluginList) { plugin ->
                Card(
                    onClick = {
                        output = PluginRegistry.handleCommand(plugin.id, "status")
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(plugin.displayName, style = MaterialTheme.typography.titleMedium)
                        Text(plugin.description, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
