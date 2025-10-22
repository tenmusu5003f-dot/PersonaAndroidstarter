package com.example.persona.core
class PersonaMemory {
    private val kv = mutableMapOf<String, String>()
    fun remember(key: String, value: String) { kv[key] = value }
    fun recall(key: String): String? = kv[key]
}
