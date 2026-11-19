package com.sae.multiplatform.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sae.multiplatform.resources.Res
import com.sae.multiplatform.resources.sae_logo
import com.sae.multiplatform.ui.theme.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun AboutScreen(onBack: () -> Unit) {
    PageFrame {
        SaePanel(Modifier.fillMaxWidth()) {
            Image(painterResource(Res.drawable.sae_logo), "Logo de SAE",
                Modifier.size(108.dp).align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(14.dp))
            Text("Acerca de SAE", fontSize = 25.sp, fontWeight = FontWeight.Bold,
                color = SaeNavy, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(Modifier.height(12.dp))
            Text("Sistema de Acompañamiento Estudiantil. Organiza la vida académica y reúne herramientas para el bienestar del estudiante.",
                color = SaeMuted, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Text("Equipo del proyecto", fontWeight = FontWeight.Bold, color = SaeNavy)
            Text("Cristian Hoyos · Daniel Mañunga · Juan Guerrero", color = SaeMuted)
            Spacer(Modifier.height(20.dp))
            OutlinedButton(onClick = onBack) { Text("Volver al perfil") }
        }
    }
}
