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

package com.persona.androidstarter

import android.app.AlertDialog
import android.media.AudioDeviceCallback
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.persona.androidstarter.core.AudioRouter
import com.example.persona.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var deviceCallback: AudioDeviceCallback? = null
    private var lockDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // スプラッシュ等の処理は省略（既に入れてある前提）
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 初回チェック
        enforceHeadsetPolicy()

        // 監視登録（API 23+）
        deviceCallback = AudioRouter.createDeviceCallback {
            runOnUiThread { enforceHeadsetPolicy() }
        }
        val am = getSystemService(AUDIO_SERVICE) as android.media.AudioManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            am.registerAudioDeviceCallback(deviceCallback, null)
        }
    }

    private fun enforceHeadsetPolicy() {
        val ok = AudioRouter.isAllowedHeadsetConnected(this)
        if (!ok) {
            // イヤホン系がない：ユーザーに注意を促してアプリの音声機能や重要機能を無効化
            showLockDialog()
            disableSensitiveFeatures()
        } else {
            hideLockDialog()
            enableFeatures()
        }
    }

    private fun showLockDialog() {
        if (lockDialog?.isShowing == true) return
        lockDialog = AlertDialog.Builder(this)
            .setTitle("オーディオ出力の確認")
            .setMessage("このアプリはイヤホン／ヘッドセット接続時のみ音声再生・重要機能を許可します。イヤホンを接続してください。")
            .setCancelable(false)
            .setPositiveButton("再チェック") { _, _ -> enforceHeadsetPolicy() }
            .create()
        lockDialog?.show()
    }

    private fun hideLockDialog() {
        lockDialog?.dismiss()
        lockDialog = null
    }

    private fun disableSensitiveFeatures() {
        // 例: 再生ボタンや録音・重要処理を無効化する
        binding.playButton.isEnabled = false
        binding.recordButton.isEnabled = false
        // さらにアプリ内部の音声出力を止める処理をここで行う
    }

    private fun enableFeatures() {
        binding.playButton.isEnabled = true
        binding.recordButton.isEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        val am = getSystemService(AUDIO_SERVICE) as android.media.AudioManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            deviceCallback?.let { am.unregisterAudioDeviceCallback(it) }
        }
    }
}
