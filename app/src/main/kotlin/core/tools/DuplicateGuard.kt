package core.tools

import core.roadsV1_PersonaPlugin
import core.roadsV1_PluginRegistry

/**
 * DuplicateGuard
 * -------------------------------------------------
 * ランタイムで「重複登録」を検知して自動で無効化するガード。
 * - 同一IDの PersonaPlugin が複数来た場合、先勝ち or 後勝ちを選べる
 * - ログだけ残し、クラッシュを避ける
 */
object DuplicateGuard {

    enum class Policy { KEEP_FIRST, KEEP_LAST }

    private var policy: Policy = Policy.KEEP_FIRST
    private val seen = linkedSetOf<String>()

    fun configure(policy: Policy = Policy.KEEP_FIRST) {
        this.policy = policy
        seen.clear()
    }

    /**
     * Plugin 登録時に呼び出して、重複IDを自動で無視 or 差し替えする。
     * 返り値: 実際に登録されたかどうか
     */
    fun safeRegister(plugin: roadsV1_PersonaPlugin): Boolean {
        val id = plugin.id
        val existed = seen.contains(id)

        return when {
            !existed && policy == Policy.KEEP_FIRST -> {
                seen += id
                roadsV1_PluginRegistry.register(plugin)
                log("register[$id] ✓ keep-first")
                true
            }
            existed && policy == Policy.KEEP_FIRST -> {
                log("duplicate[$id] ignored (keep-first)")
                false
            }
            !existed && policy == Policy.KEEP_LAST -> {
                seen += id
                roadsV1_PluginRegistry.register(plugin)
                log("register[$id] ✓ keep-last (first)")
                true
            }
            else /* existed && KEEP_LAST */ -> {
                // 既存を差し替える
                roadsV1_PluginRegistry.register(plugin)
                log("duplicate[$id] replaced (keep-last)")
                true
            }
        }
    }

    private fun log(msg: String) = println("🛡️ DuplicateGuard: $msg")
}
