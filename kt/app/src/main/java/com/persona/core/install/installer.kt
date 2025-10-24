package core.install

import android.content.Context
import android.util.Log
import com.persona.androidstarter.security.AssetGuard
import com.persona.androidstarter.security.NetGuard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject
import java.io.File

object Installer {
    private const val TAG = "Installer"

    /** エントリポイント：セキュア環境で呼ばれる前提（SecurityHub.init 後） */
    suspend fun run(ctx: Context): Boolean = withContext(Dispatchers.IO) {
        Log.i(TAG, "Installer start")

        // 0) ローカルアセットの整合性（必須）
        if (!AssetGuard.verifyHashes(ctx)) {
            Log.w(TAG, "Asset hash verify failed"); return@withContext false
        }

        // 1) ローカル展開（assets → /files/modules/_staging）
        val localOk = installFromAssets(ctx)
        if (!localOk) return@withContext false

        // 2) リモート同期（必要なら manifest を取得して更新）
        val remoteOk = tryRemoteSync(ctx) // ピン済み NetGuard.client を想定
        if (!remoteOk) Log.w(TAG, "Remote sync skipped or failed (non-fatal)")

        InstallStore.setLastGood(ctx, System.currentTimeMillis())
        Log.i(TAG, "Installer complete")
        true
    }

    /** assets/installer/manifest.json を読み、署名済みファイルだけ展開 */
    private fun installFromAssets(ctx: Context): Boolean {
        return try {
            val am = ctx.assets
            val text = am.open("installer/manifest.json").use { String(it.readBytes()) }
            val plan = parsePlan(JSONObject(text))

            val staging = FileUtils.dir(ctx, "modules/_staging")
            if (staging.exists()) staging.deleteRecursively()
            staging.mkdirs()

            for (m in plan.modules) {
                val base = File(staging, m.id).apply { mkdirs() }
                for (f in m.files) {
                    val out = File(base, f.path)
                    out.parentFile?.mkdirs()
                    am.open("modules/${m.id}/${f.path}").use { ins ->
                        out.outputStream().use { ins.copyTo(it) }
                    }
                    val got = FileUtils.sha256(out)
                    if (!got.equals(f.sha256, true)) {
                        Log.w(TAG, "hash mismatch (local): ${f.path}")
                        return false
                    }
                }
            }

            // 原子的スワップ
            val live = FileUtils.dir(ctx, "modules/live")
            val backup = FileUtils.dir(ctx, "modules/backup")
            FileUtils.atomicSwapDir(staging, live, backup)
            Log.i(TAG, "Local assets installed into ${live.absolutePath}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "installFromAssets error: ${e.message}")
            false
        }
    }

    /** リモート manifest を取得して差分を同期（オプション） */
    private fun tryRemoteSync(ctx: Context): Boolean {
        return try {
            val client = NetGuard.client // 事前に pinSetup 済み前提
            // 例: https://api.example.com/persona/manifest.json
            val manifestUrl = getRemoteManifestUrl(ctx) ?: return false
            val res = client.newCall(Request.Builder().url(manifestUrl).build()).execute()
            if (!res.isSuccessful) return false
            val body = res.body?.string() ?: return false

            val plan = parsePlan(JSONObject(body))

            val staging = FileUtils.dir(ctx, "modules/_staging_remote")
            if (staging.exists()) staging.deleteRecursively()
            staging.mkdirs()

            for (m in plan.modules) {
                val base = File(staging, m.id).apply { mkdirs() }
                // 既存バージョンと比較して不要ならスキップ
                val current = InstallStore.getVersion(ctx, m.id)
                if (current != null && current == m.version) {
                    Log.i(TAG, "skip ${m.id} (already ${m.version})")
                    continue
                }
                for (f in m.files) {
                    val out = File(base, f.path)
                    out.parentFile?.mkdirs()
                    val url = f.url ?: continue
                    client.newCall(Request.Builder().url(url).build()).execute().use { r ->
                        if (!r.isSuccessful) throw IllegalStateException("download failed ${f.path}")
                        out.outputStream().use { r.body!!.byteStream().copyTo(it) }
                    }
                    val got = FileUtils.sha256(out)
                    if (!got.equals(f.sha256, true)) {
                        throw IllegalStateException("hash mismatch (remote): ${f.path}")
                    }
                }
                // モジュール単位でバージョン反映
                InstallStore.setVersion(ctx, m.id, m.version)
            }

            val live = FileUtils.dir(ctx, "modules/live")
            val backup = FileUtils.dir(ctx, "modules/backup")
            FileUtils.atomicSwapDir(staging, live, backup)
            Log.i(TAG, "Remote modules installed into ${live.absolutePath}")
            true
        } catch (e: Exception) {
            Log.w(TAG, "tryRemoteSync error: ${e.message}")
            // ロールバック（live は backup に戻す）
            val live = File(ctx.filesDir, "modules/live")
            val backup = File(ctx.filesDir, "modules/backup")
            if (live.exists() && backup.exists()) {
                live.deleteRecursively()
                backup.renameTo(live)
            }
            false
        }
    }

    /** JSON → InstallPlan へ */
    private fun parsePlan(json: JSONObject): InstallPlan {
        val modules = mutableListOf<ModuleSpec>()
        val arr = json.getJSONArray("modules")
        for (i in 0 until arr.length()) {
            val jm = arr.getJSONObject(i)
            val id = jm.getString("id")
            val ver = jm.getString("version")
            val filesJ = jm.getJSONArray("files")
            val files = mutableListOf<ModuleFile>()
            for (j in 0 until filesJ.length()) {
                val jf = filesJ.getJSONObject(j)
                files += ModuleFile(
                    path = jf.getString("path"),
                    sha256 = jf.getString("sha256"),
                    size = jf.optLong("size").takeIf { it > 0 },
                    url = jf.optString("url", null)
                )
            }
            modules += ModuleSpec(id, ver, files)
        }
        return InstallPlan(modules)
    }

    /** 端末/ビルドごとに切り替えたい場合はここで返す */
    private fun getRemoteManifestUrl(ctx: Context): String? {
        // BuildConfig などから読み替え可能
        return "https://api.example.com/persona/manifest.json"
    }
}
