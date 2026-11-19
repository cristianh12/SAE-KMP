package com.sae.multiplatform.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sae.multiplatform.ui.screens.AboutScreen
import com.sae.multiplatform.ui.screens.DashboardScreen
import com.sae.multiplatform.ui.screens.WelcomeScreen

/**
 * Navegación en commonMain: ambas plataformas ejecutan el mismo flujo.
 * Más adelante se añadirán rutas para materias, calendario y biblioteca.
 */
@Composable
fun SaeNavigationHost() {
    var currentScreen by remember { mutableStateOf(SaeScreen.WELCOME) }

    when (currentScreen) {
        SaeScreen.WELCOME -> WelcomeScreen(
            onEnter = { currentScreen = SaeScreen.DASHBOARD }
        )
        SaeScreen.DASHBOARD -> DashboardScreen(
            onAbout = { currentScreen = SaeScreen.ABOUT },
            onExit = { currentScreen = SaeScreen.WELCOME }
        )
        SaeScreen.ABOUT -> AboutScreen(
            onBack = { currentScreen = SaeScreen.DASHBOARD }
        )
    }
}
