package com.example.persona

import android.app.Application
import android.os.StrictMode
import androidx.work.*

import java.util.concurrent.TimeUnit

class PersonaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 起動高速化（初期I/O制限を緩める）
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        // 夜間バックグラウンド学習・最適化（充電 & Wi-Fi時のみ）
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val periodic = PeriodicWorkRequestBuilder<BackgroundLearningWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "AIBackgroundLearning",
            ExistingPeriodicWorkPolicy.KEEP,
            periodic
        )
    }
}
