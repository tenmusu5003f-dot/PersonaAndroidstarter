// --- roadsV1: simple duplicate scan (source filenames) ---
tasks.register("scanDuplicateSourceNames") {
    group = "verification"
    description = "Scan kotlin source for duplicate filenames (warning only)."
    doLast {
        val src = fileTree("src/main/kotlin") { include("**/*.kt") }.files
        val byName = src.groupBy { it.name }
        val dups = byName.filter { it.value.size > 1 }
        if (dups.isNotEmpty()) {
            println("⚠️ Duplicate source filenames detected:")
            dups.forEach { (name, files) ->
                println(" - $name")
                files.forEach { f -> println("    • ${f.relativeTo(project.projectDir)}") }
            }
        } else {
            println("✅ No duplicate source filenames.")
        }
    }
}
// CI で実行したいとき：
// tasks.named("check").configure { dependsOn("scanDuplicateSourceNames") }
