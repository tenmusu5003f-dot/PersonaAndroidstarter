package core.install

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object InstallStore {
    private fun prefs(ctx: Context) = EncryptedSharedPreferences.create(
        "persona.install",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        ctx,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getVersion(ctx: Context, module: String): String? =
        prefs(ctx).getString("ver:$module", null)

    fun setVersion(ctx: Context, module: String, ver: String) {
        prefs(ctx).edit().putString("ver:$module", ver).apply()
    }

    fun setLastGood(ctx: Context, ts: Long) {
        prefs(ctx).edit().putLong("last_good", ts).apply()
    }

    fun lastGood(ctx: Context): Long = prefs(ctx).getLong("last_good", 0L)
}
