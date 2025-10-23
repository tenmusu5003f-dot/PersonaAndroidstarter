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

// --- roadsV1: plugin bootstrap (append) ---
import core.plugins.EchoPlugin
import core.tools.DuplicateGuard

private fun registerPersonaPlugins() {
    // 先勝ち or 後勝ちの方針を選択（重複に強い）
    DuplicateGuard.configure(policy = DuplicateGuard.Policy.KEEP_FIRST)

    // ここでプラグインを順次登録（将来は自動スキャンに差し替え可能）
    DuplicateGuard.safeRegister(EchoPlugin())

    // 例：追加するときはここに並べる（重複はGuardが対処）
    // DuplicateGuard.safeRegister(AbyssPlugin())
    // DuplicateGuard.safeRegister(LilithPlugin())
}

override fun onCreate() {
    super.onCreate()
    Log.i("PersonaApp", "Persona system initialized successfully.")
    initPersonaCore()
    registerPersonaPlugins() // ← ここで登録
}

import core.plugins.HermesPlugin

private fun registerPersonaPlugins() {
    DuplicateGuard.configure(policy = DuplicateGuard.Policy.KEEP_FIRST)

    // 基本プラグイン登録
    DuplicateGuard.safeRegister(EchoPlugin())
    DuplicateGuard.safeRegister(HermesPlugin()) // ← ここ追加
}

import core.plugins.NoxPlugin

private fun registerPersonaPlugins() {
    DuplicateGuard.configure(policy = DuplicateGuard.Policy.KEEP_FIRST)

    // 既存
    DuplicateGuard.safeRegister(EchoPlugin())
    DuplicateGuard.safeRegister(HermesPlugin())

    // 追加：Nox（夜間ガード）
    DuplicateGuard.safeRegister(NoxPlugin())
}
