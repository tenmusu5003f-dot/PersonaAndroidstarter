// region roadsV1_security
sealed class roadsV1_SecLevel { object Low: roadsV1_SecLevel(); object High: roadsV1_SecLevel() }
object roadsV1_SecurityPolicy {
    private val blockedHigh = setOf("payment/auto", "debug/leak")
    fun allow(action: String, level: roadsV1_SecLevel): Boolean =
        when(level) {
            is roadsV1_SecLevel.High -> action !in blockedHigh
            is roadsV1_SecLevel.Low  -> !action.startsWith("payment/")
        }
}
// endregion

package core

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.os.Debug
import java.security.MessageDigest

/**
 * PersonaSecurity
 * -------------------------------------------------
 * 改ざん検知／デバッガ検知／簡易ルート兆候チェック／署名検証の骨組み。
 * 失敗時は安全モード（演出OFF・書込不可）へ降格する想定。
 */
object PersonaSecurity {

    data class Status(
        val debuggerAttached: Boolean = false,
        val suspiciousBuild: Boolean = false,
        val signatureValid: Boolean = true,
        val safeMode: Boolean = false
    )

    @Volatile
    private var cached: Status? = null

    /** 署名ハッシュ（あなたのビルド署名に差し替え推奨。未設定なら検証スキップ） */
    var expectedSha256: String? = null

    fun evaluate(app: Application): Status {
        cached?.let { return it }

        val debugger = Debug.isDebuggerConnected() || Debug.waitingForDebugger()
        val suspicious = isSuspiciousBuild()
        val sigOk = expectedSha256?.let { exp ->
            verifySignature(app, exp)
        } ?: true

        val safe = debugger || suspicious || !sigOk

        return Status(
            debuggerAttached = debugger,
            suspiciousBuild = suspicious,
            signatureValid = sigOk,
            safeMode = safe
        ).also { cached = it }
    }

    /** 端末のビルド特性から、一般的な“改造兆候”を軽く判定（簡易・誤検知を避ける設計） */
    private fun isSuspiciousBuild(): Boolean {
        val brand = Build.BRAND?.lowercase() ?: ""
        val tags = Build.TAGS?.lowercase() ?: ""
        val model = Build.MODEL?.lowercase() ?: ""
        val fingerprint = Build.FINGERPRINT?.lowercase() ?: ""

        // 開発ビルド/テスト端末の緩い兆候
        val devLike =
            "test-keys" in tags ||
            "dev-keys" in tags ||
            model.contains("emulator") ||
            model.contains("sdk") ||
            fingerprint.contains("generic")

        // ここに過激なブロックは入れない（誤検知防止のため）
        return devLike
    }

    /** アプリの署名 SHA-256 を取得して期待値と比較 */
    private fun verifySignature(app: Application, expectedSha256: String): Boolean {
        return try {
            val pkg = app.packageName
            val pm = app.packageManager
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                PackageManager.GET_SIGNING_CERTIFICATES
            else
                @Suppress("DEPRECATION") PackageManager.GET_SIGNATURES

            val info = pm.getPackageInfo(pkg, flags)
            val sigBytes: ByteArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val signingInfo = info.signingInfo
                val sig = if (signingInfo.hasMultipleSigners())
                    signingInfo.apkContentsSigners.first()
                else
                    signingInfo.signingCertificateHistory.first()
                sig.toByteArray()
            } else {
                @Suppress("DEPRECATION")
                info.signatures.first().toByteArray()
            }

            val sha = sha256(sigBytes)
            sha.equals(expectedSha256.lowercase(), ignoreCase = true)
        } catch (e: Exception) {
            false
        }
    }

    private fun sha256(data: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        val out = md.digest(data)
        return out.joinToString("") { "%02x".format(it) }
    }

    /** セキュリティ結果に応じた安全降格ヘルパー */
    fun applySafeModeIfNeeded(core: PersonaCore) {
        val status = cached ?: return
        if (status.safeMode) {
            // 起動演出など負荷の高い機能を落とす
            core.registerService("opening.effect", OpeningEffects.forTier(EffectPolicy.EffectTier.OFF))
        }
    }

    /** 必要に応じて、外からキャッシュを無効化 */
    fun reset() {
        cached = null
    }
}
