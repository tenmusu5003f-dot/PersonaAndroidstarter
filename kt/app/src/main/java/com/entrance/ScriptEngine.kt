// ScriptSandbox.kt
package com.persona.script

import kotlinx.coroutines.withTimeout
import android.util.Log

class ScriptSandbox {
  suspend inline fun guard(timeoutMs: Long = 3000, block: ()->Unit) {
    try {
      withTimeout(timeoutMs) { block() }
    } catch (e: Exception) {
      Log.e("ScriptSandbox", "Error: ${e.message}")
      // throw or handle
    }
  }
}

// ScriptEngine.kt
package com.persona.script

class ScriptEngine(private val sandbox: ScriptSandbox = ScriptSandbox()) {
  fun run(scriptText: String) {
    // 解析は簡易に。実運用ではASTで厳密に解析する
    val ast = ScriptParser.parse(scriptText)
    sandbox.guard {
      ScriptExecutor.execute(ast)
    }
  }
}

// ScriptParser / ScriptExecutor は君の既存仕様に合わせて実装してね
