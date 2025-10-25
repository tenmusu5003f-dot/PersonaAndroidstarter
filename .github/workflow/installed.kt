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

name: generate-asset-hashes
on:
  push:
    paths:
      - 'external_assets/**'
jobs:
  gen-hashes:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Generate hashes
        run: |
          python3 - <<'PY'
import hashlib, json, os
root = "external_assets"
out = {}
for dirpath, _, files in os.walk(root):
  for f in files:
    path = os.path.join(dirpath, f)
    rel = os.path.relpath(path, root)
    h = hashlib.sha256(open(path,'rb').read()).hexdigest()
    out[rel.replace("\\\\","/")] = h
open("expected-hashes.json","w").write(json.dumps(out, indent=2))
print("wrote expected-hashes.json")
PY
      - name: Commit expected hashes
        run: |
          git config user.name "github-actions"
          git config user.email "actions@github.com"
          mv expected-hashes.json external_assets/
          git add external_assets/expected-hashes.json || true
          git commit -m "ci: update asset hashes" || echo "no changes"
          git push || echo "push failed"
