package com.persona.androidstarter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.persona.androidstarter.security.*
import com.persona.androidstarter.nav.Routes
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var session: SessionManager
    private val ui = MainScope()

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_main)
        session = SessionManager(this)
    }

    override fun onStart() {
        super.onStart()
        val nav = (supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment).navController

        // 起動ガードを並列実行 → 失敗時はログインに寄せる
        ui.launch {
            val ok = withContext(Dispatchers.IO) { BootOrchestrator.runAll(this@MainActivity) }
            if (!ok) {
                AuthGate.enforce(nav, session) // 未サインイン or ガード失敗 → Login
            } else {
                if (session.isSignedIn()) {
                    nav.navigate(R.id.homeFragment)
                } else {
                    nav.navigate(R.id.loginFragment)
                }
            }
        }
    }

    override fun onDestroy() {
        ui.cancel()
        super.onDestroy()
    }
}
