package com.sae.multiplatform.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sae.multiplatform.ui.theme.*

@Composable
fun DashboardScreen(
    onCalendar: () -> Unit,
    onAbout: () -> Unit,
    onExit: () -> Unit,
    onWellness: () -> Unit
) {
    PageFrame {
        Text("SAE", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SaeNavy)
        Spacer(Modifier.height(13.dp))
        Text("Hola, estudiante", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = SaeNavy)
        Text("Tu resumen académico", fontSize = 14.sp, color = SaeMuted)
        Spacer(Modifier.height(22.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard("—", "Promedio", "▥", Modifier.weight(1f))
            MetricCard("0", "Materias", "◈", Modifier.weight(1f))
            MetricCard("0", "Recursos", "▤", Modifier.weight(1f))
        }
        Spacer(Modifier.height(24.dp))
        Text("Progreso por materia", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SaeNavy)
        Spacer(Modifier.height(12.dp))
        SaePanel(Modifier.fillMaxWidth()) {
            Text("Todavía no hay materias registradas.", color = SaeNavy)
            Spacer(Modifier.height(6.dp))
            Text("Las notas y barras de progreso se calcularán con tus datos reales.",
                color = SaeMuted, fontSize = 13.sp)
        }
        Spacer(Modifier.height(22.dp))
        Text("Próximas actividades", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SaeNavy)
        Spacer(Modifier.height(12.dp))
        SaePanel(Modifier.fillMaxWidth()) {
            Text("No hay actividades registradas.", color = SaeNavy)
            Spacer(Modifier.height(6.dp))
            Text("Consulta las fechas del semestre desde tu calendario.", color = SaeMuted, fontSize = 13.sp)
            Spacer(Modifier.height(14.dp))
            Button(onClick = onCalendar) { Text("Ver calendario") }
        }

        Spacer(Modifier.height(22.dp))

        Text(
            "Bienestar estudiantil",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = SaeNavy
        )

        Spacer(Modifier.height(12.dp))

        SaePanel(Modifier.fillMaxWidth()) {

            Text(
                "Cuida de tu bienestar",
                fontWeight = FontWeight.Bold,
                color = SaeNavy
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Registra tu estado de ánimo, nivel de estrés, horas de sueño y carga académica.",
                color = SaeMuted,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(14.dp))

            Button(
                onClick = onWellness,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Acceder a mi bienestar")
            }
        }

        Spacer(Modifier.height(18.dp))
    }
}

@Composable
private fun MetricCard(value: String, caption: String, symbol: String, modifier: Modifier) {
    SaePanel(modifier) {
        Text(symbol, color = SaePurple, fontSize = 21.sp)
        Spacer(Modifier.height(9.dp))
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SaeNavy)
        Text(caption, fontSize = 11.sp, color = SaeMuted, maxLines = 1)
    }
}
