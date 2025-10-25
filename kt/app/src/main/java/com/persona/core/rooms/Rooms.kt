package com.persona.core.rooms

import com.persona.core.persona.Persona
import com.persona.core.persona.Personas

data class Room(
    val id: String,
    val title: String,
    val members: List<Persona>
)

object Rooms {
    // 最初の集合部屋：全員集合
    val lobby: Room by lazy {
        Room(id = "lobby", title = "Lobby", members = Personas.all)
    }
}
