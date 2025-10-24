package core.persona.echo

import core.rooms.SecureInbox
import org.json.JSONObject

object EchoRoomBridge {
    val inbox = SecureInbox()

    /** ルームが呼ぶ：検証OKの時だけ JSON を返す */
    fun latestStatus(): JSONObject? {
        val bytes = inbox.pollVerified() ?: return null
        return runCatching { JSONObject(String(bytes)) }.getOrNull()
    }
}
