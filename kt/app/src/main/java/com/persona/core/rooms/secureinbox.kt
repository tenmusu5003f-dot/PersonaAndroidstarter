package core.rooms

import com.persona.androidstarter.security.SecureChannel

/**
 * SecureInbox
 * - 各ペルソナルームに置く受信専用ポスト
 * - set() は SecurityHub 側（中枢）だけが呼ぶ想定
 */
class SecureInbox {
    @Volatile private var last: SecureChannel.Envelope? = null

    fun set(env: SecureChannel.Envelope) { last = env }

    /** 取り出し：検証に通らない場合は null */
    fun pollVerified(): ByteArray? {
        val e = last ?: return null
        return SecureChannel.verifyAndUnwrap(e)
    }
}
