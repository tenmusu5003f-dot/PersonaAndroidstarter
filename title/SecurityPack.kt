val verdict = SecurityPack.evaluate(applicationContext)
if (!verdict.isSafe) {
    Log.w("PersonaGate", "Blocked:\n${verdict.advice}")
    // 入口ガード：ログインへ飛ばす/スプラッシュに留める/終了ダイアログ など
}

// app/src/main/java/com/persona/androidstarter/security/SecurityPack.kt
@file:Suppress(
    "unused", "MemberVisibilityCanBePrivate", "SpellCheckingInspection",
    "TooManyFunctions"
)

package com.persona.androidstarter.security

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Base64
import android.util.Log
import java.io.BufferedInputStream
import java.security.MessageDigest

/**
 * SecurityPack
 * ------------------------------------------------------------
 * 起動直後に実行する“軽量かつ堅牢な”統合セキュリティチェック。
 * 依存最小（標準APIのみ）。失敗時もアプリを落とさず、明確な判定を返す。
 * 後から個別ガードを差し替えやすい構造にしてある。
 *
 * 使い方（例）:
 *   val v = SecurityPack.evaluate(applicationContext)
 *   if (!v.isSafe) { /* 入口で弾く／スプラッシュに留める等 */ }
 */
object SecurityPack {

    // --- 公開モデル ---------------------------------------------------------

    data class Verdict(
        val signatureValid: Boolean,
        val tamperSuspicious: Boolean,
        val rooted: Boolean,
        val adbEnabled: Boolean,
        val assetHashOk: Boolean,
        val emulatorSuspicious: Boolean,
        val installerTrusted: Boolean,
        val advice: String
    ) {
        val isSafe: Boolean =
            signatureValid &&
            !tamperSuspicious &&
            !rooted &&
            !emulatorSuspicious &&
            installerTrusted &&
            assetHashOk
    }

    // --- エントリポイント ---------------------------------------------------

    fun evaluate(ctx: Context): Verdict {
        val sig = SignatureGuard.verifySelf(ctx)
        val tamper = TamperGuard.checkBuildFlags()
        val root = RootGuard.quickScan()
        val adb = DebugGuard.isAdbEnabled(ctx)
        val assetOk = AssetGuard.verifyHashes(ctx)
        val emu = DeviceGuard.suspiciousEmulator()
        val installer = InstallerGuard.trusted(ctx)

        val advice = buildString {
            if (!sig) appendLine("・アプリ署名が想定外です。再配布や改竄の可能性。")
            if (tamper) appendLine("・デバッグ/改変フラグを検知。")
            if (root) appendLine("・root/改造環境の疑い。")
            if (adb) appendLine("・ADBが有効です。（開発機なら許容可）")
            if (!assetOk) appendLine("・重要アセットのハッシュ不一致。")
            if (emu) appendLine("・エミュレータ特有の痕跡あり。")
            if (!installer) appendLine("・インストーラが未許可。")
            if (isEmpty()) append("問題なし。")
        }

        val v = Verdict(
            signatureValid = sig,
            tamperSuspicious = tamper,
            rooted = root,
            adbEnabled = adb,
            assetHashOk = assetOk,
            emulatorSuspicious = emu,
            installerTrusted = installer,
            advice = advice.trim()
        )
        log("verdict: $v")
        return v
    }

    // --- 個別ガード群 -------------------------------------------------------

    /** 署名検証（簡易）：自アプリの署名ハッシュを取得してホワイトリストと照合。 */
    private object SignatureGuard {
        // ここに“正規ビルドの署名ハッシュ（SHA-256 Base64）”を追加していく
        // 本番署名を apksigner/Play 由来のものに置き換えて使う。
        private val allowed = setOf(
            // "K9c...YourReleaseCertHash...Q=",
            // "DevDebugHashBase64..."
        )

