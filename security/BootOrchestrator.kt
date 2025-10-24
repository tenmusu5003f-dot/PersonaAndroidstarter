package com.persona.androidstarter.security

import android.content.Context
import kotlinx.coroutines.*
import android.util.Log

object BootOrchestrator {
  private val TAG = "BootOrchestrator"

  suspend fun start(ctx: Context, onReady: () -> Unit, onFail: (Throwable)->Unit) {
    val scope = CoroutineScope(Dispatchers.Default)
    try {
      val guards = listOf(
        asyncGuard { SignatureGuard.verify(ctx) },
        asyncGuard { AssetGuard.verifyAll(ctx) },
        asyncGuard { RootGuard.check(ctx) },
        asyncGuard { TTSPrewarm.warm(ctx) } // 任意のプリウォーム
      )
      // 待つけど、必須と任意の扱いは個別に判断できるようにする
      val results = guards.awaitAll()
      // results が全部 OK なら onReady。失敗があってもフォールバックできるように判定
      onReady()
    } catch (e: Throwable) {
      Log.e(TAG, "Boot failed: ${e.message}")
      onFail(e)
    } finally {
      scope.cancel()
    }
  }

  private fun asyncGuard(block: suspend ()->Unit): Deferred<Unit> =
    CoroutineScope(Dispatchers.Default).async { withTimeout(5_000) { block() } }
}

package com.persona.androidstarter.security

import android.content.Context
import kotlinx.coroutines.*
import android.util.Log

object BootOrchestrator {
  private val TAG = "BootOrchestrator"

  suspend fun start(ctx: Context, onReady: () -> Unit, onFail: (Throwable)->Unit) {
    val scope = CoroutineScope(Dispatchers.Default)
    try {
      val guards = listOf(
        asyncGuard { SignatureGuard.verify(ctx) },
        asyncGuard { AssetGuard.verifyAll(ctx) },
        asyncGuard { RootGuard.check(ctx) },
        asyncGuard { TTSPrewarm.warm(ctx) } // 任意のプリウォーム
      )
      // 待つけど、必須と任意の扱いは個別に判断できるようにする
      val results = guards.awaitAll()
      // results が全部 OK なら onReady。失敗があってもフォールバックできるように判定
      onReady()
    } catch (e: Throwable) {
      Log.e(TAG, "Boot failed: ${e.message}")
      onFail(e)
    } finally {
      scope.cancel()
    }
  }

  private fun asyncGuard(block: suspend ()->Unit): Deferred<Unit> =
    CoroutineScope(Dispatchers.Default).async { withTimeout(5_000) { block() } }
}
