object CryptoVault {
    private const val TAG = "CryptoVault"
    private val charset = Charsets.UTF_8

    fun ensureInit(ctx: Context): Boolean = try {
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        Log.i(TAG, "Crypto ready")
        true
    } catch (e: Exception) {
        Log.e(TAG, "Crypto init failed: ${e.message}")
        false
    }

    fun encrypt(text: String, key: SecretKeySpec): String {
        val cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val enc = cipher.doFinal(text.toByteArray(charset))
        return android.util.Base64.encodeToString(iv + enc, android.util.Base64.NO_WRAP)
    }

    fun decrypt(base64: String, key: SecretKeySpec): String {
        val all = android.util.Base64.decode(base64, android.util.Base64.NO_WRAP)
        val iv = all.copyOfRange(0, 12)
        val body = all.copyOfRange(12, all.size)
        val cipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, javax.crypto.spec.GCMParameterSpec(128, iv))
        return String(cipher.doFinal(body), charset)
    }
}
