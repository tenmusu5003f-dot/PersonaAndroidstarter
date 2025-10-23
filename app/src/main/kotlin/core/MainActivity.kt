package core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonaApp()
        }
    }
}

@Composable
fun PersonaApp() {
    var message by remember { mutableStateOf("ようこそ、Personaへ。") }

    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Persona Starter") })
                }
            ) { padding ->
                Text(
                    text = message,
                    modifier = androidx.compose.ui.Modifier.padding(padding)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonaPreview() {
    PersonaApp()
}
