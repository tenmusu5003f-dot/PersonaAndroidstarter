package com.persona.androidstarter
import android.app.Application
import android.util.Log

class PersonaApp : Application() {
  override fun onCreate() {
    super.onCreate()
    Log.i("PersonaApp","Persona system boot")
    PersonaCore.initialize(this) // ← 既存コア初期化
  }
}

