package core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import core.ui.PersonaScreen
import core.ui.theme.PersonaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonaTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    PersonaApp()
                }
            }
        }
    }
}

@Composable
fun PersonaApp() {
    var isReady by remember { mutableStateOf(false) }

    // ここでPersonaコアを初期化
    LaunchedEffect(Unit) {
        PersonaCore.initialize()
        isReady = true
    }

    if (isReady) {
        PersonaScreen()
    } else {
        LoadingScreen()
    }
}

@Composable
fun LoadingScreen() {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            text = "Loading Persona System...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
