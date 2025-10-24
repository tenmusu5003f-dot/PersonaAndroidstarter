import android.app.Application
import android.util.Log
import com.persona.androidstarter.security.SecurityHub
import core.tools.DuplicateGuard
import core.plugins.EchoPlugin
import core.plugins.HermesPlugin
import core.plugins.NoxPlugin
import core.plugins.GuardedPluginRegistry

class PersonaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("PersonaApp", "Persona system initializing…")

        // ① セキュリティ中枢を先に立ち上げる（常時オンライン）
        SecurityHub.init(this)

        // ② プラグイン登録は“ガード”を通す
        DuplicateGuard.configure(policy = DuplicateGuard.Policy.KEEP_FIRST)
        GuardedPluginRegistry.safeRegister(EchoPlugin())
        GuardedPluginRegistry.safeRegister(HermesPlugin())
        GuardedPluginRegistry.safeRegister(NoxPlugin())

        // ③ PersonaCore 初期化（中枢の状態を見るだけにする）
        PersonaCore.initialize(this)
        Log.i("PersonaApp", "PersonaCore ready.")
    }
}
