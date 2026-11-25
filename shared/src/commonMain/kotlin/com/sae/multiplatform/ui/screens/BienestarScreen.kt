package com.sae.multiplatform.ui.screens



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sae.multiplatform.data.repository.BienestarRepository
import com.sae.multiplatform.data.repository.RegistroBienestar
import com.sae.multiplatform.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun BienestarScreen(
    usuarioId: String
) {
    key(usuarioId) {
        BienestarContenido(usuarioId)
    }
}

@Composable
private fun BienestarContenido(
    usuarioId: String
) {

    val repository = remember(usuarioId) {
        BienestarRepository(usuarioId)
    }

    var estadoAnimo by remember {
        mutableStateOf(3)
    }

    var estres by remember {
        mutableStateOf(3)
    }

    var horasSueno by remember {
        mutableStateOf(7f)
    }

    var cargaAcademica by remember {
        mutableStateOf(3)
    }

    var historial by remember {
        mutableStateOf<List<RegistroBienestar>>(
            emptyList()
        )
    }

    var ultimoRegistro by remember {
        mutableStateOf<RegistroBienestar?>(null)
    }

    var mostrarHistorial by remember {
        mutableStateOf(false)
    }

    var mensaje by remember {
        mutableStateOf<String?>(null)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    var registroEliminar by remember {
        mutableStateOf<RegistroBienestar?>(null)
    }

    var confirmarEliminarTodo by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(repository) {
        try {
            historial = repository.obtenerHistorial()
            ultimoRegistro = historial.firstOrNull()

        } catch (e: Exception) {
            error = "No fue posible cargar el historial."
        }
    }

    PageFrame {

        Text(
            text = "Mi bienestar",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = SaeNavy
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Un espacio para cuidarte mientras estudias.",
            color = SaeMuted
        )

        Spacer(Modifier.height(20.dp))

        // Navegación interna
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            if (!mostrarHistorial) {

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Mi bienestar")
                }

            } else {

                OutlinedButton(
                    onClick = {
                        mostrarHistorial = false
                        mensaje = null
                        error = null
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Mi bienestar")
                }
            }

            if (mostrarHistorial) {

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Historial")
                }

            } else {

                OutlinedButton(
                    onClick = {
                        mostrarHistorial = true
                        mensaje = null
                        error = null
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Historial")
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        mensaje?.let {
            SaePanel {
                Text(
                    text = it,
                    color = SaePurple,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(12.dp))
        }

        error?.let {
            SaePanel {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(12.dp))
        }

        if (!mostrarHistorial) {

            SaePanel(Modifier.fillMaxWidth()) {

                Text(
                    text = "¿Cómo te sientes hoy?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaeNavy
                )

                Spacer(Modifier.height(16.dp))

                IndicadorBienestar(
                    titulo = "Estado de ánimo",
                    valor = estadoAnimo,
                    descripcion = descripcionAnimo(
                        estadoAnimo
                    ),
                    minimo = "Muy mal",
                    maximo = "Muy bien",
                    onChange = {
                        estadoAnimo = it
                        mensaje = null
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = 16.dp
                    )
                )

                IndicadorBienestar(
                    titulo = "Nivel de estrés",
                    valor = estres,
                    descripcion = descripcionNivel(estres),
                    minimo = "Muy bajo",
                    maximo = "Muy alto",
                    onChange = {
                        estres = it
                        mensaje = null
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = 16.dp
                    )
                )

                Text(
                    text = "Horas de sueño",
                    fontWeight = FontWeight.Bold,
                    color = SaeNavy
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "$horasSueno horas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaePurple
                )

                Slider(
                    value = horasSueno,
                    onValueChange = {
                        horasSueno =
                            (it * 2).roundToInt() / 2f
                        mensaje = null
                    },
                    valueRange = 0f..24f,
                    steps = 47
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {
                    Text("0 h", color = SaeMuted)
                    Text("24 h", color = SaeMuted)
                }

                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = 16.dp
                    )
                )

                IndicadorBienestar(
                    titulo = "Carga académica",
                    valor = cargaAcademica,
                    descripcion = descripcionNivel(
                        cargaAcademica
                    ),
                    minimo = "Muy ligera",
                    maximo = "Muy alta",
                    onChange = {
                        cargaAcademica = it
                        mensaje = null
                    }
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),

                    onClick = {
                        try {

                            ultimoRegistro =
                                repository.guardarRegistro(
                                    estadoAnimo = estadoAnimo,
                                    estres = estres,
                                    horasSueno =
                                        horasSueno.toDouble(),
                                    cargaAcademica =
                                        cargaAcademica
                                )

                            historial =
                                repository.obtenerHistorial()

                            mensaje =
                                "Registro guardado correctamente."

                            error = null

                        } catch (e: Exception) {

                            error =
                                "No fue posible guardar el registro."

                            mensaje = null
                        }
                    }
                ) {
                    Text("Registrar bienestar")
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Tus respuestas se guardan localmente en este dispositivo.",
                    color = SaeMuted,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            ultimoRegistro?.let { registro ->

                RecomendacionesBienestar(
                    recomendaciones =
                        repository.obtenerRecomendaciones(
                            registro
                        )
                )
            }

        } else {

            Text(
                text = "Mi historial de bienestar",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SaeNavy
            )

            Spacer(Modifier.height(12.dp))

            if (historial.isEmpty()) {

                SaePanel {
                    Text(
                        text = "Todavía no tienes registros.",
                        fontWeight = FontWeight.Bold,
                        color = SaeNavy
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Completa tu primera encuesta para comenzar a registrar tu bienestar.",
                        color = SaeMuted
                    )
                }

            } else {

                Text(
                    text = "Registros realizados: ${historial.size}",
                    color = SaeMuted
                )

                Spacer(Modifier.height(12.dp))

                historial.forEach { registro ->

                    key(registro.id) {

                        RegistroBienestarCard(
                            registro = registro,
                            onEliminar = {
                                registroEliminar = registro
                            }
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                }

                OutlinedButton(
                    onClick = {
                        confirmarEliminarTodo = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Eliminar todo el historial",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        SaePanel {

            Text(
                text = "Cuida de ti",
                fontWeight = FontWeight.Bold,
                color = SaeNavy
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Este módulo ofrece recomendaciones generales y no reemplaza la atención de un profesional de la salud.",
                fontSize = 12.sp,
                color = SaeMuted
            )
        }
    }

    // Confirmación para eliminar un registro
    registroEliminar?.let { registro ->

        AlertDialog(
            onDismissRequest = {
                registroEliminar = null
            },

            title = {
                Text("Eliminar registro")
            },

            text = {
                Text(
                    "¿Deseas eliminar este registro de bienestar?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        try {

                            repository.eliminarRegistro(
                                registro.id
                            )

                            historial =
                                repository.obtenerHistorial()

                            ultimoRegistro =
                                historial.firstOrNull()

                            mensaje = "Registro eliminado."
                            error = null

                        } catch (e: Exception) {

                            error =
                                "No fue posible eliminar el registro."
                        }

                        registroEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        registroEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Confirmación para eliminar todo el historial
    if (confirmarEliminarTodo) {

        AlertDialog(
            onDismissRequest = {
                confirmarEliminarTodo = false
            },

            title = {
                Text("Eliminar historial")
            },

            text = {
                Text(
                    "Esta acción eliminará todos tus registros de bienestar de este dispositivo."
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        try {

                            repository.eliminarHistorial()

                            historial = emptyList()
                            ultimoRegistro = null

                            mensaje =
                                "Historial eliminado correctamente."

                            error = null

                        } catch (e: Exception) {

                            error =
                                "No fue posible eliminar el historial."
                        }

                        confirmarEliminarTodo = false
                    }
                ) {
                    Text("Eliminar todo")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        confirmarEliminarTodo = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun IndicadorBienestar(
    titulo: String,
    valor: Int,
    descripcion: String,
    minimo: String,
    maximo: String,
    onChange: (Int) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = titulo,
            fontWeight = FontWeight.Bold,
            color = SaeNavy
        )

        Text(
            text = "$valor/5",
            fontWeight = FontWeight.Bold,
            color = SaePurple
        )
    }

    Spacer(Modifier.height(6.dp))

    Text(
        text = descripcion,
        color = SaeMuted
    )

    Slider(
        value = valor.toFloat(),
        onValueChange = {
            onChange(it.roundToInt())
        },
        valueRange = 1f..5f,
        steps = 3
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = minimo,
            fontSize = 12.sp,
            color = SaeMuted
        )

        Text(
            text = maximo,
            fontSize = 12.sp,
            color = SaeMuted
        )
    }
}

@Composable
private fun RecomendacionesBienestar(
    recomendaciones: List<String>
) {

    SaePanel(Modifier.fillMaxWidth()) {

        Text(
            text = "Tus recomendaciones",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = SaeNavy
        )

        Spacer(Modifier.height(12.dp))

        recomendaciones.forEachIndexed { indice, texto ->

            Text(
                text = "${indice + 1}. $texto",
                color = SaeMuted
            )

            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun RegistroBienestarCard(
    registro: RegistroBienestar,
    onEliminar: () -> Unit
) {

    SaePanel(Modifier.fillMaxWidth()) {

        Text(
            text = registro.fecha
                .take(16)
                .replace("T", " ") + " UTC",
            fontWeight = FontWeight.Bold,
            color = SaeNavy
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Estado de ánimo: ${
                descripcionAnimo(registro.estadoAnimo)
            }",
            color = SaeMuted
        )

        Text(
            text = "Nivel de estrés: ${registro.estres}/5",
            color = SaeMuted
        )

        Text(
            text = "Horas de sueño: ${registro.horasSueno}",
            color = SaeMuted
        )

        Text(
            text = "Carga académica: ${registro.cargaAcademica}/5",
            color = SaeMuted
        )

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onEliminar
        ) {
            Text("Eliminar registro")
        }
    }
}

private fun descripcionAnimo(
    valor: Int
): String {

    return when (valor) {
        1 -> "Muy mal"
        2 -> "Mal"
        3 -> "Normal"
        4 -> "Bien"
        5 -> "Muy bien"
        else -> "Sin definir"
    }
}

private fun descripcionNivel(
    valor: Int
): String {

    return when (valor) {
        1 -> "Muy bajo"
        2 -> "Bajo"
        3 -> "Moderado"
        4 -> "Alto"
        5 -> "Muy alto"
        else -> "Sin definir"
    }
}
