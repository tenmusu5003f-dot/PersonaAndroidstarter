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
