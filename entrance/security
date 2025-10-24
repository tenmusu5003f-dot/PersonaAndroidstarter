// =====================================
// 🚀 Persona Android Starter - FastBoot Header
// =====================================

import android.content.Context
import kotlinx.coroutines.*
import android.util.Log

object FastBoot {
    private const val TAG = "FastBoot"

    suspend fun launch(ctx: Context): Boolean = coroutineScope {
        val start = System.currentTimeMillis()
        Log.i(TAG, "⚡ Boot sequence start")

        // 並列処理ブロック（すべて同時に起動）
        val results = awaitAll(
            async { SignatureGuard.verifySelf(ctx) },
            async { TamperGuard.checkBuildFlags() },
            async { RootGuard.quickScan(ctx) },
            async { CryptoVault.warmup(ctx) },
            async { AssetGuard.verifyHashes(ctx) },
            async { NetGuard.pinSetup(ctx) },
            async { Prewarm.warmTTS(ctx) },
        )

        val ok = results.all { it }
        val time = System.currentTimeMillis() - start
        Log.i(TAG, "⚙️ Boot complete: $ok ($time ms)")
        return@coroutineScope ok
    }
}

// security/BootOrchestrator.kt
package com.persona.androidstarter.security
import android.content.Context
import kotlinx.coroutines.*

object BootOrchestrator {
    suspend fun runAll(ctx: Context): Boolean = coroutineScope {
        val results = awaitAll(
            async { SignatureGuard.verifySelf(ctx) },   // ① 署名検証
            async { TamperGuard.checkBuildFlags() },    // ② 改ざん/デバッグ検知
            async { RootGuard.quickScan(ctx) },         // ③ ルート簡易検知
            async { CryptoVault.warmup(ctx) },          // ④ キー保管庫の起動
            async { AssetGuard.verifyHashes(ctx) },     // ⑤ 重要アセットのハッシュ確認
            async { NetGuard.pinSetup() },              // ⑥ 証明書ピン留め(後述)
            async { Prewarm.warmTTS(ctx) }              // ⑦ TTS等のプリウォーム
        )
        results.all { it } // 全部OKならtrue
    }
}

// MainActivity.kt（抜粋）
override fun onStart() {
    super.onStart()
    val nav = (supportFragmentManager
        .findFragmentById(R.id.nav_host) as NavHostFragment).navController

    lifecycleScope.launch {
        val ok = BootOrchestrator.runAll(this@MainActivity)
        if (ok) AuthGate.enforce(nav) else nav.navigate(R.id.loginFragment)
    }
}

// security/SignatureGuard.kt
package com.persona.androidstarter.security
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import java.security.MessageDigest

object SignatureGuard {
    // リリース署名のハードコード（base64のSHA-256）
    private const val KNOWN_SIG_SHA256 = "AbCdEf...==" 

    fun verifySelf(ctx: android.content.Context): Boolean {
        val pm = ctx.packageManager
        val pkg = ctx.packageName
        val sigBytes = if (Build.VERSION.SDK_INT >= 28) {
            val info = pm.getPackageInfo(pkg, PackageManager.GET_SIGNING_CERTIFICATES)
            info.signingInfo.apkContentsSigners.first().toByteArray()
        } else {
            @Suppress("DEPRECATION")
            pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES).signatures.first().toByteArray()
        }
        val sha = MessageDigest.getInstance("SHA-256").digest(sigBytes)
        val b64 = Base64.encodeToString(sha, Base64.NO_WRAP)
        return b64 == KNOWN_SIG_SHA256
    }
}

// security/TamperGuard.kt
object TamperGuard {
    fun checkBuildFlags(): Boolean {
        if (BuildConfig.DEBUG) return false
        val suspicious = listOf("frida", "xposed", "magisk")
        val buildTags = android.os.Build.TAGS?.lowercase().orEmpty()
        return suspicious.none { buildTags.contains(it) }
    }
}

// security/RootGuard.kt
object RootGuard {
    fun quickScan(ctx: android.content.Context): Boolean {
        val paths = listOf("/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su")
        return paths.none { java.io.File(it).exists() }
    }
}

// security/CryptoVault.kt
object CryptoVault {
    fun warmup(ctx: android.content.Context): Boolean = try {
        SessionManager(ctx).isSignedIn(); true
    } catch (_: Exception) { false }
}

// security/AssetGuard.kt
object AssetGuard {
    private val expected = mapOf(
        "scripts/prologue.script" to "xJ1...==",
        "scripts/eventA.script"   to "Z9k...=="
    )
    fun verifyHashes(ctx: android.content.Context): Boolean {
        val md = MessageDigest.getInstance("SHA-256")
        return expected.all { (path, wantB64) ->
            ctx.assets.open(path).use { ins ->
                val got = Base64.encodeToString(md.digest(ins.readBytes()), Base64.NO_WRAP)
                got == wantB64
            }
        }
    }
}

// security/NetGuard.kt
object NetGuard {
    lateinit var client: okhttp3.OkHttpClient
    fun pinSetup(): Boolean = try {
        val pinner = okhttp3.CertificatePinner.Builder()
            .add("your.api.example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            .build()
        client = okhttp3.OkHttpClient.Builder().certificatePinner(pinner).build()
        true
    } catch (_: Exception) { false }
}

// security/Prewarm.kt
object Prewarm {
    suspend fun warmTTS(ctx: android.content.Context): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            ServiceLocator.tts.init(ctx) // ここでエンジン起動・音声読み込み
        }.isSuccess
    }
}

// MainActivity.kt（onCreate先頭）
installSplashScreen().apply {
    setKeepOnScreenCondition { /* BootOrchestrator中はtrue */ loading }
}

-dontoptimize
-keep class com.persona.androidstarter.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepattributes *Annotation*
