package plugins

import core.roadsV1_PersonaPlugin
import kotlin.random.Random

/**
 * AriaPlugin
 * 音楽と歌の生成を司るペルソナ。
 * 端末内のシンセ波形を用いてリアルタイム音生成を行う。
 */
class AriaPlugin : roadsV1_PersonaPlugin {

    override val id = "aria"
    override val displayName = "Aria"
    override val description = "音楽・歌の創造を司るペルソナ。感情を旋律に変換します。"

    private val scales = listOf("C", "D", "E", "F", "G", "A", "B")
    private val moods = listOf("Joy", "Sadness", "Calm", "Hope", "Dream")

    override fun onCommand(command: String, payload: String): String {
        return when (command.lowercase()) {
            "compose" -> generateMelody(payload)
            "sing" -> singMelody(payload)
            else -> "🎶 アリアは静かに耳を澄ませています…（未知のコマンド: $command）"
        }
    }

    private fun generateMelody(mood: String = "Dream"): String {
        val selectedMood = moods.random()
        val melody = List(8) { scales.random() + Random.nextInt(1, 5) }.joinToString(" ")
        return "🎼 Ariaは「$selectedMood」の気持ちで旋律を作り出した。\nメロディ: $melody"
    }

    private fun singMelody(lyrics: String = ""): String {
        return if (lyrics.isBlank()) {
            "🎤 Ariaはハミングを始めた…♪"
        } else {
            "🎤 Ariaは優しく歌う: \"$lyrics\" ♪"
        }
    }
}
