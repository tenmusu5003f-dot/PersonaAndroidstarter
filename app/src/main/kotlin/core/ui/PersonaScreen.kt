package core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.ui.theme.PersonaTheme

/**
 * PersonaScreen
 * -------------------------------------------------
 * ホーム画面やペルソナ一覧など、共通UIの基礎。
 * - スプラッシュ後に開く画面
 * - ペルソナ名やAI情報を中央表示
 * - 軽いフェード演出付き
 */
@Composable
fun PersonaScreen(
    modifier: Modifier = Modifier,
    personaName: String = "Echo",
    status: String = "Active",
    onSelect: (() -> Unit)? = null
) {
    PersonaTheme {
        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            visible = true
        }

        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = personaName,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Status: $status",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(onClick = { onSelect?.invoke() }) {
                            Text("Enter Room")
                        }
                    }
                }
            }
        }
    }
}
