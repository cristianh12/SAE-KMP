
package com.sae.multiplatform.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sae.multiplatform.data.repository.*
import com.sae.multiplatform.ui.theme.*
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.launch

private data class CursoVisible(
    val curso: Curso,
    val materia: Materia,
    val evaluaciones: List<Evaluacion>
)

@Composable
fun MateriasScreen(
    cliente: SupabaseClient,
    perfil: PerfilUsuario?
) {
    var cursosVisibles by remember {
        mutableStateOf<List<CursoVisible>>(emptyList())
    }

    var calificaciones by remember {
        mutableStateOf<List<Calificacion>>(emptyList())
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var mensajeError by remember {
        mutableStateOf<String?>(null)
    }

    var revision by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(cliente, perfil?.id, perfil?.rol, revision) {
        cargando = true
        mensajeError = null
        cursosVisibles = emptyList()
        calificaciones = emptyList()

        try {
            val usuario = perfil
                ?: error("No hay un perfil autenticado")

            val materias = MateriaRepository(cliente)
                .obtenerMaterias()

            val cursos = CursoRepository(cliente)
                .obtenerCursos()

            val cursosPermitidos = when (usuario.rol) {
                "estudiante" -> {
                    val matriculas = MatriculaRepository(cliente)
                        .obtenerMisMatriculas()

                    val idsCursos = matriculas
                        .map { it.cursoId }
                        .toSet()

                    cursos.filter { it.id in idsCursos }
                }

                "docente" -> {
                    cursos.filter {
                        it.docenteId == usuario.id
                    }
                }

                else -> emptyList()
            }

            val evaluacionRepository =
                EvaluacionRepository(cliente)

            cursosVisibles = cursosPermitidos.mapNotNull { curso ->
                val materia = materias.firstOrNull {
                    it.id == curso.materiaId
                }

                if (materia == null) {
                    null
                } else {
                    CursoVisible(
                        curso = curso,
                        materia = materia,
                        evaluaciones = evaluacionRepository
                            .obtenerEvaluaciones(curso.id)
                    )
                }
            }

            if (usuario.rol == "estudiante") {
                calificaciones = CalificacionRepository(cliente)
                    .obtenerMisCalificaciones()
            }

        } catch (e: Exception) {
            mensajeError =
                "No fue posible cargar la información académica."
        } finally {
            cargando = false
        }
    }

    PageFrame {
        Text(
            text = if (perfil?.rol == "docente") {
                "Mis cursos"
            } else {
                "Mis materias"
            },
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = SaeNavy
        )

        Spacer(Modifier.height(20.dp))

        when {
            cargando -> {
                CircularProgressIndicator()
            }

            mensajeError != null -> {
                SaePanel {
                    Text(
                        text = mensajeError.orEmpty(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            cursosVisibles.isEmpty() -> {
                SaePanel {
                    Text(
                        text = "No hay cursos disponibles para tu cuenta.",
                        color = SaeMuted
                    )
                }
            }

            else -> {
                cursosVisibles.forEach { item ->

                    SaePanel(Modifier.fillMaxWidth()) {

                        Text(
                            text = item.materia.nombre,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaeNavy
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Código: ${item.materia.codigo}",
                            color = SaeMuted
                        )

                        Text(
                            text = "Período: ${item.curso.periodo}",
                            color = SaeMuted
                        )

                        Text(
                            text = "Grupo: ${item.curso.grupo}",
                            color = SaeMuted
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                        Text(
                            text = "Evaluaciones",
                            fontWeight = FontWeight.Bold,
                            color = SaeNavy
                        )

                        if (item.evaluaciones.isEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "No hay evaluaciones registradas.",
                                color = SaeMuted
                            )
                        }

                        item.evaluaciones.forEach { evaluacion ->
                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = "${evaluacion.titulo} (${evaluacion.porcentaje} %)",
                                color = SaeNavy,
                                fontWeight = FontWeight.Medium
                            )

                            if (perfil?.rol == "estudiante") {
                                val nota = calificaciones.firstOrNull {
                                    it.evaluacionId == evaluacion.id
                                }

                                Text(
                                    text = if (nota != null) {
                                        "Nota: ${nota.nota} / 5.0"
                                    } else {
                                        "Nota no publicada"
                                    },
                                    color = SaeMuted
                                )
                            }
                            if (perfil?.rol == "docente") {
                                Spacer(Modifier.height(12.dp))

                                GestionNotasForm(
                                    cliente = cliente,
                                    cursoId = item.curso.id,
                                    evaluacionId = evaluacion.id
                                )
                            }
                        }

                        // Formulario docente: uno por curso
                        if (perfil?.rol == "docente") {

                            Spacer(Modifier.height(18.dp))
                            HorizontalDivider()
                            Spacer(Modifier.height(18.dp))

                            CrearEvaluacionForm(
                                cliente = cliente,
                                cursoId = item.curso.id,
                                onCreada = {
                                    revision++
                                }
                            )
                        }

                    } // Fin de SaePanel

                    Spacer(Modifier.height(14.dp))

                } // Fin de cursosVisibles.forEach
            }
        }
    }
}

// Formulario para crear evaluaciones
@Composable
private fun CrearEvaluacionForm(
    cliente: SupabaseClient,
    cursoId: String,
    onCreada: () -> Unit
) {
    val repository = remember(cliente) {
        EvaluacionRepository(cliente)
    }

    val scope = rememberCoroutineScope()

    var titulo by remember(cursoId) {
        mutableStateOf("")
    }

    var porcentaje by remember(cursoId) {
        mutableStateOf("")
    }

    var guardando by remember {
        mutableStateOf(false)
    }

    var mensaje by remember {
        mutableStateOf<String?>(null)
    }

    var esError by remember {
        mutableStateOf(false)
    }

    Text(
        text = "Crear evaluación",
        fontWeight = FontWeight.Bold,
        color = SaeNavy,
        fontSize = 18.sp
    )

    Spacer(Modifier.height(12.dp))

    OutlinedTextField(
        value = titulo,
        onValueChange = {
            titulo = it
            mensaje = null
        },
        label = {
            Text("Título de la evaluación")
        },
        placeholder = {
            Text("Ej. Segundo parcial")
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = !guardando,
        singleLine = true
    )

    Spacer(Modifier.height(10.dp))

    OutlinedTextField(
        value = porcentaje,
        onValueChange = {
            porcentaje = it
            mensaje = null
        },
        label = {
            Text("Porcentaje (%)")
        },
        placeholder = {
            Text("Ej. 30")
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = !guardando,
        singleLine = true
    )

    Spacer(Modifier.height(12.dp))

    if (mensaje != null) {
        Text(
            text = mensaje.orEmpty(),
            color = if (esError) {
                MaterialTheme.colorScheme.error
            } else {
                SaePurple
            }
        )

        Spacer(Modifier.height(10.dp))
    }

    Button(
        enabled = !guardando,
        onClick = {
            val valor = porcentaje
                .trim()
                .replace(',', '.')
                .toDoubleOrNull()

            when {
                titulo.isBlank() -> {
                    esError = true
                    mensaje = "Ingresa un título"
                }

                valor == null ||
                        !valor.isFinite() ||
                        valor <= 0.0 ||
                        valor > 100.0 -> {
                    esError = true
                    mensaje =
                        "El porcentaje debe ser mayor que 0 y máximo 100"
                }

                else -> {
                    guardando = true
                    mensaje = null

                    scope.launch {
                        try {
                            repository.crearEvaluacion(
                                cursoId = cursoId,
                                titulo = titulo.trim(),
                                porcentaje = valor
                            )

                            titulo = ""
                            porcentaje = ""

                            esError = false
                            mensaje = "Evaluación registrada"

                            onCreada()

                        } catch (e: Exception) {
                            esError = true
                            mensaje =
                                "No se pudo crear la evaluación. Verifica tus permisos y conexión."
                        } finally {
                            guardando = false
                        }
                    }
                }
            }
        }
    ) {
        if (guardando) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text("Guardar evaluación")
        }
    }
}

@Composable
private fun GestionNotasForm(
    cliente: SupabaseClient,
    cursoId: String,
    evaluacionId: String
) {
    val scope = rememberCoroutineScope()

    val matriculaRepository = remember(cliente) {
        MatriculaRepository(cliente)
    }

    val calificacionRepository = remember(cliente) {
        CalificacionRepository(cliente)
    }

    var matriculas by remember(cursoId) {
        mutableStateOf<List<Matricula>>(emptyList())
    }

    var notas by remember(evaluacionId) {
        mutableStateOf<List<Calificacion>>(emptyList())
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var mensaje by remember {
        mutableStateOf<String?>(null)
    }

    var revision by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(cliente, cursoId, evaluacionId, revision) {
        cargando = true
        mensaje = null

        try {
            matriculas = matriculaRepository
                .obtenerMatriculasDeCurso(cursoId)

            notas = calificacionRepository
                .obtenerCalificacionesDeEvaluacion(evaluacionId)

        } catch (e: Exception) {
            mensaje = "No se pudieron consultar las notas."
        } finally {
            cargando = false
        }
    }

    Text(
        text = "Gestión de calificaciones",
        fontWeight = FontWeight.Bold,
        color = SaeNavy
    )

    Spacer(Modifier.height(10.dp))

    if (cargando) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp)
        )
    } else {
        if (matriculas.isEmpty()) {
            Text(
                text = "No hay matrículas visibles en este curso.",
                color = SaeMuted
            )
        }

        matriculas.forEach { matricula ->

            val notaActual = notas.firstOrNull {
                it.estudianteId == matricula.estudianteId
            }

            key(matricula.estudianteId) {

                var notaTexto by remember(
                    evaluacionId,
                    matricula.estudianteId,
                    notaActual?.nota
                ) {
                    mutableStateOf(
                        notaActual?.nota?.toString().orEmpty()
                    )
                }

                var procesando by remember {
                    mutableStateOf(false)
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        text = "Estudiante: " +
                                matricula.estudianteId.take(8) + "...",
                        color = SaeNavy,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = when {
                            notaActual == null -> "Sin calificación"
                            notaActual.publicada -> "Publicada"
                            else -> "Sin publicar"
                        },
                        color = SaeMuted
                    )

                    OutlinedTextField(
                        value = notaTexto,
                        onValueChange = {
                            notaTexto = it
                        },
                        label = {
                            Text("Nota de 0.0 a 5.0")
                        },
                        singleLine = true,
                        enabled = !procesando,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        enabled = !procesando,
                        onClick = {
                            val valor = notaTexto.trim()
                                .replace(',', '.')
                                .toDoubleOrNull()

                            if (valor == null ||
                                !valor.isFinite() ||
                                valor !in 0.0..5.0
                            ) {
                                mensaje = "Ingresa una nota válida entre 0 y 5."
                            } else {
                                procesando = true

                                scope.launch {
                                    try {
                                        if (notaActual == null) {
                                            calificacionRepository
                                                .registrarCalificacion(
                                                    evaluacionId,
                                                    matricula.estudianteId,
                                                    valor
                                                )
                                        } else {
                                            calificacionRepository
                                                .modificarCalificacion(
                                                    notaActual.id,
                                                    valor
                                                )
                                        }

                                        revision++
                                    } catch (e: Exception) {
                                        mensaje = "No fue posible guardar la nota. Revisa los permisos."
                                    } finally {
                                        procesando = false
                                    }
                                }
                            }
                        }
                    ) {
                        Text(
                            if (notaActual == null)
                                "Registrar nota"
                            else
                                "Modificar nota"
                        )
                    }

                    if (notaActual != null &&
                        !notaActual.publicada
                    ) {
                        Spacer(Modifier.height(6.dp))

                        OutlinedButton(
                            enabled = !procesando,
                            onClick = {
                                procesando = true

                                scope.launch {
                                    try {
                                        calificacionRepository
                                            .publicarCalificacion(
                                                notaActual.id
                                            )

                                        revision++
                                    } catch (e: Exception) {
                                        mensaje = "No fue posible publicar la nota."
                                    } finally {
                                        procesando = false
                                    }
                                }
                            }
                        ) {
                            Text("Publicar nota")
                        }
                    }
                }

                HorizontalDivider()
            }
        }
    }

    if (mensaje != null) {
        Spacer(Modifier.height(8.dp))

        Text(
            text = mensaje.orEmpty(),
            color = MaterialTheme.colorScheme.error
        )
    }
}