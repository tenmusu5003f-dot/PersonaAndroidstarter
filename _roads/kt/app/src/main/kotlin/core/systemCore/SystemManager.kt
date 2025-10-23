package core.system

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.system.measureTimeMillis

/**
 * CoreSystemManager
 * -------------------------------------------------
 * Personaアプリ全体のバックグラウンド制御を担当。
 * - 各AIの処理を非同期で実行
 * - タスクキューとリソース最適化を管理
 * - バッテリーや通信量を節約しつつ学習維持
 */
object CoreSystemManager {

    private val scope = CoroutineScope(Dispatchers.Default)
    private val taskQueue = ConcurrentLinkedQueue<suspend () -> Unit>()
    private var isRunning = false

    /** タスク登録 */
    fun registerTask(task: suspend () -> Unit) {
        taskQueue.add(task)
        if (!isRunning) startProcessing()
    }

    /** メインループ開始 */
    private fun startProcessing() {
        isRunning = true
        scope.launch {
            while (taskQueue.isNotEmpty()) {
                val task = taskQueue.poll()
                if (task != null) {
                    val time = measureTimeMillis {
                        try {
                            task()
                        } catch (e: Exception) {
                            println("⚠️ Task failed: ${e.message}")
                        }
                    }
                    delay((50 - time.coerceAtMost(50)).coerceAtLeast(0))
                }
            }
            isRunning = false
        }
    }

    /** Persona固有のバックグラウンド処理 */
    fun schedulePersonaLearning(personaName: String, data: String) {
        registerTask {
            println("🧠 $personaName learning in background with $data")
            delay(100)
        }
    }

    /** 通信節約モード (Wi-Fi時優先処理) */
    fun optimizeNetworkUsage(isWifi: Boolean) {
        registerTask {
            if (isWifi) {
                println("🌐 Wi-Fi detected: syncing heavy persona data")
            } else {
                println("📶 Mobile data: lightweight sync only")
            }
        }
    }

    /** クリーンアップ */
    fun shutdown() {
        scope.cancel()
        isRunning = false
        println("🛑 CoreSystemManager stopped.")
    }
}
