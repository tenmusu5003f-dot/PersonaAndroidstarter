package com.example.persona

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.persona.data.PersonaConfig
import com.example.persona.tts.TtsHelper
import com.example.persona.settings.SettingsScreen

class MainActivity : ComponentActivity() {
    private lateinit var tts: TtsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TtsHelper(this)
        val personas = PersonaConfig.load(this)

        setContent {
            MaterialTheme {
                var showSettings by remember { mutableStateOf(false) }
                if (showSettings) {
                    SettingsScreen(
                        context = this,
                        onBack = { showSettings = false }
                    )
                } else {
                    PersonaHome(
                        personas = personas,
                        onSpeak = { tts.speak(it) },
                        onOptimize = { true },
                        onOpenSettings = { showSettings = true }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }
}

@Composable
fun PersonaHome(
    personas: List<com.example.persona.data.PersonaEntry>,
    onSpeak: (String) -> Unit,
    onOptimize: () -> Boolean,
    onOpenSettings: () -> Unit
) {
    var status by remember { mutableStateOf("Persona is online.") }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Persona") },
                actions = {
                    TextButton(onClick = onOpenSettings) { Text("設定") }
                }
            )
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val ok = onOptimize()
                    status = if (ok) "Optimized!" else "Skipped"
                }) { Text("最適化") }

                Button(onClick = { onSpeak("こんにちは、ペルソナです。") }) {
                    Text("話す")
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(status, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            Text("Personas", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(personas) { p ->
                    ElevatedCard {
                        Column(Modifier.padding(12.dp)) {
                            Text("${p.name}（${p.id}）", style = MaterialTheme.typography.titleSmall)
                            Text("mood=${p.mood} / lang=${p.lang}")
                        }
                    }
                }
            }
        }
    }
}
