package opt

object SecurityOptConfig {
    // 既定は全OFF（R8で消えるよう const 経由）
    const val EMULATOR   = BuildConfig.OPT_EMULATOR
    const val INTEGRITY  = BuildConfig.OPT_INTEGRITY
    const val HOOK_DEEP  = BuildConfig.OPT_HOOK_DEEP
}

object SecurityOpt {
    suspend fun run(ctx: android.content.Context): List<Pair<String, Boolean>> {
        val results = mutableListOf<Pair<String, Boolean>>()
        if (SecurityOptConfig.EMULATOR)   results += "emulator" to EmulatorGuard.detect(ctx)
        if (SecurityOptConfig.INTEGRITY)  results += "integrity" to PlayIntegrityGuard.check(ctx)
        if (SecurityOptConfig.HOOK_DEEP)  results += "hookDeep"  to HookDeepGuard.scan()
        return results
    }
}

/*** ダミー実装（必要になった時だけ中身を入れる） ***/
object EmulatorGuard { fun detect(ctx: android.content.Context) = false }
object PlayIntegrityGuard { fun check(ctx: android.content.Context) = true }
object HookDeepGuard { fun scan() = false }

data class OptPolicy(val emulator:Boolean=false, val integrity:Boolean=false, val hookDeep:Boolean=false)

object OptPolicyLoader {
    fun fetch(json: String?): OptPolicy? = try {
        json?.let { kotlinx.serialization.json.Json.decodeFromString(OptPolicy.serializer(), it) }
    } catch (_: Exception) { null }
}

fun applyRemotePolicy(p: OptPolicy?) {
    p ?: return
    // BuildConfigは書き換え不可なので“ランタイム上書きフラグ”を用意
    RuntimeFlag.EMULATOR  = RuntimeFlag.EMULATOR  || p.emulator
    RuntimeFlag.INTEGRITY = RuntimeFlag.INTEGRITY || p.integrity
    RuntimeFlag.HOOK_DEEP = RuntimeFlag.HOOK_DEEP || p.hookDeep
}
object RuntimeFlag {
    var EMULATOR=false; var INTEGRITY=false; var HOOK_DEEP=false
}

val enabled = SecurityOptConfig.EMULATOR || RuntimeFlag.EMULATOR

// 既存の最小コア後に、軽く並列で起動
async {
    if (RuntimeFlag.EMULATOR || RuntimeFlag.INTEGRITY || RuntimeFlag.HOOK_DEEP) {
        opt.SecurityOpt.run(ctx)  // 失敗してもフェイルセーフで続行
    }

}

# optional-security の公開APIだけ保持（中身はフラグfalseなら消える）
-keep class opt.SecurityOpt { *; }
-keep class opt.SecurityOptConfig { *; }
