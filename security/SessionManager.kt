// security/SessionManager.kt
package com.persona.androidstarter.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SessionManager(ctx: Context) {
  private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
  private val prefs = EncryptedSharedPreferences.create(
    "persona.secure",
    masterKeyAlias,
    ctx,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
  )

  fun isSignedIn(): Boolean = prefs.getBoolean("signed_in", false)
  fun signIn() = prefs.edit().putBoolean("signed_in", true).apply()
  fun signOut() = prefs.edit().clear().apply()
}
