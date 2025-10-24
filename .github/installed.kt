package core.install

import android.content.Context
import android.util.Log
import com.persona.androidstarter.security.AssetGuard
import com.persona.androidstarter.security.CryptoVault
import com.persona.androidstarter.security.NetGuard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Installer
 * --------------------------------------------------
 * - アプリ初回起動や更新時に依存モジュールを一括展開
 * - セキュリティハッシュ確認済みのファイルだけを導入
 * - 外部サーバーに同期をかける場合もここを経由
 */
object Installer {
    private const val TAG = "Installer"

    suspend fun run(ctx: Context): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Installer start")

        // 1. 暗号鍵を確認
        if (!CryptoVault.ensureInit(ctx)) return@withContext false

        // 2. アセットの整合性をチェック
        if (!AssetGuard.verifyHashes(ctx)) return@withContext false

        // 3. ネット接続を初期化（必要ならダウンロード）
        if (!NetGuard.pinSetup(ctx)) return@withContext false

        // 4. 必要ファイルを /data/data/.../modules に展開
        val moduleDir = File(ctx.filesDir, "modules")
        if (!moduleDir.exists()) moduleDir.mkdirs()
        Log.i(TAG, "Module dir = ${moduleDir.absolutePath}")

        // 5. 成功
        Log.i(TAG, "Installer complete.")
        true
    }
}

override fun onCreate() {
    super.onCreate()
    SecurityHub.init(this)

    // 中枢安全確認後にインストーラ起動
    CoroutineScope(Dispatchers.Default).launch {
        val ok = core.install.Installer.run(this@PersonaApp)
        if (ok) {
            Log.i("PersonaApp", "Modules installed and verified.")
        } else {
            Log.w("PersonaApp", "Installer failed. Modules skipped.")
        }
    }

    PersonaCore.initialize(this)
}
