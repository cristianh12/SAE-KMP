package com.sae.multiplatform.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta basada en las capturas del prototipo SAE iOS (SwiftUI).
val SaePurple = Color(0xFF6739D3)
val SaePurpleSoft = Color(0xFFECE5FA)
val SaeBlue = SaePurple // Compatibilidad con pantallas anteriores.
val SaeCyan = Color(0xFF00B9D8)
val SaeNavy = Color(0xFF242038)
val SaeBackground = Color(0xFFF6F5FB)
val SaeMuted = Color(0xFF777587)

private val palette = lightColorScheme(
    primary = SaePurple, onPrimary = Color.White,
    secondary = SaeCyan, background = SaeBackground,
    onBackground = SaeNavy, surface = Color.White, onSurface = SaeNavy
)

@Composable
fun SaeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = palette, content = content)
}
