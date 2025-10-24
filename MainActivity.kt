package com.persona.androidstarter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.persona.androidstarter.security.SessionManager
import com.persona.androidstarter.security.BootOrchestrator
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
  private lateinit var session: SessionManager
  private val uiScope = CoroutineScope(Dispatchers.Main + Job())

  override fun onCreate(b: Bundle?) {
    super.onCreate(b)
    setContentView(R.layout.activity_main) // FragmentContainerView を含む
    session = SessionManager(this)
  }

  override fun onStart() {
    super.onStart()
    // NavHost を find して AuthGate を適用（AuthGate 内で nav.navigate）
    uiScope.launch {
      BootOrchestrator.start(this@MainActivity, onReady = {
        // 起動成功：そのまま進む
      }, onFail = {
        // フォールバック処理（詳細は設計次第）
      })
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    uiScope.cancel()
  }
}
