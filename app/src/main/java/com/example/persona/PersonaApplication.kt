package com.example.persona

import android.app.Application
import android.os.StrictMode
import android.util.Log
import androidx.work.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class PersonaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 🔧 起動時高速化設定
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        // 🧠 並列起動（AIモジュールを並行処理）
        val cpuCount = Runtime.getRuntime().availableProcessors().coerceAtMost(4)
        val executor = Executors.newFixedThreadPool(cpuCount)

        Log.i("PersonaApp", "Optimized startup using $cpuCount threads")

        // 🔋 夜間バックグラウンド学習
        setupNightlyLearning()
    }

    private fun setupNightlyLearning() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val work = PeriodicWorkRequestBuilder<BackgroundLearningWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "AIBackgroundLearning",
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }
}
