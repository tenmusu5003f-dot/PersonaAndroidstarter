package core.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import core.*
import core.ui.theme.PersonaTheme

/**
 * PersonaMainScreen
 * -------------------------------------------------
 * アプリの顔。起動演出 → ホーム（一覧）→ 各ペルソナルーム へ遷移。
 * - OpeningEffects を安全ティアに応じて再生
 * - HomeMenu で選択された名前を Room に引き渡す
 */
@Composable
fun PersonaMainScreen(
    onNavigateSettings: () -> Unit
) {
    PersonaTheme {
        var showSplash by remember { mutableStateOf(true) }
        var currentPersona by remember { mutableStateOf<String?>(null) }

        val os = remember { PersonaOSBridge.osInfo() }
        val battery = PersonaCore.getService<Int>("system.battery.level")
        val sec = PersonaSecurity.evaluate(
            app = (LocalAppProvider.current ?: return)
        )
        val isSafe = sec.signatureValid && !sec.debuggerAttached && !sec.suspiciousBuild
        val tier = remember(os, battery, isSafe) {
            EffectPolicy.autoAdjust(os, isSafe, battery)
        }

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            when {
                showSplash -> {
                    // ロゴが無い場合は null でOK（フォールバックあり）
                    val logo = runCatching { painterResource(id = android.R.drawable.sym_def_app_icon) }.getOrNull()
                    OpeningEffects.Play(
                        tier = tier,
                        logo = logo,
                        onEnd = { showSplash = false }
                    )
                }

                currentPersona == null -> {
                    // ホーム（ペルソナ一覧）
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        HomeMenu(
                            personas = listOf("Abyss", "Lilith", "Echo", "Hermes", "Nox")
                        ) { selected ->
                            currentPersona = selected
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = onNavigateSettings) {
                                Text("Settings")
                            }
                        }
                    }
                }

                else -> {
                    // ルーム
                    PersonaRoom(
                        personaName = currentPersona!!,
                        onBack = { currentPersona = null }
                    )
                }
            }
        }
    }
}

/**
 * Application コンテキストを Compose 側に一時提供するための Local。
 * MainActivity で CompositionLocalProvider しておくと安全。
 */
val LocalAppProvider = staticCompositionLocalOf<android.app.Application?> { null }
```0
