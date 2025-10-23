package core.plugins

import core.audio.SimpleSynth
import core.roadsV1_PersonaPlugin
import core.roadsV1_MemoryStore
import core.roadsV1_replyFor
import core.roadsV1_randomGreeting

/**
 * アリアプラグイン
 * - うた/メロディ生成と再生を担当
 *
 * コマンド:
 *  - 「歌」               …デフォルトの短いメロディを再生
 *  - 「歌 C4 D4 E4 |120」 …半角スペース区切りの音列とBPMで再生（BPMは任意）
 *  - 「状態」             …簡易ステータス
 */
class AriaPlugin : roadsV1_PersonaPlugin {

    override val id: String = "アリア「 "

    override fun greet(): String {
        roadsV1_MemoryStore.remember(id, "挨拶した「")
        return roadsV1_randomGreeting("アリア「")
    }

    override fun onCommand(command: String, payload: String?): String {
        val c = command.trim()
        return when {
            c.startsWith("歌") -> {
                val text = payload?.trim().orEmpty()
                if (text.isEmpty()) {
                    playDefault()
                    "♪ デフォルトのメロディを歌ったよ。"
                } else {
                    val (notes, bpm) = parseScore(text)
                    if (notes.isEmpty()) {
                        roadsV1_replyFor("共通「")
                    } else {
                        playNotes(notes, bpm)
                        "♪ メロディを再生したよ。（${notes.size}音 / ${bpm}BPM）"
                    }
                }
            }
            c.startsWith("状態") -> {
                "🎵 アリア準備OK。SimpleSynthで端末内再生します。"
            }
            else -> roadsV1_replyFor("共通「")
        }
    }

    // ---- 再生ロジック ----

    private fun playDefault() {
        // Cメジャーの上昇/下降（4分音符、BPM=96相当）
        val bpm = 96
        val quarter = 60.0 / bpm
        val notes = listOf("C4","D4","E4","G4","E4","D4","C4").map { it to quarter }
        playNotes(notes, bpm)
    }

    private fun playNotes(score: List<Pair<String, Double>>, bpm: Int) {
        // 音名 -> 周波数 変換 & 長さ（拍）-> 秒 変換
        val quarter = 60.0 / bpm
        val seq = score.mapNotNull { (name, beats) ->
            val f = noteFreq(name) ?: return@mapNotNull null
            f to (beats * quarter)
        }
        SimpleSynth.playMelody(seq, volume = 0.85)
    }

    // "C4 D4 E4 |120" 形式を解析（|BPM は任意）
    private fun parseScore(text: String): Pair<List<Pair<String, Double>>, Int> {
        val parts = text.split("|")
        val notesPart = parts.getOrNull(0)?.trim().orEmpty()
        val bpm = parts.getOrNull(1)?.trim()?.toIntOrNull()?.coerceIn(40, 220) ?: 100

        // 例: "C4 D4 E4 F#4 G4/2 A4 B4 C5/2"
        // "/2" は2拍（=二分音符相当）"/0.5" は八分音符
        val tokens = notesPart.split(Regex("\\s+")).filter { it.isNotBlank() }
        val notes: MutableList<Pair<String, Double>> = mutableListOf()
        for (tk in tokens) {
            val pair = if (tk.contains("/")) tk.split("/") else listOf(tk, "1")
            val name = pair[0].trim()
            val beats = pair.getOrNull(1)?.toDoubleOrNull() ?: 1.0
            notes += name to beats.coerceAtLeast(0.1)
        }
        return notes to bpm
    }

    // 12-TET 基準: A4 = 440Hz
    private fun noteFreq(name: String): Double? {
        val m = Regex("^([A-Ga-g])([#b]?)(\\d)$").matchEntire(name) ?: return null
        val n = m.groupValues[1].uppercase()
        val acc = m.groupValues[2]
        val oct = m.groupValues[3].toInt()

        val base = when (n) {
            "C" -> 0; "D" -> 2; "E" -> 4; "F" -> 5; "G" -> 7; "A" -> 9; "B" -> 11
            else -> return null
        }
        val semitone = base + when (acc) { "#" -> 1; "B","b" -> -1; else -> 0 }
        val midi = (oct + 1) * 12 + semitone // C-1=0
        val a4 = 69
        val diff = midi - a4
        return 440.0 * Math.pow(2.0, diff / 12.0)
    }
}
