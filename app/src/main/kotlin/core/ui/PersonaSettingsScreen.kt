package core.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.config.PersonaConfigManager

/**
 * PersonaSettingsScreen
 * -------------------------------------------------
 * ユーザー設定画面（暗号化コンフィグに保存）
 * - テーマ（ライト/ダーク/自動）
 * - 起動演出ON/OFF
 * - バックグラウンド学習ON/OFF
 * - 省データモードON/OFF
 *
 * ※ 保存は PersonaConfigManager.update() で暗号化ファイルへ反映
 */
@Composable
fun PersonaSettingsScreen(
    appFilesDirPath: String, // context.filesDir.absolutePath を渡す想定
    onBack: () -> Unit
) {
    val cfgDir = java.io.File(appFilesDirPath)
    // 1回だけロード
    LaunchedEffect(Unit) { PersonaConfigManager.loadConfig(cfgDir) }

    // ローカル状態（初期値は保存済み値 or 既定値）
    var themeMode by remember { mutableStateOf(PersonaConfigManager.get("theme") ?: "system") }
    var opening by remember { mutableStateOf((PersonaConfigManager.get("opening") ?: "on") == "on") }
    var bgLearn by remember { mutableStateOf((PersonaConfigManager.get("bg_learn") ?: "on") == "on") }
    var dataSaver by remember { mutableStateOf((PersonaConfigManager.get("data_saver") ?: "off") == "on") }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Settings", style = MaterialTheme.typography.titleLarge)
                TextButton(onClick = onBack) { Text("Back") }
            }

            // Theme mode
            Text("Theme", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeChoice("system", themeMode) { new ->
                    themeMode = new
                    PersonaConfigManager.update("theme", new, cfgDir)
                }
                ThemeChoice("light", themeMode) { new ->
                    themeMode = new
                    PersonaConfigManager.update("theme", new, cfgDir)
                }
                ThemeChoice("dark", themeMode) { new ->
                    themeMode = new
                    PersonaConfigManager.update("theme", new, cfgDir)
                }
            }

            // Opening effect
            SettingSwitch(
                title = "Opening Effects",
                checked = opening,
                onCheckedChange = {
                    opening = it
                    PersonaConfigManager.update("opening", if (it) "on" else "off", cfgDir)
                }
            )

            // Background learning
            SettingSwitch(
                title = "Background Learning",
                checked = bgLearn,
                onCheckedChange = {
                    bgLearn = it
                    PersonaConfigManager.update("bg_learn", if (it) "on" else "off", cfgDir)
                }
            )

            // Data saver
            SettingSwitch(
                title = "Data Saver",
                checked = dataSaver,
                onCheckedChange = {
                    dataSaver = it
                    PersonaConfigManager.update("data_saver", if (it) "on" else "off", cfgDir)
                }
            )

            Spacer(Modifier.weight(1f))

            // Debug dump (開発時のみ)
            TextButton(onClick = { PersonaConfigManager.debugDump() }) {
                Text("Debug: Dump Config")
            }
        }
    }
}

@Composable
private fun ThemeChoice(
    label: String,
    current: String,
    onSelect: (String) -> Unit
) {
    val selected = current == label
    FilterChip(
        selected = selected,
        onClick = { onSelect(label) },
        label = { Text(label.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }) }
    )
}

@Composable
private fun SettingSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleSmall)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
