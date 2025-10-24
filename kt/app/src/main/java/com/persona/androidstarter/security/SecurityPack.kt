package com.persona.androidstarter.security

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.security.MessageDigest

object AssetGuard {
  private const val TAG = "AssetGuard"

  fun verifyAll(ctx: Context): Boolean {
    return try {
      val expected = loadExpected(ctx) // assets/expected-hashes.json
      val assetManager = ctx.assets
      val keys = expected.keys()
      while (keys.hasNext()) {
        val name = keys.next()
        val expectedHash = expected.getString(name)
        val actualHash = sha256(assetManager.open(name))
        if (!expectedHash.equals(actualHash, ignoreCase = true)) {
          Log.w(TAG, "Hash mismatch: $name")
          // 必要なら削除や無効化フラグをたてる
          return false
        }
      }
      true
    } catch (e: Exception) {
      Log.e(TAG, "Asset verify failed: ${e.message}")
      false
    }
  }

  private fun loadExpected(ctx: Context): JSONObject {
    ctx.assets.open("expected-hashes.json").use { ins ->
      val s = ins.readBytes().toString(Charsets.UTF_8)
      return JSONObject(s)
    }
  }

  private fun sha256(ins: java.io.InputStream): String {
    val md = MessageDigest.getInstance("SHA-256")
    ins.use { input ->
      val buf = ByteArray(8192)
      var r: Int
      while (input.read(buf).also { r = it } > 0) md.update(buf, 0, r)
    }
    return md.digest().joinToString("") { "%02x".format(it) }
  }
}
