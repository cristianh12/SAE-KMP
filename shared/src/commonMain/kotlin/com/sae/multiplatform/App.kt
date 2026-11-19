package com.sae.multiplatform

import androidx.compose.runtime.Composable
import com.sae.multiplatform.navigation.SaeNavigationHost
import com.sae.multiplatform.ui.theme.SaeTheme

/** Punto de entrada de la interfaz compartida entre Android y Desktop. */
@Composable
fun App() {
    SaeTheme {
        SaeNavigationHost()
    }
}
