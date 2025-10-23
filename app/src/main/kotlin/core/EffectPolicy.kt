package core

/**
 * EffectPolicy
 * -------------------------------------------------
 * デバイス性能・バッテリー残量・安全モードなどに応じて
 * 自動的に描画演出の強度を調整するポリシー。
 * PersonaSecurity や PersonaOSBridge から参照される。
 */
object EffectPolicy {

    /** 演出強度を4段階で管理 */
    enum class EffectTier {
        OFF,        // 🔸最小演出（セキュリティ／省電力モード）
        LIGHT,      // 🔸軽負荷端末向け
        NORMAL,     // 🔹標準（デフォルト）
        FULL        // 💎ハイエンド端末向け、全演出有効
    }

    /** 現在の適用ティア（起動時はNORMAL） */
    private var currentTier: EffectTier = EffectTier.NORMAL

    /** 現在のティアを返す */
    fun tier(): EffectTier = currentTier

    /**
     * デバイス情報と安全性を考慮して自動調整。
     * PersonaOSBridge や PersonaSecurity から呼び出されることを想定。
     */
    fun autoAdjust(os: Map<String, String>, securitySafe: Boolean, batteryLevel: Int?): EffectTier {
        val sdk = os["sdk"]?.toIntOrNull() ?: 33
        val brand = os["brand"]?.lowercase() ?: "unknown"

        val lowBattery = (batteryLevel ?: 100) < 20
        val lowSpec = sdk < 29 || brand.contains("generic") || brand.contains("emulator")

        currentTier = when {
            !securitySafe || lowBattery -> EffectTier.OFF
            lowSpec -> EffectTier.LIGHT
            sdk in 29..32 -> EffectTier.NORMAL
            else -> EffectTier.FULL
        }

        log("auto-adjust → $currentTier (sdk=$sdk, brand=$brand, battery=$batteryLevel, safe=$securitySafe)")
        return currentTier
    }

    /** 手動設定も可能 */
    fun setTier(tier: EffectTier) {
        currentTier = tier
        log("tier manually set → $tier")
    }

    private fun log(msg: String) {
        println("[EffectPolicy] $msg")
    }
}
