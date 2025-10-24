package core.install

import android.content.Context
import java.io.File
import java.security.MessageDigest

object FileUtils {
    fun dir(ctx: Context, name: String): File =
        File(ctx.filesDir, name).apply { if (!exists()) mkdirs() }

    fun sha256(file: File): String {
        val md = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { ins ->
            val buf = ByteArray(8192)
            while (true) {
                val r = ins.read(buf); if (r <= 0) break
                md.update(buf, 0, r)
            }
        }
        return md.digest().joinToString("") { "%02x".format(it) }
    }

    fun atomicSwapDir(staging: File, live: File, backup: File) {
        // live -> backup
        if (live.exists()) {
            if (backup.exists()) backup.deleteRecursively()
            live.renameTo(backup)
        }
        // staging -> live
        staging.renameTo(live)
    }
}
