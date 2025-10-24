// security/AuthGate.kt
package com.persona.androidstarter.security

import androidx.navigation.NavController
import com.persona.androidstarter.nav.Routes

object AuthGate {
  fun enforce(nav: NavController, session: SessionManager) {
    if (!session.isSignedIn()) {
      nav.navigate(Routes.Login) {
        popUpTo(Routes.Root) { inclusive = false }
        launchSingleTop = true
      }
    }
  }
}
