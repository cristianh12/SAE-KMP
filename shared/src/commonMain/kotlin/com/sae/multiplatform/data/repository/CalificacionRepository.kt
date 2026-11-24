
package com.sae.multiplatform.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull

data class Calificacion(
    val id: String,
    val evaluacionId: String,
    val estudianteId: String,
    val nota: Double,
    val publicada: Boolean
)

class CalificacionRepository(
    private val client: SupabaseClient
) {

    // Convertir un registro de Supabase en una calificación
    private fun convertirCalificacion(
        registro: JsonObject
    ): Calificacion {
        return Calificacion(
            id = registro["id"]
                ?.jsonPrimitive?.content
                ?: error("Calificación sin identificador"),

            evaluacionId = registro["evaluacion_id"]
                ?.jsonPrimitive?.content
                ?: error("Calificación sin evaluación"),

            estudianteId = registro["estudiante_id"]
                ?.jsonPrimitive?.content
                ?: error("Calificación sin estudiante"),

            nota = registro["nota"]
                ?.jsonPrimitive?.contentOrNull
                ?.toDoubleOrNull()
                ?: error("Calificación sin nota válida"),

            publicada = registro["publicada"]
                ?.jsonPrimitive?.contentOrNull
                ?.toBooleanStrictOrNull()
                ?: false
        )
    }

    // 1. Consultar notas publicadas del estudiante
    suspend fun obtenerMisCalificaciones(): List<Calificacion> {

        val usuario = client.auth.currentUserOrNull()
            ?: error("Debes iniciar sesión")

        val registros = client.from("calificaciones")
            .select {
                filter {
                    eq("estudiante_id", usuario.id)
                    eq("publicada", true)
                }
            }
            .decodeList<JsonObject>()

        return registros.map { convertirCalificacion(it) }
    }

    // 2. Registrar una nueva nota sin publicarla
    suspend fun registrarCalificacion(
        evaluacionId: String,
        estudianteId: String,
        nota: Double
    ) {
        client.auth.currentUserOrNull()
            ?: error("Debes iniciar sesión")

        require(evaluacionId.isNotBlank()) {
            "Debes seleccionar una evaluación"
        }

        require(estudianteId.isNotBlank()) {
            "Debes seleccionar un estudiante"
        }

        require(nota.isFinite() && nota in 0.0..5.0) {
            "La nota debe estar entre 0.0 y 5.0"
        }

        val datos = JsonObject(
            mapOf(
                "evaluacion_id" to JsonPrimitive(evaluacionId),
                "estudiante_id" to JsonPrimitive(estudianteId),
                "nota" to JsonPrimitive(nota),
                "publicada" to JsonPrimitive(false)
            )
        )

        val registros = client.from("calificaciones")
            .insert(datos) {
                select()
            }
            .decodeList<JsonObject>()

        check(registros.size == 1) {
            "No se pudo confirmar el registro de la calificación"
        }

        val guardada = convertirCalificacion(registros.single())

        check(
            guardada.evaluacionId == evaluacionId &&
                    guardada.estudianteId == estudianteId &&
                    guardada.nota == nota &&
                    !guardada.publicada
        ) {
            "La calificación guardada no coincide con los datos enviados"
        }
    }

    // 3. Modificar una nota existente
    suspend fun modificarCalificacion(
        calificacionId: String,
        nuevaNota: Double
    ) {
        client.auth.currentUserOrNull()
            ?: error("Debes iniciar sesión")

        require(calificacionId.isNotBlank()) {
            "Debes seleccionar una calificación"
        }

        require(nuevaNota.isFinite() && nuevaNota in 0.0..5.0) {
            "La nota debe estar entre 0.0 y 5.0"
        }

        val registros = client.from("calificaciones")
            .update(
                {
                    set("nota", nuevaNota)
                }
            ) {
                filter {
                    eq("id", calificacionId)
                }
                select()
            }
            .decodeList<JsonObject>()

        check(registros.size == 1) {
            "No se modificó ninguna calificación. Verifica los permisos."
        }

        val actualizada = convertirCalificacion(registros.single())

        check(
            actualizada.id == calificacionId &&
                    actualizada.nota == nuevaNota
        ) {
            "No se pudo confirmar la modificación de la nota"
        }
    }

    // 4. Publicar una calificación
    suspend fun publicarCalificacion(
        calificacionId: String
    ) {
        client.auth.currentUserOrNull()
            ?: error("Debes iniciar sesión")

        require(calificacionId.isNotBlank()) {
            "Debes seleccionar una calificación"
        }

        val registros = client.from("calificaciones")
            .update(
                {
                    set("publicada", true)
                }
            ) {
                filter {
                    eq("id", calificacionId)
                }
                select()
            }
            .decodeList<JsonObject>()

        check(registros.size == 1) {
            "No se publicó ninguna calificación. Verifica los permisos."
        }

        val publicada = convertirCalificacion(registros.single())

        check(
            publicada.id == calificacionId &&
                    publicada.publicada
        ) {
            "No se pudo confirmar la publicación de la nota"
        }
    }

    // 5. Consultar calificaciones de una evaluación
    suspend fun obtenerCalificacionesDeEvaluacion(
        evaluacionId: String
    ): List<Calificacion> {

        client.auth.currentUserOrNull()
            ?: error("Debes iniciar sesión")

        require(evaluacionId.isNotBlank()) {
            "Debes seleccionar una evaluación"
        }

        val registros = client.from("calificaciones")
            .select {
                filter {
                    eq("evaluacion_id", evaluacionId)
                }
            }
            .decodeList<JsonObject>()

        return registros.map { convertirCalificacion(it) }
    }
}
