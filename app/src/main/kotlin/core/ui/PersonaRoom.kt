package core.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import core.ui.theme.PersonaTheme

/**
 * PersonaRoom
 * -------------------------------------------------
 * 各ペルソナAIが存在する「部屋」。
 * - チャット入力欄付き
 * - レスポンス表示とアニメーション余地あり
 * - 後に音声出力・Live2D・動的背景追加予定
 */
@Composable
fun PersonaRoom(
    personaName: String,
    onBack: () -> Unit
) {
    PersonaTheme {
        var input by remember { mutableStateOf("") }
        var response by remember { mutableStateOf("ようこそ、$personaName の部屋へ。") }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
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
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }

                // AI Response area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = response,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Input area
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BasicTextField(
                        value = input,
                        onValueChange = { input = it },
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .height(48.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 8.dp, vertical = 10.dp)
                    )
                    Button(onClick = {
                        response = "「$input」…なるほど、そう思うんですね。"
                        input = ""
                    }) {
                        Text("Send")
                    }
                }
            }
        }
    }
}
