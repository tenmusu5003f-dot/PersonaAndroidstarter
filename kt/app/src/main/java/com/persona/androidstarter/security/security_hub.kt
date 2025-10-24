package com.persona.androidstarter.security

import android.app.Application
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * SecurityHub
 * - 起動時に全ガードを実行して「安全状態」を配信
 * - Net/TTS/Crypto/Assets を一元初期化
 * - Persona側は SecurityHub からの「承認済みデータ」しか使わない
 */
object SecurityHub {
    private const val TAG = "SecurityHub"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    data class State(
        val safe: Boolean = false,
        val signatureValid: Boolean = false,
        val tamperOk: Boolean = false,
        val rootOk: Boolean = true,
        val assetsOk: Boolean = true,
        val netReady: Boolean = false,
        val ttsReady: Boolean = false
    )
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    fun init(app: Application) {
        scope.launch {
            Log.i(TAG, "init…")
            val sOk = SignatureGuard.verifySelf(app)
            val tOk = TamperGuard.checkBuildFlags(app)
            val rOk = RootGuard.quickScan(app)
            val cOk = CryptoVault.ensureInit(app)
            val aOk = AssetGuard.verifyHashes(app)
            val nOk = NetGuard.pinSetup(app)
            val ttsOk = TTSPrewarm.warm(app)

            val safeAll = sOk && tOk && rOk && cOk && aOk
            _state.value = State(
                safe = safeAll,
                signatureValid = sOk,
                tamperOk = tOk,
                rootOk = rOk,
                assetsOk = aOk,
                netReady = nOk,
                ttsReady = ttsOk
            )
            Log.i(TAG, "ready: ${_state.value}")
        }
    }

    fun isSafe(): Boolean = state.value.safe
}
