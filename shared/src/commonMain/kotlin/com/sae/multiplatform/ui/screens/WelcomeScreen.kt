package com.sae.multiplatform.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sae.multiplatform.resources.Res
import com.sae.multiplatform.resources.sae_logo
import com.sae.multiplatform.ui.theme.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun WelcomeScreen(onEnter: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(
        listOf(Color(0xFFEAE0FF), Color(0xFFF7F6FD), Color.White))),
        contentAlignment = Alignment.Center) {
        Column(Modifier.fillMaxWidth().widthIn(max = 430.dp)
            .verticalScroll(rememberScrollState()).padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painterResource(Res.drawable.sae_logo), "Logo de SAE", Modifier.size(150.dp))
            Spacer(Modifier.height(20.dp))
            Text("Bienvenido a SAE", fontSize = 28.sp, fontWeight = FontWeight.Bold,
                color = SaeNavy, textAlign = TextAlign.Center)
            Spacer(Modifier.height(9.dp))
            Text("Sistema de Acompañamiento Estudiantil", color = SaeMuted,
                fontSize = 14.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            SaePanel(Modifier.fillMaxWidth()) {
                Text("Acceso al espacio académico", fontWeight = FontWeight.SemiBold,
                    color = SaeNavy)
                Spacer(Modifier.height(10.dp))
                Text("Entra para explorar las pantallas de SAE. La autenticación real todavía no está integrada.",
                    color = SaeMuted, fontSize = 13.sp)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onEnter, modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp)) { Text("Ingresar a SAE") }
            }
            Spacer(Modifier.height(16.dp))
            Text("Versión Kotlin Multiplatform · Android y Desktop", fontSize = 11.sp,
                color = SaeMuted, textAlign = TextAlign.Center)
        }
    }
}
