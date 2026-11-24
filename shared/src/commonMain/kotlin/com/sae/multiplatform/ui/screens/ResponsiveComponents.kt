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
import com.sae.multiplatform.data.repository.PerfilUsuario

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
fun ProfileSection(
    perfil: PerfilUsuario?,
    correo: String?,
    onAbout: () -> Unit,
    onExit: () -> Unit
) {
    PageFrame {
        Text(
            text = "Mi perfil",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = SaeNavy
        )

        Spacer(Modifier.height(20.dp))

        SaePanel(Modifier.fillMaxWidth()) {

            val nombreRol = when (perfil?.rol) {
                "docente" -> "Docente"
                "estudiante" -> "Estudiante"
                else -> "Sin rol asignado"
            }

            Text(
                text = "Información personal",
                fontWeight = FontWeight.Bold,
                color = SaeNavy,
                fontSize = 20.sp
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Nombre",
                fontWeight = FontWeight.SemiBold,
                color = SaeNavy
            )
            Text(
                text = perfil?.nombre
                    ?.takeIf { it.isNotBlank() }
                    ?: "Sin registrar",
                color = SaeMuted
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Correo institucional",
                fontWeight = FontWeight.SemiBold,
                color = SaeNavy
            )
            Text(
                text = correo ?: "No disponible",
                color = SaeMuted
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Rol",
                fontWeight = FontWeight.SemiBold,
                color = SaeNavy
            )
            Text(
                text = nombreRol,
                color = SaePurple,
                fontWeight = FontWeight.Bold
            )

            if (perfil?.rol == "estudiante") {
                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Programa académico",
                    fontWeight = FontWeight.SemiBold,
                    color = SaeNavy
                )
                Text(
                    text = perfil.programa
                        ?.takeIf { it.isNotBlank() }
                        ?: "Sin registrar",
                    color = SaeMuted
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "Semestre",
                    fontWeight = FontWeight.SemiBold,
                    color = SaeNavy
                )
                Text(
                    text = perfil.semestre?.toString()
                        ?: "Sin registrar",
                    color = SaeMuted
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(onClick = onAbout) {
                Text("Acerca de SAE y créditos")
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(onClick = onExit) {
                Text("Cerrar sesión")
            }
        }
    }
}
