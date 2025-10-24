object NetGuard {
    private const val TAG = "NetGuard"
    lateinit var client: okhttp3.OkHttpClient

    fun pinSetup(ctx: Context): Boolean = try {
        val host = "api.example.com" // ←あなたのAPIドメイン
        val pins = listOf(
            // openssl x509 -pubkey -noout -in cert.pem | openssl pkey -pubin -outform der | openssl dgst -sha256 -binary | base64
            "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
        )
        val pinner = okhttp3.CertificatePinner.Builder().apply {
            pins.forEach { add(host, it) }
        }.build()
        client = okhttp3.OkHttpClient.Builder()
            .certificatePinner(pinner)
            .build()
        Log.i(TAG, "NetGuard ready for $host")
        true
    } catch (e: Exception) {
        Log.e(TAG, "NetGuard error: ${e.message}")
        false
    }

    fun get(url: String): String? = try {
        val req = okhttp3.Request.Builder().url(url).build()
        client.newCall(req).execute().use { it.body?.string() }
    } catch (e: Exception) {
        Log.e(TAG, "GET failed: ${e.message}")
        null
    }
}
