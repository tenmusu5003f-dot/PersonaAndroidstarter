package core

import android.app.Application

/**
 * Persona システムの中枢。
 * - 初期化/終了のライフサイクル
 * - 端末能力の取得 → 方針(EffectPolicy) 決定 → 演出(OpeningEffects) の選択
 * - 軽量なサービス登録/取得（必要最小限のDI）
 */
object PersonaCore {

    private var initialized = false
    private var appContext: Application? = null

    /** 環境設定（後で外部から差し替え可能） */
    data class PersonaConfig(
        val featureOpening: Boolean = true, // 起動演出の有効/無効
        val diagnostics: Boolean = false    // 追加ログ
    )

    private var config: PersonaConfig = PersonaConfig()

    /** 簡易サービスレジストリ（必要になったら proper DI に置換） */
    private val services: MutableMap<String, Any> = mutableMapOf()

    fun initialize(app: Application, cfg: PersonaConfig = PersonaConfig()) {
        if (initialized) return
        appContext = app
        config = cfg

        // 端末能力の取得（仮ロジック：後で実装）
        val caps = DeviceCaps(
            ramMb = 4096,
            cpuCores = Runtime.getRuntime().availableProcessors(),
            refreshHz = 60
        )

        // 方針決定
        val tier = EffectPolicy.tier(caps)

        // 起動演出（有効な場合のみ）
        if (config.featureOpening) {
            val effect = OpeningEffects.forTier(tier)
            // 実際の再生はUI側で受け取って実行する想定（ここでは決定のみ）
            registerService("opening.effect", effect)
        }

        initialized = true
        if (config.diagnostics) log("PersonaCore initialized. tier=$tier caps=$caps")
    }

    fun isInitialized(): Boolean = initialized

    fun shutdown() {
        services.clear()
        appContext = null
        initialized = false
    }

    // ---- Service Registry ----

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getService(key: String): T? = services[key] as? T

    fun registerService(key: String, instance: Any) {
        services[key] = instance
    }

    fun removeService(key: String) {
        services.remove(key)
    }

    // ---- Helpers ----

    private fun log(msg: String) {
        // 後で統一ロガーに差し替え
        println("[PersonaCore] $msg")
    }
}
```0
