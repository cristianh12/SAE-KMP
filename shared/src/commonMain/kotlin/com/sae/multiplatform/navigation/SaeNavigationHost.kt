package com.sae.multiplatform.navigation

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sae.multiplatform.ui.screens.*
import com.sae.multiplatform.ui.theme.*

private data class NavItem(val screen: SaeScreen, val icon: String, val label: String)
private val tabs = listOf(
    NavItem(SaeScreen.DASHBOARD, "⌂", "Inicio"),
    NavItem(SaeScreen.SUBJECTS, "◈", "Materias"),
    NavItem(SaeScreen.CALENDAR, "▦", "Calendario"),
    NavItem(SaeScreen.RESOURCES, "▥", "Recursos"),
    NavItem(SaeScreen.PROFILE, "●", "Perfil")
)

@Composable
fun SaeNavigationHost() {
    var screen by remember { mutableStateOf(SaeScreen.WELCOME) }
    if (screen == SaeScreen.WELCOME) {
        WelcomeScreen(onEnter = { screen = SaeScreen.DASHBOARD })
        return
    }
    BoxWithConstraints(Modifier.fillMaxSize().background(SaeBackground)) {
        val isWide = maxWidth >= 840.dp
        val currentTab = if (screen == SaeScreen.ABOUT) SaeScreen.PROFILE else screen
        if (isWide) {
            Row(Modifier.fillMaxSize()) {
                Column(
                    Modifier.width(210.dp).fillMaxHeight().background(Color.White)
                        .padding(horizontal = 14.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("SAE", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = SaePurple,
                        modifier = Modifier.padding(start = 16.dp, bottom = 22.dp))
                    tabs.forEach { tab ->
                        NavEntry(tab, currentTab == tab.screen, true) { screen = tab.screen }
                    }
                    Spacer(Modifier.weight(1f))
                    Text("Sistema de Acompañamiento Estudiantil", fontSize = 11.sp,
                        color = SaeMuted, modifier = Modifier.padding(12.dp))
                }
                Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.TopCenter) {
                    ScreenContent(screen,
                        onCalendar = { screen = SaeScreen.CALENDAR },
                        onAbout = { screen = SaeScreen.ABOUT },
                        onBack = { screen = SaeScreen.PROFILE },
                        onExit = { screen = SaeScreen.WELCOME })
                }
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(1f)) {
                    ScreenContent(screen,
                        onCalendar = { screen = SaeScreen.CALENDAR },
                        onAbout = { screen = SaeScreen.ABOUT },
                        onBack = { screen = SaeScreen.PROFILE },
                        onExit = { screen = SaeScreen.WELCOME })
                }
                Row(
                    Modifier.fillMaxWidth().background(Color.White)
                        .navigationBarsPadding().padding(horizontal = 3.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    tabs.forEach { tab ->
                        Box(Modifier.weight(1f)) {
                            NavEntry(tab, currentTab == tab.screen, false) { screen = tab.screen }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavEntry(item: NavItem, selected: Boolean, wide: Boolean, onClick: () -> Unit) {
    val color = if (selected) SaePurple else SaeMuted
    val shape = RoundedCornerShape(12.dp)
    val modifier = if (wide) Modifier.fillMaxWidth() else Modifier.fillMaxWidth()
    if (wide) {
        Row(modifier.background(if (selected) SaePurpleSoft else Color.Transparent, shape)
                .clickable(onClick = onClick).padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(item.icon, fontSize = 22.sp, color = color)
            Text(item.label, color = color, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
        }
    } else {
        Column(modifier.clickable(onClick = onClick).padding(vertical = 3.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(item.icon, fontSize = 22.sp, color = color, lineHeight = 24.sp)
            Text(item.label, color = color, fontSize = 10.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, maxLines = 1)
        }
    }
}

@Composable
private fun ScreenContent(
    screen: SaeScreen,
    onCalendar: () -> Unit,
    onAbout: () -> Unit,
    onBack: () -> Unit,
    onExit: () -> Unit
) {
    when (screen) {
        SaeScreen.DASHBOARD -> DashboardScreen(onCalendar, onAbout, onExit)
        SaeScreen.CALENDAR -> CalendarScreen(onBack)
        SaeScreen.SUBJECTS -> PendingSection("Mis materias", "◈",
            "Aquí aparecerán las materias y calificaciones que registres cuando se conecte el módulo de datos.")
        SaeScreen.RESOURCES -> PendingSection("Biblioteca", "▥",
            "Tu biblioteca está vacía. Los archivos y la búsqueda bibliográfica se integrarán con el módulo de Recursos.")
        SaeScreen.PROFILE -> ProfileSection(onAbout, onExit)
        SaeScreen.ABOUT -> AboutScreen(onBack)
        SaeScreen.WELCOME -> Unit
    }
}
