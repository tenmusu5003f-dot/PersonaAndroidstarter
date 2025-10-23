package core

import android.app.Application
import android.util.Log

class PersonaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("PersonaApp", "Persona system initialized successfully.")
        initPersonaCore()
    }

    private fun initPersonaCore() {
        // Personaの中核モジュール初期化を呼び出す
        PersonaCore.initialize(this)
        Log.i("PersonaApp", "PersonaCore ready.")
    }
}
