package com.example.persona.backup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.*
import com.example.persona.settings.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val repo = SettingsRepository(context)
            val enabled = runBlocking { repo.autoBackupEnabled.first() }
            if (enabled) scheduleDailyBackup(context)
        }
    }

    companion object {
        fun scheduleDailyBackup(context: Context) {
            val request = PeriodicWorkRequestBuilder<BackupWorker>(1, TimeUnit.DAYS).build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "dailyBackup",
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        }

        fun cancelDailyBackup(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork("dailyBackup")
        }
    }
}
