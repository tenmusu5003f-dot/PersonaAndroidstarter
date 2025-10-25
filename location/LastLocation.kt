package com.persona.androidstarter.core.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import android.location.Location

object LastLocation {
    @SuppressLint("MissingPermission")
    suspend fun getFast(ctx: Context): Location? {
        // 直近のFusedLocationProviderの最後の位置のみ（その場で測位しない＝静か）
        return try { LocationServices.getFusedLocationProviderClient(ctx).lastLocation.await() }
        catch (_: Exception){ null }
    }
}
