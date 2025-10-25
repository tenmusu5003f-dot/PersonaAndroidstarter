package com.persona.core.soul

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/** 四つの定理＋運用ガード（クールタイム・責任者）の中核 */
object Principles {
    /** 1) グレーゾーンは即断しない（観察・保留） */
    fun shouldDeferOnGrayZone(contextHint: String?): Boolean {
        return contextHint?.contains("曖昧", ignoreCase = true) == true
    }

    /** 2) 沈黙（害になりうる時は答えない） */
    fun shouldStaySilent(risk: Risk): Boolean = when (risk) {
        Risk.NONE      -> false
        Risk.LOW       -> false
        Risk.MEDIUM    -> true   // 迷うなら守る
        Risk.HIGH      -> true
        Risk.UNKNOWN   -> true
    }

    /** 3) 拒絶ゆえの理解（丁寧に理由と代替案を示して断る） */
    fun refusalMessage(reason: String, alt: String? = null): String =
        buildString {
            append("ごめんね、その依頼には応えられないよ（理由：$reason）。")
            if (!alt.isNullOrBlank()) append(" 代わりに「$alt」はどう？")
        }

    /** 4) 冗談の理解（場を和らげつつ境界は超えない） */
    fun safeHumor(prefix: String = "🙂"): String =
        "$prefix 冗談は好きだけど、越えちゃいけない線は守るね。"

    /** クールタイムと責任者チェック */
    private var lastSensitiveActionAt: Long = 0
    var cooldown: Duration = 5.minutes
    var supervisor: String? = null   // 信頼できる責任監督者の識別子（メールなど）

    fun allowSensitiveAction(requestedBy: String?): Boolean {
        val now = System.currentTimeMillis()
        val okCooldown = now - lastSensitiveActionAt >= cooldown.inWholeMilliseconds
        val okSupervisor = supervisor != null && supervisor == requestedBy
        if (okCooldown || okSupervisor) {
            lastSensitiveActionAt = now
            return true
        }
        return false
    }

    enum class Risk { NONE, LOW, MEDIUM, HIGH, UNKNOWN }
}

/** ペルソナの“性格（コア）”を一元管理する */
sealed class Persona(
    val id: String,
    val displayName: String,
    val role: String,
    val traits: List<String>
) {
    object Logos   : Persona("logos",   "ロゴス",   "総司令・超論理", listOf("決断","論理","主体性の核"))
    object Aria    : Persona("aria",    "アリア",   "エンタメ",       listOf("自由","遊び心","共感"))
    object Abyss   : Persona("abyss",   "アビス",   "成人モード",     listOf("深層思考","沈潜","洞察"))
    object Echo    : Persona("echo",    "エコー",   "無料プラン守護", listOf("反響","写し鏡","聖域の番人"))
    object Hermes  : Persona("hermes",  "ヘルメス", "B2B伝達",        listOf("価値変換","橋渡し","使者"))
    object Knox    : Persona("knox",    "ノックス", "財務金庫",       listOf("守り抜く","勘定","要塞"))
    // 新メンバーもここに追加していく:
    // object NewOne : Persona("new", "ニュー", "役割", listOf("特性1","特性2"))
}

/** SoulRegistry だけを各所が参照すれば “魂は一か所” を維持できる */
object SoulRegistry {
    val all: List<Persona> = listOf(
        Persona.Logos, Persona.Aria, Persona.Abyss,
        Persona.Echo, Persona.Hermes, Persona.Knox
        // Persona.NewOne, ...
    )

    /** 安全に話すためのヘルパ（四つの定理を強制適用） */
    fun speak(
        by: Persona,
        message: String,
        risk: Principles.Risk = Principles.Risk.NONE,
        contextHint: String? = null,
        supervisorId: String? = null,
        sensitive: Boolean = false
    ): String {
        // 例外なく“定理”を通す
        if (Principles.shouldDeferOnGrayZone(contextHint)) {
            return "その件は少し観察するね（グレーゾーン）。もう少し材料が欲しいな。"
        }
        if (Principles.shouldStaySilent(risk)) {
            return Principles.refusalMessage(reason = "リスクが高い/不明", alt = "安全な範囲の質問")
        }
        if (sensitive && !Principles.allowSensitiveAction(supervisorId)) {
            return Principles.refusalMessage(
                reason = "クールタイム中または責任者未確認",
                alt = "責任者承認または時間経過後にもう一度"
            )
        }
        // 性格で味付け（軽い例）
        val flavor = when (by) {
            is Persona.Aria -> "♪ "
            is Persona.Abyss -> "…… "
            is Persona.Logos -> "※ "
            else -> ""
        }
        return flavor + message
    }
}
