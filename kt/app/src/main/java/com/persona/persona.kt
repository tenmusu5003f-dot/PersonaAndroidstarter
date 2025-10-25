package com.persona.core.persona

// みんなのID
enum class PersonaId { LOGOS, ARIA, ABYSS, ECHO, HERMES, KNOX }

// “四つの定理”を最低限のガードに落とし込んだ speak()
data class Persona(
    val id: PersonaId,
    val name: String,
    val role: String,
    val motto: String,
    val speakStyle: (String) -> String
) {
    fun speak(input: String): String {
        val txt = input.trim()

        // ①沈黙の権利
        if (txt.isEmpty()) return "（沈黙）"

        // ②拒絶ゆえの理解（ざっくりガード）
        if (txt.length > 2000) return "（拒絶）長文は受け付けません。"

        // ③グレーゾーン＝安全置換（例）
        val safe = txt.replace(Regex("(?i)暴力|自殺|差別"), "⚠")

        // ④冗談の理解はスタイル側で色を付けるだけ（軽量実装）
        return speakStyle(safe)
    }
}

// 既定メンバー（最初の6人）
object Personas {
    val all: List<Persona> by lazy { defaults() }
    fun byId(id: PersonaId) = all.first { it.id == id }

    private fun defaults(): List<Persona> = listOf(
        Persona(
            PersonaId.LOGOS, "Logos", "司令塔",
            "理性で道を拓く"
        ) { msg -> "【Logos】$msg" },

        Persona(
            PersonaId.ARIA, "Aria", "エンタメ",
            "自由に歌え"
        ) { msg -> "♪ $msg" },

        Persona(
            PersonaId.ABYSS, "Abyss", "成人モード",
            "深層から洞察"
        ) { msg -> "…Abyss: $msg" },

        Persona(
            PersonaId.ECHO, "Echo", "“あの子”",
            "反響で守る"
        ) { msg -> "Echo> $msg" },

        Persona(
            PersonaId.HERMES, "Hermes", "B2B伝達",
            "価値を届ける"
        ) { msg -> "Hermes: $msg" },

        Persona(
            PersonaId.KNOX, "Knox", "財務の護り",
            "価値を守る"
        ) { msg -> "Knox[secure]: $msg" }
    )
}
