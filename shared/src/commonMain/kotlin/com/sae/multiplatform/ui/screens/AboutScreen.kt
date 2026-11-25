
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

            Image(
                painterResource(Res.drawable.sae_logo),
                "Logo de SAE",
                Modifier
                    .size(108.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Acerca de SAE",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = SaeNavy,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Sistema de Acompañamiento Estudiantil",
                fontWeight = FontWeight.Bold,
                color = SaePurple,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "SAE es una aplicación multiplataforma desarrollada para facilitar la organización académica, el acceso a recursos educativos y el bienestar de los estudiantes.",
                color = SaeMuted,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Equipo del proyecto",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SaeNavy
            )

            Spacer(Modifier.height(16.dp))

            IntegranteCard(
                nombre = "Cristian Hoyos",
                responsabilidad = "Arquitectura y diseño",
                descripcion = "Arquitectura multiplataforma, diseño de interfaces, navegación, Dashboard y calendario."
            )

            Spacer(Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(Modifier.height(16.dp))

            IntegranteCard(
                nombre = "Daniel Mañunga",
                responsabilidad = "Gestión académica y base de datos",
                descripcion = "Integración con Supabase, autenticación, perfiles, materias, evaluaciones y calificaciones."
            )

            Spacer(Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(Modifier.height(16.dp))

            IntegranteCard(
                nombre = "Juan Guerrero",
                responsabilidad = "Biblioteca y bienestar estudiantil",
                descripcion = "Integración con Open Library, búsqueda de libros, portadas, favoritos, encuestas de bienestar, historial y persistencia local."
            )

            Spacer(Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Tecnologías utilizadas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SaeNavy
            )

            Spacer(Modifier.height(12.dp))

            val tecnologias = listOf(
                "Kotlin Multiplatform",
                "Compose Multiplatform",
                "Supabase",
                "Ktor",
                "Open Library API",
                "Coil",
                "Multiplatform Settings"
            )

            tecnologias.forEach { tecnologia ->

                Text(
                    text = "• $tecnologia",
                    color = SaeMuted,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(5.dp))
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Proyecto académico · 2026",
                fontSize = 12.sp,
                color = SaeMuted,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al perfil")
            }
        }
    }
}

@Composable
private fun IntegranteCard(
    nombre: String,
    responsabilidad: String,
    descripcion: String
) {

    Text(
        text = nombre,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = SaeNavy
    )

    Spacer(Modifier.height(4.dp))

    Text(
        text = responsabilidad,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = SaePurple
    )

    Spacer(Modifier.height(6.dp))

    Text(
        text = descripcion,
        fontSize = 13.sp,
        color = SaeMuted
    )
}
