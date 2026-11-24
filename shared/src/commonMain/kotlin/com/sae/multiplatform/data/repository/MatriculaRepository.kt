
package com.sae.multiplatform.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull

data class Matricula(
    val cursoId: String,
    val estudianteId: String,
    val matriculadoEn: String?
)

class MatriculaRepository(
    private val client: SupabaseClient
) {

    suspend fun obtenerMisMatriculas(): List<Matricula> {
        val usuario = client.auth.currentUserOrNull()
            ?: error("Debes iniciar sesion")

        val registros = client.from("matriculas")
            .select {
                filter {
                    eq("estudiante_id", usuario.id)
                }
            }
            .decodeList<JsonObject>()

        return registros.map { registro ->
            Matricula(
                cursoId = registro["curso_id"]
                    ?.jsonPrimitive?.contentOrNull
                    ?: error("Matricula sin curso"),

                estudianteId = registro["estudiante_id"]
                    ?.jsonPrimitive?.contentOrNull
                    ?: error("Matricula sin estudiante"),

                matriculadoEn = registro["matriculado_en"]
                    ?.jsonPrimitive?.contentOrNull
            )
        }
    }

    // Consultar estudiantes matriculados en un curso
    suspend fun obtenerMatriculasDeCurso(
        cursoId: String
    ): List<Matricula> {

        client.auth.currentUserOrNull()
            ?: error("Debes iniciar sesión")

        require(cursoId.isNotBlank()) {
            "Debes seleccionar un curso"
        }

        val registros = client.from("matriculas")
            .select {
                filter {
                    eq("curso_id", cursoId)
                }
            }
            .decodeList<JsonObject>()

        return registros.map { registro ->
            Matricula(
                cursoId = registro["curso_id"]
                    ?.jsonPrimitive?.contentOrNull
                    ?: error("Matrícula sin curso"),

                estudianteId = registro["estudiante_id"]
                    ?.jsonPrimitive?.contentOrNull
                    ?: error("Matrícula sin estudiante"),

                matriculadoEn = registro["matriculado_en"]
                    ?.jsonPrimitive?.contentOrNull
            )
        }
    }

}
