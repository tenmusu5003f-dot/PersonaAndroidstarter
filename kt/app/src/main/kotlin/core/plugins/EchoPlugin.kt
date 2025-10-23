package core.plugins

import core.roadsV1_PersonaPlugin
import core.roadsV1_MemoryStore
import core.roadsV1_replyFor
import core.roadsV1_randomGreeting

/**
 * EchoPlugin
 * -------------------------------------------------
 * サンプルのペルソナ実装。IDは "Echo" 固定。
 * DuplicateGuard 経由で登録され、二重定義を安全に回避する。
 */
class EchoPlugin : roadsV1_PersonaPlugin {
    override val id: String = "Echo"

    override fun onGreet(): String {
        roadsV1_MemoryStore.remember(id, "greeted", persist = false)
        return roadsV1_randomGreeting("エコー")
    }

    override fun onCommand(command: String, payload: String?): String {
        roadsV1_MemoryStore.remember(id, "cmd:$command", persist = false)
        return roadsV1_replyFor(command, "共通")
    }
}
