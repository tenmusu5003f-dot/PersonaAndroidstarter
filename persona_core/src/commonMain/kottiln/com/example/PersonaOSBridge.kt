package com.example.persona.core
interface PersonaOSBridge {
    fun log(message: String)
    fun nowMillis(): Long
}
class DefaultOSBridge : PersonaOSBridge {
    override fun log(message: String) { /* no-op */ }
    override fun nowMillis(): Long = 0L
}
