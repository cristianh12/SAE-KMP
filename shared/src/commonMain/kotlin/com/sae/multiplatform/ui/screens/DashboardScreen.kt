package com.sae.multiplatform.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sae.multiplatform.ui.theme.SaeBackground
import com.sae.multiplatform.ui.theme.SaeBlue
import com.sae.multiplatform.ui.theme.SaeNavy

@Composable
fun DashboardScreen(onAbout: () -> Unit, onExit: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(SaeBackground)
            .verticalScroll(rememberScrollState())
            .padding(22.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth().widthIn(max = 740.dp)) {
            Text("SAE", fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, color = SaeBlue)
            Spacer(Modifier.height(6.dp))
            Text("Panel académico", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = SaeNavy)
            Spacer(Modifier.height(8.dp))
            Text("Aquí se mostrará la información que realmente registres en SAE.",
                fontSize = 15.sp, color = Color(0xFF516178))
            Spacer(Modifier.height(22.dp))
            InfoCard(
                title = "Materias y calificaciones",
                text = "Aún no está conectada la gestión de materias. No se mostrarán cursos ni notas ficticias."
            )
            Spacer(Modifier.height(12.dp))
            InfoCard(
                title = "Trabajos y entregas",
                text = "Los trabajos aparecerán aquí cuando su registro y consulta estén conectados a la base de datos."
            )
            Spacer(Modifier.height(12.dp))
            InfoCard(
                title = "Calendario y recordatorios",
                text = "La siguiente etapa implementará eventos reales y su almacenamiento persistente."
            )
            Spacer(Modifier.height(22.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = onAbout) { Text("Acerca de SAE") }
                OutlinedButton(onClick = onExit) { Text("Salir") }
            }
        }
    }
}

@Composable
private fun InfoCard(title: String, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = SaeNavy)
            Spacer(Modifier.height(8.dp))
            Text(text, fontSize = 14.sp, color = Color(0xFF5A697D))
        }
    }
}
