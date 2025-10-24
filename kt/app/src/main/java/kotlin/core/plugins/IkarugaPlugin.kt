// ScriptEngine.kt
class ScriptEngine {
    private val sandbox = ScriptSandbox()
    fun run(scriptText: String) {
        val ast = ScriptParser.parse(scriptText)
        sandbox.guard {
            ScriptExecutor.execute(ast)
        }
    }
}

// ScriptSandbox.kt
class ScriptSandbox {
    inline fun guard(block: () -> Unit) {
        try {
            withTimeout(3000) { block() }   // 無限ループ対策
        } catch (e: Exception) {
            Log.e("ScriptSandbox", "Error: ${e.message}")
        }
    }
}

// ScriptParser.kt
object ScriptParser {
    fun parse(src: String): List<Command> {
        return src.lines()
            .filter { it.isNotBlank() }
            .map { line ->
                val parts = line.split(" ", limit = 2)
                Command(parts[0], parts.getOrNull(1))
            }
    }
}
data class Command(val name: String, val arg: String?)

// ScriptParser.kt
object ScriptParser {

  // ScriptExecutor.kt
object ScriptExecutor {
    fun execute(cmds: List<Command>) {
        for (c in cmds) when (c.name) {
            "say" -> say(c.arg ?: "")
            "wait" -> waitMs(c.arg?.toLongOrNull() ?: 0)
            else -> Log.w("Executor", "Unknown command: ${c.name}")
        }
    }
    private fun say(text: String) {
        // TTSエンジンを呼び出す処理を後で接続
    }
    private fun waitMs(ms: Long) { Thread.sleep(ms) }
}
    fun parse(src: String): List<Command> {
        return src.lines()
            .filter { it.isNotBlank() }
            .map { line ->
                val parts = line.split(" ", limit = 2)
                Command(parts[0], parts.getOrNull(1))
            }
    }
}
data class Command(val name: String, val arg: String?)
