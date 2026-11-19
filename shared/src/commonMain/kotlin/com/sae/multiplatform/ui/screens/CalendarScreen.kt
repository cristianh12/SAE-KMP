package com.sae.multiplatform.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sae.multiplatform.calendar.*
import com.sae.multiplatform.ui.theme.*

/** Adaptación de la pantalla iOS. Teléfono: una columna; escritorio: calendario y agenda en paralelo. */
@Composable
fun CalendarScreen(onBack: () -> Unit) {
    val today = remember { todayLocal() }
    var month by remember { mutableStateOf(CalendarMonth(today.year, today.month)) }
    var selected by remember { mutableStateOf(today) }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val wide = maxWidth >= 700.dp
        val compact = maxWidth < 380.dp
        PageFrame {
            Text("Calendario académico", fontSize = 26.sp, fontWeight = FontWeight.Bold,
                color = SaeNavy, modifier = Modifier.fillMaxWidth(),
                textAlign = if (wide) TextAlign.Start else TextAlign.Center)
            Spacer(Modifier.height(14.dp))
            if (wide) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.Top) {
                    CalendarCard(month, selected, today, compact = false,
                        onPrevious = { month = month.previous() },
                        onNext = { month = month.next() },
                        onSelect = { selected = it },
                        onToday = { month = CalendarMonth(today.year, today.month); selected = today },
                        modifier = Modifier.weight(1.45f))
                    AgendaPanel(selected, Modifier.weight(1f))
                }
            } else {
                CalendarCard(month, selected, today, compact,
                    onPrevious = { month = month.previous() },
                    onNext = { month = month.next() },
                    onSelect = { selected = it },
                    onToday = { month = CalendarMonth(today.year, today.month); selected = today },
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(15.dp))
                AgendaPanel(selected, Modifier.fillMaxWidth())
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun CalendarCard(
    month: CalendarMonth,
    selected: CalendarDate,
    today: CalendarDate,
    compact: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSelect: (CalendarDate) -> Unit,
    onToday: () -> Unit,
    modifier: Modifier = Modifier
) {
    SaePanel(modifier) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text("‹", color = SaePurple, fontSize = 30.sp,
                modifier = Modifier.clickable(onClick = onPrevious).padding(horizontal = 8.dp))
            Text("${monthNamesEs[month.month - 1]} ${month.year}",
                color = SaePurple, fontSize = if (compact) 16.sp else 19.sp,
                fontWeight = FontWeight.Bold)
            Text("›", color = SaePurple, fontSize = 30.sp,
                modifier = Modifier.clickable(onClick = onNext).padding(horizontal = 8.dp))
        }
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth()) {
            listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom").forEach { name ->
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(if (compact) name.take(1) else name, fontSize = 11.sp,
                        color = SaeMuted, maxLines = 1)
                }
            }
        }
        Spacer(Modifier.height(5.dp))
        // Altura limitada por fila: la cuadrícula no se estira como antes en Desktop.
        calendarCells(month).chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    val picked = date == selected
                    val current = date == today
                    val fill = when {
                        picked -> SaePurpleSoft
                        current -> Color(0xFFF3EDFE)
                        else -> Color.Transparent
                    }
                    Box(Modifier.weight(1f).height(if (compact) 38.dp else 46.dp)
                        .padding(2.dp)
                        .background(fill, RoundedCornerShape(9.dp))
                        .then(if (date != null) Modifier.clickable { onSelect(date) } else Modifier),
                        contentAlignment = Alignment.Center) {
                        if (date != null) {
                            Text("${date.day}", color = if (picked) SaePurple else SaeNavy,
                                fontSize = 13.sp,
                                fontWeight = if (picked || current) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onToday, modifier = Modifier.align(Alignment.End)) {
            Text("Volver a hoy", color = SaePurple, fontSize = 12.sp)
        }
    }
}

@Composable
private fun AgendaPanel(date: CalendarDate, modifier: Modifier = Modifier) {
    Column(modifier) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Legend("Entrega", SaePurple)
            Legend("Parcial", Color(0xFFF3AC50))
            Legend("Actividad", Color(0xFF55BA82))
        }
        Spacer(Modifier.height(16.dp))
        Text("${date.day} de ${monthNamesEs[date.month - 1].lowercase()} de ${date.year}",
            fontWeight = FontWeight.Bold, fontSize = 17.sp, color = SaeNavy)
        Spacer(Modifier.height(12.dp))
        SaePanel(Modifier.fillMaxWidth()) {
            Text("Sin actividades registradas", fontWeight = FontWeight.Medium, color = SaeNavy)
            Spacer(Modifier.height(8.dp))
            Text("Los eventos que guardes aparecerán aquí al conectar el calendario a la base de datos.",
                color = SaeMuted, fontSize = 13.sp)
        }
    }
}

@Composable
private fun Legend(title: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(Modifier.size(7.dp).background(color, RoundedCornerShape(5.dp)))
        Text(title, color = SaeMuted, fontSize = 10.sp)
    }
}
