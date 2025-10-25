package com.persona.core.actions

import com.persona.core.persona.Persona
import com.persona.core.rooms.Rooms

// 各ペルソナの基本行動を定義
sealed class Action(open val actor: Persona) {
    data class Speak(override val actor: Persona, val message: String) : Action(actor)
    data class React(override val actor: Persona, val target: Persona, val emotion: String) : Action(actor)
}

// 実行ハブ
object ActionEngine {
    fun perform(action: Action): String = when (action) {
        is Action.Speak -> "${action.actor.name}：${action.actor.speak(action.message)}"
        is Action.React -> "${action.actor.name} → ${action.target.name} に「${action.emotion}」の反応"
    }

    // ロビー全員でメッセージを共有する
    fun broadcast(message: String): String {
        val room = Rooms.lobby
        val outputs = room.members.map { p ->
            Action.Speak(p, message).let { perform(it) }
        }
        return outputs.joinToString("\n")
    }
}
