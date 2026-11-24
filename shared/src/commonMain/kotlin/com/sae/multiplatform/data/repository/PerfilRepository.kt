
package com.sae.multiplatform.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull

data class PerfilUsuario(
    val id: String,
    val nombre: String,
    val programa: String?,
    val semestre: Int?,
    val rol: String
)

class PerfilRepository(
    private val client: SupabaseClient
) {
    suspend fun obtenerMiPerfil(): PerfilUsuario {
        val usuario = client.auth.currentUserOrNull()
            ?: error("Debes iniciar sesion primero")

        val perfil = client.from("perfiles")
            .select {
                filter {
                    eq("id", usuario.id)
                }
            }
            .decodeSingle<JsonObject>()

        val registroRol = client.from("roles_usuario")
            .select {
                filter {
                    eq("usuario_id", usuario.id)
                }
            }
            .decodeSingle<JsonObject>()

        return PerfilUsuario(
            id = usuario.id,
            nombre = perfil["nombre"]
                ?.jsonPrimitive?.contentOrNull.orEmpty(),
            programa = perfil["programa"]
                ?.jsonPrimitive?.contentOrNull,
            semestre = perfil["semestre"]
                ?.jsonPrimitive?.contentOrNull?.toIntOrNull(),
            rol = registroRol["rol"]
                ?.jsonPrimitive?.contentOrNull
                ?: error("La cuenta no tiene un rol asignado")
        )
    }
}
