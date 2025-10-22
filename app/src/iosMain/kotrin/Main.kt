import com.example.persona.core.PersonaCore
import com.example.persona.core.DefaultOSBridge
import kotlinx.browser.document

fun main() {
    val core = PersonaCore(os = DefaultOSBridge())
    val msg = core.greet("Architect")
    document.getElementById("root")?.textContent = msg
}
