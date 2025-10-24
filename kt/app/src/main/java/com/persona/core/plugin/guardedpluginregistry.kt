package core.plugins

import com.persona.androidstarter.security.SecurityHub

/**
 * GuardedPluginRegistry
 * - SecurityHub.safe==true のときだけ登録を許可
 * - DuplicateGuard と併用推奨
 */
object GuardedPluginRegistry {
    private val plugins = mutableListOf<Any>() // 型は実装側で安全に扱う

    fun safeRegister(p: Any): Boolean {
        if (!SecurityHub.isSafe()) return false
        if (plugins.any { it::class == p::class }) return false
        plugins += p
        return true
    }

    fun all(): List<Any> = plugins.toList()
}
