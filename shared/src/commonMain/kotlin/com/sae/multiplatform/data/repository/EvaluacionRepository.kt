
package com.sae.multiplatform.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull

data class Evaluacion(
    val id: String,
    val cursoId: String,
    val titulo: String,
    val porcentaje: Double
)

class EvaluacionRepository(
    private val client: SupabaseClient
) {

    suspend fun obtenerEvaluaciones(
        cursoId: String
    ): List<Evaluacion> {

        require(cursoId.isNotBlank()) {
            "Debes indicar un curso"
        }

        val registros = client.from("evaluaciones")
            .select {
                filter {
                    eq("curso_id", cursoId)
                }
            }
            .decodeList<JsonObject>()

        return registros.map { registro ->
            Evaluacion(
                id = registro["id"]
                    ?.jsonPrimitive?.content
                    ?: error("Evaluacion sin identificador"),

                cursoId = registro["curso_id"]
                    ?.jsonPrimitive?.content
                    ?: error("Evaluacion sin curso"),

                titulo = registro["titulo"]
                    ?.jsonPrimitive?.contentOrNull.orEmpty(),

                porcentaje = registro["porcentaje"]
                    ?.jsonPrimitive?.contentOrNull
                    ?.toDoubleOrNull()
                    ?: 0.0
            )
        }
    }

    suspend fun crearEvaluacion(
        cursoId: String,
        titulo: String,
        porcentaje: Double
    ) {
        client.auth.currentUserOrNull()
            ?: error("Debes iniciar sesión")

        require(cursoId.isNotBlank()) {
            "Debes seleccionar un curso"
        }

        require(titulo.isNotBlank()) {
            "Debes ingresar el título de la evaluación"
        }

        require(porcentaje > 0.0 && porcentaje <= 100.0) {
            "El porcentaje debe estar entre 0 y 100"
        }

        val datos = JsonObject(
            mapOf(
                "curso_id" to JsonPrimitive(cursoId),
                "titulo" to JsonPrimitive(titulo.trim()),
                "porcentaje" to JsonPrimitive(porcentaje)
            )
        )

        client.from("evaluaciones").insert(datos)
    }
}
