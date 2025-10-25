package com.persona.androidstarter.core

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import java.io.File
import java.security.MessageDigest

object Security {

    /** 端末が開発者モード/デバッグ可能か（本番では false が望ましい） */
    fun isDebuggable(context: Context): Boolean =
        (context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0

    /** Play ストア（com.android.vending）経由のインストールか */
    fun isFromPlayStore(context: Context): Boolean {
        val pm = context.packageManager
        val installer = if (Build.VERSION.SDK_INT >= 30) {
            pm.getInstallSourceInfo(context.packageName).installingPackageName
        } else {
            @Suppress("DEPRECATION")
            pm.getInstallerPackageName(context.packageName)
        }
        return installer == "com.android.vending"
    }

    /** 署名ハッシュ（SHA-256）が想定と一致するか（簡易改ざん検知） */
    fun isSignatureValid(context: Context, expectedSha256: String): Boolean {
        return try {
            val pm = context.packageManager
            val pkg = if (Build.VERSION.SDK_INT >= 28)
                pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            else
                @Suppress("DEPRECATION")
                pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)

            val certBytes = if (Build.VERSION.SDK_INT >= 28)
                pkg.signingInfo.apkContentsSigners.first().toByteArray()
            else
                @Suppress("DEPRECATION")
                pkg.signatures.first().toByteArray()

            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(certBytes)
            digest.joinToString(":") { "%02X".format(it) } == expectedSha256.uppercase()
        } catch (_: Exception) {
            false
        }
    }

    /** ざっくり Root/エミュレータ兆候（誤検知を避けるため緩め） */
    fun hasSuspiciousEnvironment(): Boolean {
        val suExists = listOf(
            "/system/bin/su", "/system/xbin/su", "/sbin/su", "/system/su"
        ).any { File(it).exists() }
        val emulator = (Build.FINGERPRINT?.contains("generic", true) == true) ||
                (Build.MODEL?.contains("google_sdk", true) == true)
        return suExists || emulator
    }

    /** 収集系の既定（Play の Data safety で “収集なし” を宣言するなら false 固定） */
    val telemetryEnabled: Boolean = false

    /** 端末識別子の代替（広告ID等は扱わず、端末ローカルな一意IDに限定） */
    fun localDeviceId(context: Context): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown"
}
