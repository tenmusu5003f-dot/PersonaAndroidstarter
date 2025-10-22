object BackupManager {

    fun runBackup(context: Context) {
        // 実際のバックアップ処理
        val filesDir = context.filesDir
        val backupFile = File(context.getExternalFilesDir(null), "backup.zip")
        ZipOutputStream(FileOutputStream(backupFile)).use { zip ->
            filesDir.walkTopDown().forEach { file ->
                if (file.isFile) {
                    zip.putNextEntry(ZipEntry(file.relativeTo(filesDir).path))
                    file.inputStream().copyTo(zip)
                    zip.closeEntry()
                }
            }
        }
    }
}
