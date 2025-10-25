package com.persona.androidstarter.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptedStore(ctx: Context) {
    private val masterKey = MasterKey.Builder(ctx)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        ctx,
        "persona_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun putString(key: String, value: String) { prefs.edit().putString(key, value).apply() }
    fun getString(key: String, def: String? = null): String? = prefs.getString(key, def)

    /** SOS後は痕跡を消す（最小化） */
    fun wipe(keys: List<String>) { prefs.edit().apply { keys.forEach { remove(it) } }.apply() }
}
