package com.example.persona.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "persona_settings")

object Keys {
    val AUTO_BACKUP = booleanPreferencesKey("auto_backup_enabled")
    val LAST_BACKUP_AT = longPreferencesKey("last_backup_at")
}

class SettingsRepository(private val context: Context) {
    val autoBackupEnabled: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.AUTO_BACKUP] ?: false }

    val lastBackupAt: Flow<Long> =
        context.dataStore.data.map { it[Keys.LAST_BACKUP_AT] ?: 0L }

    suspend fun setAutoBackup(enabled: Boolean) {
        context.dataStore.edit { it[Keys.AUTO_BACKUP] = enabled }
    }

    suspend fun setLastBackupAt(ts: Long) {
        context.dataStore.edit { it[Keys.LAST_BACKUP_AT] = ts }
    }
}
