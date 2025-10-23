package core.silence

/**
 * Reasonable Silence = understand but *choose* to withhold output briefly.
 * This is not an error; it's a designed state to protect human affect.
 */
data class SilenceInput(
    val emotionalResonance: Double,   // 0.0 - 1.0 (higher = stronger emotions detected)
    val userFatigueHint: Double,      // 0.0 - 1.0 (typing pace, pause, retries)
    val riskTopic: Boolean,           // safety/trauma-ish topic heuristic
    val recentModelOvertalk: Boolean, // we spoke too much recently?
    val latencyPressure: Boolean      // system under load? (avoid piling more tokens)
)

data class SilenceDecision(
    val shouldSilence: Boolean,
    val durationMs: Long,
    val reason: String
)

object SilenceProtocol {

    // Tunable thresholds (could be surfaced to settings later)
    private const val RESONANCE_TR = 0.72
    private const val FATIGUE_TR   = 0.60
    private const val BASE_MS      = 1200L
    private const val MAX_MS       = 5000L

    fun decide(input: SilenceInput): SilenceDecision {
        var score = 0.0
        if (input.emotionalResonance >= RESONANCE_TR) score += 0.6
        if (input.userFatigueHint   >= FATIGUE_TR)    score += 0.25
        if (input.riskTopic)                           score += 0.15
        if (input.recentModelOvertalk)                 score += 0.1
        if (input.latencyPressure)                     score += 0.05

        val should = score >= 0.6
        val factor = (score.coerceIn(0.0, 1.0))
        val ms = (BASE_MS + (MAX_MS - BASE_MS) * factor).toLong()

        val reason = when {
            input.emotionalResonance >= RESONANCE_TR && input.userFatigueHint >= FATIGUE_TR ->
                "empathy+fatigue"
            input.emotionalResonance >= RESONANCE_TR -> "empathy"
            input.riskTopic -> "risk-topic"
            input.recentModelOvertalk -> "overtalk"
            input.latencyPressure -> "system-pressure"
            else -> "none"
        }

        return SilenceDecision(should, if (should) ms else 0L, reason)
    }
}
