package core.audio

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin

/**
 * SimpleSynth
 * - 追加ライブラリ無しで端末内にPCM波形を生成・再生する極小ユーティリティ
 * - サンプルレート: 44.1kHz / モノラル / 16-bit
 *
 * 使い方:
 *   SimpleSynth.playMelody(listOf(440.0 to 0.4, 494.0 to 0.4, 523.25 to 0.8))
 */
object SimpleSynth {

    private const val SAMPLE_RATE = 44100

    /** 単音を再生（周波数[Hz], 長さ[秒], 音量0.0..1.0） */
    fun playTone(frequencyHz: Double, seconds: Double, volume: Double = 0.8) {
        val totalSamples = (SAMPLE_RATE * seconds).toInt().coerceAtLeast(1)
        val buffer = ShortArray(totalSamples)

        // 簡易エンベロープ（クリックノイズ低減）
        val attack = (0.01 * SAMPLE_RATE).toInt().coerceAtMost(totalSamples / 10)
        val release = (0.02 * SAMPLE_RATE).toInt().coerceAtMost(totalSamples / 8)

        val twoPiF = 2.0 * PI * frequencyHz
        for (i in 0 until totalSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val env = when {
                i < attack -> i.toDouble() / attack
                i > totalSamples - release -> (totalSamples - i).toDouble() / release
                else -> 1.0
            }
            val s = sin(twoPiF * t)
            buffer[i] = (s * env * volume * Short.MAX_VALUE).toInt().toShort()
        }

        playPcm(buffer)
    }

    /** 複数音をシーケンス再生（[周波数Hz] to [長さ秒] のリスト） */
    fun playMelody(notes: List<Pair<Double, Double>>, volume: Double = 0.8) {
        if (notes.isEmpty()) return
        // 連続再生のため一括PCM化
        val chunks = notes.map { (f, d) -> tonePcm(f, d, volume) }
        val total = chunks.sumOf { it.size }
        val buffer = ShortArray(total)
        var pos = 0
        for (c in chunks) {
            System.arraycopy(c, 0, buffer, pos, c.size)
            pos += c.size
        }
        playPcm(buffer)
    }

    // ---- 内部実装 ----

    private fun tonePcm(frequencyHz: Double, seconds: Double, volume: Double): ShortArray {
        val totalSamples = (SAMPLE_RATE * seconds).toInt().coerceAtLeast(1)
        val buffer = ShortArray(totalSamples)
        val attack = (0.01 * SAMPLE_RATE).toInt().coerceAtMost(totalSamples / 10)
        val release = (0.02 * SAMPLE_RATE).toInt().coerceAtMost(totalSamples / 8)
        val twoPiF = 2.0 * PI * frequencyHz
        for (i in 0 until totalSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val env = when {
                i < attack -> i.toDouble() / attack
                i > totalSamples - release -> (totalSamples - i).toDouble() / release
                else -> 1.0
            }
            val s = sin(twoPiF * t)
            buffer[i] = (s * env * volume * Short.MAX_VALUE).toInt().toShort()
        }
        return buffer
    }

    private fun playPcm(buffer: ShortArray) {
        val minBuf = AudioTrack.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val track = AudioTrack(
            AudioManager.STREAM_MUSIC,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            maxOf(minBuf, buffer.size * 2),
            AudioTrack.MODE_STATIC
        )

        try {
            track.write(buffer, 0, buffer.size)
            track.play()
            // 再生完了まで待機（簡易）
            val millis = (buffer.size * 1000L / SAMPLE_RATE)
            Thread.sleep(millis + 20)
        } catch (_: InterruptedException) {
            // ignore
        } finally {
            track.stop()
            track.release()
        }
    }
}
