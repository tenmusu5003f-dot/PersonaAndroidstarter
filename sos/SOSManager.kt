package com.persona.androidstarter.sos

import android.content.Context
import android.location.Location
import androidx.work.*
import com.persona.androidstarter.core.audio.AudioSafety
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object SosManager {

    /** 2段階確認（例：UI側で2回タップ or 秘密のジェスチャ）を通過したら呼ぶ */
    suspend fun dispatchSilentSOS(
        ctx: Context,
        endpoint: String,
        token: String = "",
        locProvider: suspend () -> Location?,
        note: String = "silent"
    ): Boolean {
        // スピーカー出力は一切使わない。イヤホン未接続でもSOSは動く（外部音不要だから）
        // ただし TTS やUIサウンド等は別層で AudioSafety が抑止している想定。

        // 位置情報の最小化取得
        val loc = locProvider.invoke()
        val data = workDataOf(
            "endpoint" to endpoint,
            "token" to token,
            "lat" to (loc?.latitude ?: 0.0),
            "lon" to (loc?.longitude ?: 0.0),
            "note" to note
        )

        val req = OneTimeWorkRequestBuilder<SosWorker>()
            .setInputData(data)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        return suspendCancellableCoroutine { cont ->
            WorkManager.getInstance(ctx).enqueue(req)
            WorkManager.getInstance(ctx).getWorkInfoByIdLiveData(req.id)
                .observeForever { info ->
                    if (info?.state?.isFinished == true) {
                        cont.resume(info.state == WorkInfo.State.SUCCEEDED)
                    }
                }
        }
    }

    /** イヤホン以外の出力を使わせない。アプリの音系はこれで統制 */
    fun enforceHeadphoneOnly(ctx: Context) {
        AudioSafety.enforceMuteIfNoHeadphones(ctx)
    }
}
