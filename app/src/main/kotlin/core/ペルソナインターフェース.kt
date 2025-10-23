package core

// Persona Interface – base definitions for persona plug-ins
interface roadsV1_PersonaPlugin {
    val id: String
    fun onGreet(): String = "……"
    fun onCommand(command: String, payload: String? = null): String = "……"
}

object roadsV1_PluginRegistry {
    private val map = linkedMapOf<String, roadsV1_PersonaPlugin>()
    fun register(p: roadsV1_PersonaPlugin) { map[p.id] = p }
    fun get(id: String): roadsV1_PersonaPlugin? = map[id]
    fun all(): List<roadsV1_PersonaPlugin> = map.values.toList()
}
