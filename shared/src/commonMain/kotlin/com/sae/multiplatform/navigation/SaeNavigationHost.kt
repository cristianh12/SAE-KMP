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
import com.sae.multiplatform.data.remote.SupabaseProvider
import com.sae.multiplatform.data.repository.AuthRepository
import kotlinx.coroutines.launch
import com.sae.multiplatform.data.remote.SupabaseLocalConfig
import com.sae.multiplatform.data.repository.PerfilRepository
import com.sae.multiplatform.data.repository.PerfilUsuario
import io.github.jan.supabase.SupabaseClient

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
    val scope = rememberCoroutineScope()


    val cliente = remember {
        SupabaseProvider.crearCliente(
            projectUrl = "https://voxctlzffrkypkthmblv.supabase.co",
            publishableKey = SupabaseLocalConfig.PUBLISHABLE_KEY
        )
    }

    val authRepository = remember(cliente) {
        AuthRepository(cliente)
    }

    val perfilRepository = remember(cliente) {
        PerfilRepository(cliente)
    }

    var perfilActual by remember {
        mutableStateOf<PerfilUsuario?>(null)
    }


    var cargandoLogin by remember { mutableStateOf(false) }
    var errorLogin by remember { mutableStateOf<String?>(null) }
    if (screen == SaeScreen.WELCOME) {
        WelcomeScreen(onEnter = { screen = SaeScreen.LOGIN })
        return
    }

    if (screen == SaeScreen.LOGIN) {
        LoginScreen(
            onLogin = { correo, contrasena ->
                if (!cargandoLogin) {
                    cargandoLogin = true
                    errorLogin = null

                    scope.launch {
                        try {
                            authRepository.iniciarSesion(
                                correo = correo,
                                contrasena = contrasena
                            )

                            val usuario = authRepository.usuarioAutenticado()

                            if (usuario != null) {
                                val perfil = perfilRepository.obtenerMiPerfil()

                                if (perfil.rol == "docente" ||
                                    perfil.rol == "estudiante"
                                ) {
                                    perfilActual = perfil
                                    screen = SaeScreen.DASHBOARD
                                } else {
                                    errorLogin = "Tu cuenta no tiene un rol válido"
                                    authRepository.cerrarSesion()
                                }
                            } else {
                                errorLogin = "No se pudo confirmar la sesión"
                            }
                        } catch (e: Exception) {
                            errorLogin =
                                "No se pudo iniciar sesión o consultar el perfil."
                        } finally {
                            cargandoLogin = false
                        }
                    }
                }
            },
            onBack = {
                errorLogin = null
                screen = SaeScreen.WELCOME
            },
            errorMessage = errorLogin,
            isLoading = cargandoLogin
        )
        return
    }


    BoxWithConstraints(Modifier.fillMaxSize().background(SaeBackground)) {
        val isWide = maxWidth >= 840.dp
        val currentTab = when (screen) {
            SaeScreen.ABOUT -> SaeScreen.PROFILE
            SaeScreen.WELLNESS -> SaeScreen.DASHBOARD
            else -> screen
        }
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

                    ScreenContent(
                        screen,
                        cliente = cliente, // ← AGREGA ESTA LÍNEA
                        perfil = perfilActual,
                        correo = authRepository.usuarioAutenticado()?.email,
                        onCalendar = { screen = SaeScreen.CALENDAR },
                        onWellness = { screen = SaeScreen.WELLNESS },
                        onAbout = { screen = SaeScreen.ABOUT },
                        onBack = { screen = SaeScreen.PROFILE },
                        onExit = {
                            scope.launch {
                                try {
                                    authRepository.cerrarSesion()
                                    perfilActual = null
                                    errorLogin = null
                                    screen = SaeScreen.WELCOME
                                } catch (e: Exception) {
                                    errorLogin = "No se pudo cerrar la sesión"
                                }
                            }
                        }
                    )


                }
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(1f)) {

                    ScreenContent(
                        screen,
                        cliente = cliente, // ← AGREGA ESTA LÍNEA
                        perfil = perfilActual,
                        correo = authRepository.usuarioAutenticado()?.email,
                        onCalendar = { screen = SaeScreen.CALENDAR },
                        onWellness = { screen = SaeScreen.WELLNESS },
                        onAbout = { screen = SaeScreen.ABOUT },
                        onBack = { screen = SaeScreen.PROFILE },
                        onExit = {
                            scope.launch {
                                try {
                                    authRepository.cerrarSesion()
                                    perfilActual = null
                                    errorLogin = null
                                    screen = SaeScreen.WELCOME
                                } catch (e: Exception) {
                                    errorLogin = "No se pudo cerrar la sesión"
                                }
                            }
                        }
                    )


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
    cliente: SupabaseClient,
    perfil: PerfilUsuario?,
    correo: String?,
    onCalendar: () -> Unit,
    onAbout: () -> Unit,
    onBack: () -> Unit,
    onExit: () -> Unit,
    onWellness: () -> Unit
) {

    when (screen) {
        SaeScreen.DASHBOARD -> DashboardScreen(
            onCalendar = onCalendar,
            onAbout = onAbout,
            onExit = onExit,
            onWellness = onWellness
        )
        SaeScreen.CALENDAR -> CalendarScreen(onBack)
        SaeScreen.SUBJECTS -> MateriasScreen(
            cliente = cliente,
            perfil = perfil
        )
        SaeScreen.RESOURCES -> {
            val usuarioId = perfil?.id

            if (usuarioId != null) {
                BibliotecaScreen(
                    usuarioId = usuarioId
                )
            } else {
                PendingSection(
                    "Biblioteca",
                    "▥",
                    "Debes iniciar sesión para acceder a tus recursos."
                )
            }
        }
        SaeScreen.WELLNESS -> {
            val usuarioId = perfil?.id

            if (usuarioId != null) {
                BienestarScreen(
                    usuarioId = usuarioId
                )
            } else {
                PendingSection(
                    "Mi bienestar",
                    "♡",
                    "Debes iniciar sesión para acceder a tu bienestar."
                )
            }
        }

        SaeScreen.PROFILE -> ProfileSection(
            perfil = perfil,
            correo = correo,
            onAbout = onAbout,
            onExit = onExit
        )

        SaeScreen.ABOUT -> AboutScreen(onBack)
        SaeScreen.WELCOME, SaeScreen.LOGIN -> Unit
    }
}
