package com.example.persona.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.persona.backup.BackupManager
import com.example.persona.backup.BackupWorker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun SettingsScreen(
    context: Context,
    vm: SettingsViewModel = viewModel(factory = SettingsViewModel.factory(context)),
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val auto by vm.autoBackupEnabled.collectAsState(initial = false)
    val lastAt by vm.lastBackupAt.collectAsState(initial = 0L)

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("設定") }) }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // 自動バックアップトグル
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("自動バックアップ", style = MaterialTheme.typography.titleMedium)
                    Text("1日1回、端末のアプリ領域をzip保存します。", style = MaterialTheme.typography.bodySmall)
                }
                Switch(checked = auto, onCheckedChange = { enabled ->
                    scope.launch {
                        vm.setAutoBackup(enabled)
                        if (enabled) {
                            // 1日1回の定期実行を登録
                            val req = PeriodicWorkRequestBuilder<BackupWorker>(1, TimeUnit.DAYS).build()
                            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                                "dailyBackup",
                                ExistingPeriodicWorkPolicy.UPDATE,
                                req
                            )
                            Toast.makeText(context, "自動バックアップを有効化しました", Toast.LENGTH_SHORT).show()
                        } else {
                            WorkManager.getInstance(context).cancelUniqueWork("dailyBackup")
                            Toast.makeText(context, "自動バックアップを無効化しました", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }

            // 手動バックアップ
            Button(onClick = {
                scope.launch {
                    val f = BackupManager.createBackupZip(context)
                    vm.setLastBackupAt(System.currentTimeMillis())
                    Toast.makeText(context, "バックアップ作成: ${f.name}", Toast.LENGTH_SHORT).show()
                }
            }) { Text("今すぐバックアップ") }

            // 最終バックアップ時刻
            val ts = if (lastAt > 0) {
                SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPAN).format(Date(lastAt))
            } else "未実行"
            Text("最終バックアップ: $ts", style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(8.dp))
            Text("保存先: Android/data/<本アプリ>/files/backups/", style = MaterialTheme.typography.bodySmall)
        }
    }
}
