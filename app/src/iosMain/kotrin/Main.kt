import com.example.persona.core.*

fun main() {
    val core = PersonaCore()
    val msg = core.greet("Architect")
    println(">>> $msg")
    document.getElementById("root")?.textContent = msg
}
