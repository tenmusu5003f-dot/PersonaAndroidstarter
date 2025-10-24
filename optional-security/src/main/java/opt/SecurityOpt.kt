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