        fun verifySelf(ctx: Context): Boolean = runCatching {
            val pm = ctx.packageManager
            val pkg = ctx.packageName
            val signatures = if (Build.VERSION.SDK_INT >= 28) {
                val pi = pm.getPackageInfo(pkg, PackageManager.GET_SIGNING_CERTIFICATES)
                pi.signingInfo.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES).signatures
            }
            val sha256 = MessageDigest.getInstance("SHA-256")
            signatures.any { sig ->
                val hash = Base64.encodeToString(sha256.digest(sig.toByteArray()), Base64.NO_WRAP)
                allowed.isEmpty() || allowed.contains(hash)
            }
        }.getOrElse { e ->
            loge("SignatureGuard", e); true // 署名未登録の間は通す
        }
    }

    /** デバッグgable・エンジン改変の匂いなど最低限のフラグ検知。 */
    private object TamperGuard {
        fun checkBuildFlags(): Boolean {
            // Debuggable / TestOnly などの最小検知（必要に応じて拡張）
            val debug = isAppDebuggable()
            val testOnly = isTestOnly()
            return debug || testOnly
        }

        private fun isAppDebuggable(): Boolean = try {
            // BuildConfig.DEBUG に依存しない安全な判定
            (0 != (android.os.Debug::class.java
                .getDeclaredMethod("isDebuggerConnected")
                .invoke(null) as Boolean)
            )
        } catch (_: Throwable) {
            android.os.Debug.isDebuggerConnected()
        }

        private fun isTestOnly(): Boolean = Build.TAGS?.contains("test-keys") == true
    }

    /** ルート・改造環境の簡易スキャン（ファイル痕跡とビルドタグ）。 */
    private object RootGuard {
        private val knownPaths = listOf(
            "/system/app/Superuser.apk",
            "/sbin/su", "/system/bin/su", "/system/xbin/su",
            "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su"
        )

        fun quickScan(): Boolean = try {
            if (Build.TAGS?.contains("test-keys") == true) return true
            knownPaths.any { path -> java.io.File(path).exists() }
        } catch (e: Throwable) {
            loge("RootGuard", e); false
        }
    }

    /** ADB 有効状態（開発機なら許容も可）。 */
    private object DebugGuard {
        fun isAdbEnabled(ctx: Context): Boolean = runCatching {
            Settings.Global.getInt(ctx.contentResolver, Settings.Global.ADB_ENABLED, 0) == 1
        }.getOrDefault(false)
    }

    /** 重要アセットのハッシュを点検。必要に応じて `targets` を増やす。 */
    private object AssetGuard {
        // 「壊れていたら困る」ファイルを並べる（存在しない場合はスキップ＝OK）
        private val targets = listOf(
            // "models/core.bin",
            // "packs/manifest.json"
        )

        private val expected = mapOf<String, String>(
            // "models/core.bin" to "sha256:xxxxxxxx...",
        )

        fun verifyHashes(ctx: Context): Boolean {
            for (rel in targets) {
                val ok = verifyOne(ctx, rel)
                if (!ok) return false
            }
            return true
        }

        private fun verifyOne(ctx: Context, rel: String): Boolean = runCatching {
            val exp = expected[rel] ?: return true // まだ登録していなければスルー
            val want = exp.substringAfter("sha256:", missingDelimiterValue = exp)
            val am = ctx.assets
            BufferedInputStream(am.open(rel)).use { ins ->
                val md = MessageDigest.getInstance("SHA-256")
                val buf = ByteArray(8 * 1024)
                var n: Int
                while (true) {
                    n = ins.read(buf)
                    if (n <= 0) break
                    md.update(buf, 0, n)
                }
                val have = md.digest().joinToString("") { "%02x".format(it) }
                have.equals(want, ignoreCase = true)
            }
        }.getOrElse { e ->
            loge("AssetGuard", e); false
        }
    }

    /** エミュレータらしさ（完全ではないが軽量）。 */
    private object DeviceGuard {
        fun suspiciousEmulator(): Boolean {
            val bd = Build.BRAND?.lowercase().orEmpty()
            val mf = Build.MANUFACTURER?.lowercase().orEmpty()
            val prod = Build.PRODUCT?.lowercase().orEmpty()
            val model = Build.MODEL?.lowercase().orEmpty()

            val marks = listOf("generic", "sdk", "emulator", "genymotion", "vbox", "qemu")
            return listOf(bd, mf, prod, model).any { s -> marks.any { m -> s.contains(m) } }
        }
    }

    /** インストーラ確認（Play/社内ストアなどホワイトリスト方式）。 */
    private object InstallerGuard {
        private val trustedInstallers = setOf(
            "com.android.vending",      // Google Play
            "com.google.android.packageinstaller" // サイドロード（開発用）：本番は外してOK
        )

        fun trusted(ctx: Context): Boolean = runCatching {
            val pm = ctx.packageManager
            val src = if (Build.VERSION.SDK_INT >= 30) {
                pm.getInstallSourceInfo(ctx.packageName).installingPackageName
            } else {
                @Suppress("DEPRECATION")
                pm.getInstallerPackageName(ctx.packageName)
            }
            src == null || trustedInstallers.contains(src)
        }.getOrElse { e ->
            loge("InstallerGuard", e); true // 取得不能時は許容
        }
    }

    // --- 共通ログ ------------------------------------------------------------

    private fun log(msg: String) = Log.i("SecurityPack", msg)
    private fun loge(tag: String, e: Throwable) =
        Log.w("SecurityPack/$tag", e.message ?: e.toString())
}
