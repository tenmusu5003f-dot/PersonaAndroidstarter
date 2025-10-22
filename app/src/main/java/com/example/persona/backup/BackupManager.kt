package com.example.persona.backup

import android.content.Context
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object BackupManager {

    /**
     * アプリの内部ストレージ(filesDir 以下)を zip に固めて
     * getExternalFilesDir(null)/backups/ に保存する。
     * 外部権限は不要（アプリ専用領域）。
     * 戻り値: 作成したファイル
     */
    fun createBackupZip(context: Context): File {
        val srcRoot = context.filesDir
        val outDir = File(context.getExternalFilesDir(null), "backups").apply { mkdirs() }
        val ts = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val outFile = File(outDir, "persona_backup_$ts.zip")

        ZipOutputStream(BufferedOutputStream(FileOutputStream(outFile))).use { zip ->
            srcRoot.walkTopDown().forEach { f ->
                if (f.isFile) {
                    val relPath = f.relativeTo(srcRoot).path.replace("\\", "/")
                    zip.putNextEntry(ZipEntry(relPath))
                    f.inputStream().use { input -> input.copyTo(zip, 8 * 1024) }
                    zip.closeEntry()
                }
            }
        }
        return outFile
    }
}
