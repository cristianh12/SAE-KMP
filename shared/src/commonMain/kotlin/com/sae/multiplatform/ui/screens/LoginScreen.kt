
package com.sae.multiplatform.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sae.multiplatform.ui.theme.*

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onBack: () -> Unit,
    errorMessage: String? = null,
    isLoading: Boolean = false
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(SaeBackground),
        contentAlignment = Alignment.Center
    ) {
        val anchoFormulario = if (maxWidth > 480.dp) 420.dp
        else maxWidth

        Column(
            modifier = Modifier
                .width(anchoFormulario)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "SAE",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SaePurple
            )

            Text(
                text = "Bienvenido de nuevo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SaePurple,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Sistema de Acompañamiento Estudiantil",
                color = SaeMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    errorLocal = null
                },
                label = { Text("Correo electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    errorLocal = null
                },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp)
            )

            val mensajeError = errorLocal ?: errorMessage

            if (mensajeError != null) {
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {
                    when {
                        correo.isBlank() ->
                            errorLocal = "Ingresa tu correo electrónico"

                        contrasena.isBlank() ->
                            errorLocal = "Ingresa tu contraseña"

                        else -> {
                            errorLocal = null
                            onLogin(correo.trim(), contrasena)
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SaePurple
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Iniciar sesión")
                }
            }

            TextButton(
                onClick = onBack,
                enabled = !isLoading
            ) {
                Text("Volver a bienvenida", color = SaePurple)
            }
        }
    }
}
