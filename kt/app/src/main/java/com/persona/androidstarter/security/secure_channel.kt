package com.persona.androidstarter.security

import android.util.Base64
import java.security.MessageDigest

/**
 * SecureChannel
 * - 中枢→各部屋 への **片方向** データパイプ
 * - 署名/ハッシュ付きメッセージのみ通す
 */
object SecureChannel {

    data class Envelope(
        val topic: String,            // ex: "persona/echo/status"
        val payload: ByteArray,       // 実データ（JSON/CBOR等）
        val sha256_hex: String        // ペイロードのSHA-256（hex）
    )

    /** 中枢が作る：payload のハッシュを付与 */
    fun wrap(topic: String, payload: ByteArray): Envelope {
        val hex = sha256(payload)
        return Envelope(topic, payload, hex)
    }

    /** 各部屋で受け取る：ハッシュ一致＋Hubがsafeな場合のみOK */
    fun verifyAndUnwrap(env: Envelope): ByteArray? {
        if (!SecurityHub.isSafe()) return null
        val ok = sha256(env.payload).equals(env.sha256_hex, ignoreCase = true)
        return if (ok) env.payload else null
    }

    private fun sha256(bytes: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        val d = md.digest(bytes)
        return d.joinToString("") { "%02x".format(it) }
    }
}
