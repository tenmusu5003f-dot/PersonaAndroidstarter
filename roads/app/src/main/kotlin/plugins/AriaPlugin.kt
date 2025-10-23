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

import core.lyrics.LyricGenerator
...
val lyrics = LyricGenerator.composeSong("夜空")

package core.lyrics

import kotlin.random.Random

/**
 * LyricGenerator
 * - アリアの歌詞を即興生成する簡易テキストユーティリティ
 * - 外部通信なし / 組み込み辞書のみ使用
 *
 * 使用例:
 *   val line = LyricGenerator.compose("夜空")
 *   // => 「夜空の果てに、光はまだ瞬いている」
 */
object LyricGenerator {

    private val subjects = listOf(
        "風", "星", "夢", "夜空", "心", "記憶", "海", "孤独", "希望", "約束"
    )

    private val verbs = listOf(
        "囁く", "揺れる", "舞う", "沈む", "響く", "願う", "漂う", "滲む", "祈る", "流れる"
    )

    private val endings = listOf(
        "永遠に", "儚く", "静かに", "もう一度", "遠くで", "優しく", "まだここに", "忘れられず", "あなたへ", "彼方へ"
    )

    private val patterns = listOf(
        "{subject}が{verb}{ending}",
        "{subject}の中で{verb}",
        "{subject}は{ending}",
        "{subject}よ、{verb}",
        "{subject}に{verb}{ending}"
    )

    /** テーマワードを指定して詩的な一行を生成する */
    fun compose(theme: String? = null): String {
        val subject = theme?.takeIf { it.isNotBlank() } ?: subjects.random()
        val verb = verbs.random()
        val ending = endings.random()
        val pattern = patterns.random()

        return pattern
            .replace("{subject}", subject)
            .replace("{verb}", verb)
            .replace("{ending}", ending)
    }

    /** 複数行をまとめて生成（曲中セクション用） */
    fun composeLines(theme: String? = null, count: Int = 4): List<String> {
        return List(count.coerceAtLeast(1)) { compose(theme) }
    }

    /** 歌詞全体をまとめた一つの文字列として返す */
    fun composeSong(theme: String? = null, verses: Int = 2, linesPerVerse: Int = 4): String {
        val builder = StringBuilder()
        repeat(verses.coerceAtLeast(1)) { v ->
            composeLines(theme, linesPerVerse).forEach { builder.appendLine(it) }
            if (v < verses - 1) builder.appendLine()
        }
        return builder.toString().trim()
    }
}
