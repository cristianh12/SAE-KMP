package com.sae.multiplatform.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sae.multiplatform.ui.theme.*

@Composable
fun PageFrame(content: @Composable ColumnScope.() -> Unit) {
    Box(Modifier.fillMaxSize().background(SaeBackground), contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier.widthIn(max = 1120.dp).fillMaxWidth().fillMaxHeight()
                .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 22.dp),
            content = content
        )
    }
}

@Composable
fun SaePanel(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier, shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(Modifier.fillMaxWidth().padding(18.dp), content = content)
    }
}

@Composable
fun PendingSection(title: String, icon: String, description: String) {
    PageFrame {
        Text(title, fontSize = 27.sp, fontWeight = FontWeight.Bold, color = SaeNavy)
        Spacer(Modifier.height(24.dp))
        SaePanel(Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(48.dp))
            Text(icon, fontSize = 52.sp, color = SaePurple, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Text(description, textAlign = TextAlign.Center, color = SaeMuted,
                modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
fun ProfileSection(onAbout: () -> Unit, onExit: () -> Unit) {
    PageFrame {
        Text("Perfil", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = SaeNavy)
        Spacer(Modifier.height(20.dp))
        SaePanel(Modifier.fillMaxWidth()) {
            Text("Perfil estudiantil", fontWeight = FontWeight.Bold, color = SaeNavy)
            Spacer(Modifier.height(8.dp))
            Text("Los datos personales se mostrarán al integrar el perfil persistente.", color = SaeMuted)
            Spacer(Modifier.height(20.dp))
            Button(onClick = onAbout) { Text("Acerca de SAE y créditos") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onExit) { Text("Salir de SAE") }
        }
    }
}
