package com.example.persona.data

import android.content.Context
import org.json.JSONObject

data class PersonaEntry(val id: String, val name: String, val mood: String, val lang: String)

object PersonaConfig {
    fun load(context: Context): List<PersonaEntry> {
        val json = context.assets.open("ai_config.json").bufferedReader().use { it.readText() }
        val root = JSONObject(json)
        val arr = root.getJSONArray("personas")
        return buildList {
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                add(
                    PersonaEntry(
                        id = o.getString("id"),
                        name = o.getString("name"),
                        mood = o.getString("mood"),
                        lang = o.optString("lang", "ja")
                    )
                )
            }
        }
    }
}
