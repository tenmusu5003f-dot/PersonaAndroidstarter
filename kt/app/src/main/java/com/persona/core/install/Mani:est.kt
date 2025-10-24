package core.install

data class ModuleFile(
    val path: String,      // 例: "scripts/prologue.script"
    val sha256: String,    // 小文字hex
    val size: Long? = null,
    val url: String? = null // リモート同期用
)

data class ModuleSpec(
    val id: String,          // 例: "core-assets"
    val version: String,     // 例: "1.3.0"
    val files: List<ModuleFile>
)

data class InstallPlan(
    val modules: List<ModuleSpec>
)
