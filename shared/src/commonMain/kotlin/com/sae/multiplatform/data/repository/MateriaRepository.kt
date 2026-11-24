
package com.sae.multiplatform.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull

data class Materia(
    val id: String,
    val codigo: String,
    val nombre: String,
    val creditos: Int
)

class MateriaRepository(
    private val client: SupabaseClient
) {
    suspend fun obtenerMaterias(): List<Materia> {
        val registros = client.from("materias")
            .select()
            .decodeList<JsonObject>()

        return registros.map { registro ->
            Materia(
                id = registro["id"]
                    ?.jsonPrimitive?.content
                    ?: error("Materia sin identificador"),
                codigo = registro["codigo"]
                    ?.jsonPrimitive?.contentOrNull.orEmpty(),
                nombre = registro["nombre"]
                    ?.jsonPrimitive?.contentOrNull.orEmpty(),
                creditos = registro["creditos"]
                    ?.jsonPrimitive?.contentOrNull
                    ?.toIntOrNull() ?: 0
            )
        }
    }
}
