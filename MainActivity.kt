val result = ActionEngine.broadcast("起動成功。世界に届けよう！")
text = "ここにいるよ：$names\n\n---\n$result"

<activity android:name="com.persona.androidstarter.MainActivity" ...>

package com.persona.androidstarter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.persona.androidstarter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView.text = "Hello Persona!"
    }
}

// MainActivity の onCreate 末尾あたりで
val okStore = Security.isFromPlayStore(this)
val okSig   = Security.isSignatureValid(this, expectedSha256 = "AA:BB:...:ZZ") // ←後で設定
val envOK   = !Security.hasSuspiciousEnvironment()

// ここでは“止めない”でログだけ。最終的に要件に応じて挙動を決める。
android.util.Log.i("PersonaSec",
    "store=$okStore, sig=$okSig, envOK=$envOK, debug=${Security.isDebuggable(this)}")
