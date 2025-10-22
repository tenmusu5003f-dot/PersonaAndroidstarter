package com.example.persona.core
class PersonaSecurity {
    fun sanitize(input: String): String =
        input.replace(Regex("[\\n\\r\\t]"), " ").trim().take(64)
}
