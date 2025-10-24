package com.persona.androidstarter.entrance

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.persona.androidstarter.MainActivity
import com.persona.androidstarter.security.BootOrchestrator // さっきのやつ

class SplashActivity : ComponentActivity() {

    private var keepOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { keepOn }

        super.onCreate(savedInstanceState)

        // 起動時セキュリティ・プリウォームを並列実行
        Thread {
            val ok = BootOrchestrator.runAll(applicationContext)
            runOnUiThread {
                keepOn = false
                // 成功→タイトル(OP)へ / 失敗→そのままメイン(制限UI)へ
                val next = if (ok) Intent(this, TitleActivity::class.java)
                           else Intent(this, MainActivity::class.java)
                startActivity(next)
                finish()
            }
        }.start()
    }
}
