// region roadsV1_opening_impl
object roadsV1_OpeningEffects {
    fun plan(deviceTier: Int): List<roadsV1_Effect> = roadsV1_EffectPolicy.openingFor(deviceTier)

    // 疑似レンダラ（今はログだけ。後で実装を差し替え）
    fun render(plan: List<roadsV1_Effect>, log: (String) -> Unit = ::println) {
        plan.forEach { log("opening:${it.id} x${it.weight}") }
    }
}
// endregion

package core

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * OpeningEffects
 * -------------------------------------------------
 * EffectPolicy のティアに応じて、起動時の演出を出し分ける。
 * - OFF    : 何もしない（即 onEnd）
 * - LIGHT  : 背景 → ロゴのフェードイン
 * - NORMAL : フェード＋軽い拡大（パルス）
 * - FULL   : 色面フェード → ロゴパルスを2サイクル
 *
 * 依存アセットが無くても動作するよう、ロゴは外部から渡す前提（null可）。
 * ロゴが無い場合は色面のみのフェード動作にフォールバック。
 */
object OpeningEffects {

    /**
     * 起動演出の再生。UIツリー内で呼ぶこと。
     * @param tier EffectPolicy.tier() の現在値
     * @param logo 任意のロゴ（null可）— 例： painterResource(R.drawable.logo)
     * @param durationMs アニメーション1サイクルの基準時間
     * @param onEnd 演出終了時に呼ばれるコールバック
     */
    @Composable
    fun Play(
        tier: EffectPolicy.EffectTier,
        logo: Painter? = null,
        durationMs: Int = 600,
        onEnd: () -> Unit
    ) {
        when (tier) {
            EffectPolicy.EffectTier.OFF -> NoEffect(onEnd)
            EffectPolicy.EffectTier.LIGHT -> FadeInLogo(logo, durationMs, onEnd)
            EffectPolicy.EffectTier.NORMAL -> PulseLogo(logo, durationMs, cycles = 1, onEnd = onEnd)
            EffectPolicy.EffectTier.FULL -> FullIntro(logo, durationMs, onEnd)
        }
    }

    // --- Effects ---

    @Composable
    private fun NoEffect(onEnd: () -> Unit) {
        // 即終了（安全最優先）
        LaunchedEffect(Unit) { onEnd() }
    }

    @Composable
    private fun FadeInLogo(logo: Painter?, durationMs: Int, onEnd: () -> Unit) {
        val alpha by animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(durationMs = durationMs, easing = FastOutSlowInEasing),
            label = "fade_alpha"
        )
        AutoFinish(durationMs.toLong(), onEnd)

        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (logo != null) {
                    Image(
                        painter = logo,
                        contentDescription = "logo",
                        modifier = Modifier
                            .size(120.dp)
                            .alpha(alpha)
                    )
                } else {
                    // ロゴが無い場合は色面フェードのみにフォールバック
                    Box(
                        Modifier
                            .size(120.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha * 0.2f))
                    )
                }
            }
        }
    }

    @Composable
    private fun PulseLogo(
        logo: Painter?,
        durationMs: Int,
        cycles: Int,
        onEnd: () -> Unit
    ) {
        val infinite = remember { Animatable(0f) }
        val totalMs = durationMs * (cycles.coerceAtLeast(1))
        LaunchedEffect(Unit) {
            repeat(cycles) {
                infinite.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMs = durationMs, easing = FastOutSlowInEasing)
                )
                infinite.snapTo(0f)
            }
            onEnd()
        }
        val alpha = 0.5f + 0.5f * infinite.value

        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (logo != null) {
                    Image(
                        painter = logo,
                        contentDescription = "logo",
                        modifier = Modifier
                            .size(140.dp)
                            .alpha(alpha)
                    )
                } else {
                    Box(
                        Modifier
                            .size(140.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha * 0.25f))
                    )
                }
            }
        }
    }

    @Composable
    private fun FullIntro(logo: Painter?, durationMs: Int, onEnd: () -> Unit) {
        // 背景色のなめらかなフェード → ロゴのパルス2サイクル
        val bgAlpha by animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(durationMs = durationMs / 2, easing = LinearOutSlowInEasing),
            label = "bg_alpha"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.0f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f * bgAlpha))
            )
            // 背景フェードののちにロゴパルス2回
            if (bgAlpha >= 0.99f) {
                PulseLogo(logo, durationMs, cycles = 2, onEnd = onEnd)
            }
        }
    }

    // --- Utilities ---

    @Composable
    private fun AutoFinish(delayMs: Long, onEnd: () -> Unit) {
        LaunchedEffect(delayMs) {
            kotlinx.coroutines.delay(delayMs)
            onEnd()
        }
    }
}

/**
 * 例：呼び出し側の使い方（MainActivityの setContent {} 内など）
 *
 * val os = PersonaOSBridge.osInfo()
 * val battery = PersonaCore.getService<Int>("system.battery.level")
 * val security = PersonaSecurity.evaluate(application).let { it.signatureValid && !it.debuggerAttached && !it.suspiciousBuild }
 * val tier = EffectPolicy.autoAdjust(os, security, battery)
 *
 * OpeningEffects.Play(tier = tier, logo = painterResource(R.drawable.logo)) {
 *     // 演出が終わったらホーム画面へ
 *     showHome = true
 * }
 */
