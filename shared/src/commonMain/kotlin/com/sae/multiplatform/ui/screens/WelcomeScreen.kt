package com.sae.multiplatform.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import com.sae.multiplatform.ui.theme.SaeBlue
import com.sae.multiplatform.ui.theme.SaeNavy
import org.jetbrains.compose.resources.painterResource

@Composable
fun WelcomeScreen(onEnter: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(
                colors = listOf(Color(0xFFE9F8FF), Color(0xFFF6F7FF), Color.White)
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().widthIn(max = 520.dp).padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.sae_logo),
                contentDescription = "Logotipo oficial de SAE",
                modifier = Modifier.size(194.dp)
            )
            Spacer(Modifier.height(26.dp))
            Text(
                text = "Tu espacio académico",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = SaeNavy,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Organiza tu semestre, tus materias y tu bienestar en un solo lugar.",
                fontSize = 16.sp,
                color = Color(0xFF4C5C75),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(30.dp))
            Button(
                onClick = onEnter,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SaeBlue)
            ) {
                Text(text = "Ingresar a SAE", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Sistema de Acompañamiento Estudiantil",
                fontSize = 12.sp,
                color = Color(0xFF68768A),
                textAlign = TextAlign.Center
            )
        }
    }
}
