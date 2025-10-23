package core.ui

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset

/**
 * PersonaTransition
 * -------------------------------------------------
 * ペルソナ画面切り替え用アニメーション。
 * - フェード＋スライドで自然な遷移
 * - PersonaScreenやHomeMenuで共通使用
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PersonaTransition(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it / 3 }),
        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it / 3 })
    ) {
        content()
    }
}
