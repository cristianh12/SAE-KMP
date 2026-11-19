package com.sae.multiplatform.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val SaeBlue = Color(0xFF0649B6)
val SaeCyan = Color(0xFF00B9D8)
val SaeNavy = Color(0xFF10234C)
val SaeBackground = Color(0xFFF4F8FF)

private val SaeColors = lightColorScheme(
    primary = SaeBlue,
    onPrimary = Color.White,
    secondary = SaeCyan,
    background = SaeBackground,
    onBackground = SaeNavy,
    surface = Color.White,
    onSurface = SaeNavy
)

@Composable
fun SaeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = SaeColors, content = content)
}
