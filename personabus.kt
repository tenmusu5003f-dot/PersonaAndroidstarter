package core.bus

import com.persona.androidstarter.security.SecureChannel
import kotlinx.coroutines.flow.*

/**
 * PersonaBus:
 * 全ペルソナ共通の受信バス。SecurityHub からのみ set() できる。
 * 各ルームは subscribe(topic) で購読。
 */
object PersonaBus {
    private val _stream = MutableSharedFlow<SecureChannel.Envelope>(
        replay = 1, extraBufferCapacity = 8
    )

    /** SecurityHub から流す（送信）。 */
    fun dispatch(env: SecureChannel.Envelope) {
        _stream.tryEmit(env)
    }

    /** 各ペルソナが購読。 */
    fun subscribe(topic: String): Flow<ByteArray> = _stream
        .filter { it.topic == topic }
        .mapNotNull { SecureChannel.verifyAndUnwrap(it) }
}
