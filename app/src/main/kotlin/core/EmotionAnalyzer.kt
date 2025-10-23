package core

/**
 * 超軽量の感情解析スタブ。
 * 将来は分類器やルールセットへ差し替え可能な“接続点”だけ提供する。
 */
object EmotionAnalyzer {

    /**
     * とりあえずの経験則ルール。
     * - 将来: 正式なモデル/辞書/スコアリングに置換する
     */
    fun analyze(text: String): String {
        val t = text.lowercase()

        return when {
            listOf("ありがとう", "嬉しい", "すき", "love", "happy").any { t.contains(it) } -> "joy"
            listOf("つらい", "悲しい", "泣", "sad", "alone").any { t.contains(it) }      -> "sorrow"
            listOf("こわい", "怖い", "anxious", "不安").any { t.contains(it) }            -> "fear"
            listOf("怒", "むかつ", "angry", "許せ").any { t.contains(it) }               -> "anger"
            else -> "neutral"
        }
    }
}
