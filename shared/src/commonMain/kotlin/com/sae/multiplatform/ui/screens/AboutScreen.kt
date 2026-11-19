package com.sae.multiplatform.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sae.multiplatform.resources.Res
import com.sae.multiplatform.resources.sae_logo
import com.sae.multiplatform.ui.theme.SaeBackground
import com.sae.multiplatform.ui.theme.SaeNavy
import org.jetbrains.compose.resources.painterResource

@Composable
fun AboutScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(SaeBackground).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.sae_logo),
            contentDescription = "Logotipo de SAE",
            modifier = Modifier.size(126.dp)
        )
        Spacer(Modifier.height(20.dp))
        Text("Acerca de SAE", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = SaeNavy)
        Spacer(Modifier.height(14.dp))
        Text(
            "SAE es un proyecto de Kotlin Multiplatform orientado a la organización académica, " +
                "los recordatorios y el bienestar estudiantil.",
            color = Color(0xFF4C5C75), textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(22.dp))
        Text("Desarrollo — Etapa 1", fontWeight = FontWeight.SemiBold, color = SaeNavy)
        Text("Cristian Hoyos", color = Color(0xFF4C5C75))
        Text("Créditos completos: se agregarán con los nombres confirmados de los integrantes.",
            color = Color(0xFF64748B), fontSize = 12.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(26.dp))
        Button(onClick = onBack) { Text("Volver al panel") }
    }
}
