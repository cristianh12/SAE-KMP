
package com.sae.multiplatform.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull

data class Curso(
    val id: String,
    val materiaId: String,
    val docenteId: String,
    val periodo: String,
    val grupo: String
)

class CursoRepository(
    private val client: SupabaseClient
) {
    suspend fun obtenerCursos(): List<Curso> {
        val registros = client.from("cursos")
            .select()
            .decodeList<JsonObject>()

        return registros.map { registro ->
            Curso(
                id = registro["id"]?.jsonPrimitive?.content
                    ?: error("Curso sin identificador"),
                materiaId = registro["materia_id"]?.jsonPrimitive?.content
                    ?: error("Curso sin materia"),
                docenteId = registro["docente_id"]?.jsonPrimitive?.content
                    ?: error("Curso sin docente"),
                periodo = registro["periodo"]
                    ?.jsonPrimitive?.contentOrNull.orEmpty(),
                grupo = registro["grupo"]
                    ?.jsonPrimitive?.contentOrNull.orEmpty()
            )
        }
    }

    suspend fun crearCurso(
        materiaId: String,
        periodo: String,
        grupo: String = "A"
    ) {
        val docente = client.auth.currentUserOrNull()
            ?: error("Debes iniciar sesión")

        require(materiaId.isNotBlank())
        require(periodo.isNotBlank())
        require(grupo.isNotBlank())

        val datos = JsonObject(
            mapOf(
                "materia_id" to JsonPrimitive(materiaId),
                "docente_id" to JsonPrimitive(docente.id),
                "periodo" to JsonPrimitive(periodo),
                "grupo" to JsonPrimitive(grupo)
            )
        )

        client.from("cursos").insert(datos)
    }
}
