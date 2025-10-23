package core.security

import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

/**
 * PersonaSecurity
 * -------------------------------------------------
 * Persona AIデータのセキュリティ管理を担当。
 * - 暗号化/復号化（AES）
 * - データ整合性チェック
 * - 改ざん防止ロジック
 */
object PersonaSecurity {

    private const val SECRET_KEY = "PersonaSecureKey"
    private const val AES_MODE = "AES/ECB/PKCS5Padding"

    /** AESで暗号化 */
    fun encrypt(input: String): String {
        return try {
            val key = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
            val cipher = Cipher.getInstance(AES_MODE)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val encrypted = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
            Base64.encodeToString(encrypted, Base64.DEFAULT)
        } catch (e: Exception) {
            "EncryptionError:${e.message}"
        }
    }

    /** AESで復号化 */
    fun decrypt(encrypted: String): String {
        return try {
            val key = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
            val cipher = Cipher.getInstance(AES_MODE)
            cipher.init(Cipher.DECRYPT_MODE, key)
            val decoded = Base64.decode(encrypted, Base64.DEFAULT)
            String(cipher.doFinal(decoded), Charsets.UTF_8)
        } catch (e: Exception) {
            "DecryptionError:${e.message}"
        }
    }

    /** データ整合性ハッシュ生成 */
    fun checksum(data: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(data.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }

    /** 改ざん検知（内部でチェック） */
    fun isTampered(original: String, hash: String): Boolean {
        return checksum(original) != hash
    }

    /** デバッグ表示（セキュリティログ） */
    fun debugLog(message: String) {
        println("🔐 [PersonaSecurity] $message")
    }
}
