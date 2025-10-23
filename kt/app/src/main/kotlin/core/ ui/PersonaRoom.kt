package core.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.PersonaMemory
import core.PersonaCore

/**
 * PersonaRoom
 * -------------------------------------------------
 * 各ペルソナ専用ルーム。
 * - 会話ログ・好感度・学習進行度などを表示
 * - PersonaMemory と PersonaCore を接続
 */
@Composable
fun PersonaRoom(
    personaName: String,
    onBack: () -> Unit
) {
    val memory = remember { PersonaMemory(personaName) }
    var affection by remember { mutableStateOf(memory.getStat("affection")) }
    var energy by remember { mutableStateOf(memory.getStat("energy")) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        message = PersonaCore.generateGreeting(personaName)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = personaName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onBack) {
                    Text("Back")
                }
            }

            // Persona image
            val avatar = runCatching {
                painterResource(id = android.R.drawable.sym_def_app_icon)
            }.getOrNull()
            avatar?.let {
                Image(
                    painter = it,
                    contentDescription = personaName,
                    modifier = Modifier
                        .size(180.dp)
                        .padding(8.dp)
                )
            }

            // Message bubble
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Status
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Affection: $affection")
                LinearProgressIndicator(
                    progress = affection / 100f,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 4.dp)
                )
                Text("Energy: $energy")
                LinearProgressIndicator(
                    progress = energy / 100f,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 4.dp)
                )
            }

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    memory.updateStat("affection", 5)
                    affection = memory.getStat("affection")
                    message = PersonaCore.reply(personaName, "praise")
                }) {
                    Text("Talk ❤")
                }
                Button(onClick = {
                    memory.updateStat("energy", 10)
                    energy = memory.getStat("energy")
                    message = PersonaCore.reply(personaName, "gift")
                }) {
                    Text("Gift 🎁")
                }
            }
        }
    }
}
