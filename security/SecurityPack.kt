package com.persona.androidstarter.security

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.File
import java.security.MessageDigest

/** ===========================
 *  🚀 FastBoot + Guards 一式
 *  =========================== */

object FastBoot {
    private const val TAG = "FastBoot"
    suspend fun launch(ctx: Context, timeoutMs: Long = 5_000): Boolean = coroutineScope {
        try {
            val t0 = System.currentTimeMillis()
            val results = withTimeout(timeoutMs) {
                awaitAll(
                    async { SignatureGuard.verifySelf(ctx) },     // 必須
                    async { TamperGuard.checkBuildFlags(ctx) },   // 必須(開発中は常にtrue)
                    async { RootGuard.quickScan(ctx) },           // 任意
                    async { CryptoVault.ensureInit(ctx) },        // 必須
                    async { AssetGuard.verifyHashes(ctx) },       // 推奨
                    async { NetGuard.pinSetup(ctx) },             // 任意
                    async { TTSPrewarm.warm(ctx); true }          // 任意（失敗しても続行）
                )
            }
            val ok = results.all { it }
            Log.i(TAG, "Boot complete=$ok in ${System.currentTimeMillis()-t0} ms")
            ok
        } catch (e: Exception) {
            Log.w(TAG, "boot error: ${e.message}")
            false
        }
    }
}

/** セッション（暗号化Preferences） */
class SessionManager(ctx: Context) {
    private val master = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val pref = EncryptedSharedPreferences.create(
        "persona.secure", master, ctx,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    fun isSignedIn(): Boolean = pref.getBoolean("signed_in", false)
    fun signIn() = pref.edit().putBoolean("signed_in", true).apply()
    fun signOut() = pref.edit().clear().apply()
}

/** 未ログインならLoginへ寄せる */
object AuthGate {
    fun enforce(nav: androidx.navigation.NavController, session: SessionManager) {
        if (!session.isSignedIn()) {
            nav.navigate(com.persona.androidstarter.R.id.loginFragment) {
                popUpTo(com.persona.androidstarter.R.id.homeFragment) { inclusive = false }
                launchSingleTop = true
            }
        }
    }
}

/** 署名チェック（雛形） */
object SignatureGuard {
    private const val TAG = "SignatureGuard"
    fun verifySelf(ctx: Context): Boolean = try {
        val info = ctx.packageManager.getPackageInfo(
            ctx.packageName, PackageManager.GET_SIGNING_CERTIFICATES
        )
        val ok = !info.signingInfo?.apkContentsSigners.isNullOrEmpty()
        if (!ok) Log.w(TAG, "no signatures")
        ok
    } catch (e: Exception) { Log.e(TAG,"${e.message}"); false }
}

/** デバッグ/改変フラグ（雛形） */
object TamperGuard {
    fun checkBuildFlags(ctx: Context): Boolean = try {
        // 開発中は true で通す。リリースでは厳格に。
        true
    } catch (e: Exception) { false }
}

/** ルート簡易検知 */
object RootGuard {
    private val suPaths = listOf("/system/bin/su","/system/xbin/su","/sbin/su","/vendor/bin/su")
    fun quickScan(@Suppress("UNUSED_PARAMETER") ctx: Context): Boolean = try {
        suPaths.none { File(it).exists() }
    } catch (_: Exception) { true }
}

/** 鍵初期化 */
object CryptoVault {
    fun ensureInit(@Suppress("UNUSED_PARAMETER") ctx: Context): Boolean = try {
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC); true
    } catch (_: Exception) { false }
}

/** アセットハッシュ検証（assets/expected-hashes.json を参照・未配置は通過） */
object AssetGuard {
    private fun loadExpected(ctx: Context): JSONObject? = try {
        ctx.assets.open("expected-hashes.json").use { ins ->
            JSONObject(ins.readBytes().toString(Charsets.UTF_8))
        }
    } catch (_: Exception) { null }

    fun verifyHashes(ctx: Context): Boolean = try {
        val expected = loadExpected(ctx) ?: return true
        val am = ctx.assets
        val keys = expected.keys()
        while (keys.hasNext()) {
            val name = keys.next()
            am.open(name).use { ins ->
                val actual = sha256(ins)
                val want = expected.getString(name)
                if (!actual.equals(want, true)) return false
            }
        }
        true
    } catch (_: Exception) { false }

    private fun sha256(ins: java.io.InputStream): String {
        val md = MessageDigest.getInstance("SHA-256")
        val buf = ByteArray(8192); var r: Int
        while (ins.read(buf).also { r = it } > 0) md.update(buf, 0, r)
        return md.digest().joinToString("") { "%02x".format(it) }
    }
}

/** ネットワーク初期化（ピンニング等を後で実装） */
object NetGuard { fun pinSetup(@Suppress("UNUSED_PARAMETER") ctx: Context) = true }

/** TTSプリウォーム（SDKに合わせて中身を入れてね） */
object TTSPrewarm { fun warm(@Suppress("UNUSED_PARAMETER") ctx: Context) { /* no-op */ } }
