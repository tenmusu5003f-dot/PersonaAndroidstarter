package core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import core.ui.PersonaSettingsScreen
import core.ui.PersonaMainScreen

/**
 * NavHost.kt
 * -------------------------------------------------
 * Personaアプリのナビゲーション制御。
 * - Compose Navigation の軽量実装
 * - メイン画面・設定画面間の遷移を管理
 */
@Composable
fun PersonaNavHost(
    navController: NavHostController,
    appFilesDirPath: String
) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            PersonaMainScreen(
                onNavigateSettings = { navController.navigate("settings") }
            )
        }

        composable("settings") {
            PersonaSettingsScreen(
                appFilesDirPath = appFilesDirPath,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
