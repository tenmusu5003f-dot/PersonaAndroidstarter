package com.example.persona.desktop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.singleWindowApplication
import com.example.persona.core.PersonaCore
import com.example.persona.core.DefaultOSBridge

fun main() = singleWindowApplication(title = "Persona Desktop") {
    val core = remember { PersonaCore(os = DefaultOSBridge()) }
    var output by remember { mutableStateOf(core.greet("Architect")) }

    MaterialTheme {
        Button(onClick = { output = if (core.optimizeOnce()) "Optimized!" else "Skipped" }) {
            Text(output)
        }
    }
}
