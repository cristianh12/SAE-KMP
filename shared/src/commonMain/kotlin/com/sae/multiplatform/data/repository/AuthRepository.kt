
package com.sae.multiplatform.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepository(
    private val client: SupabaseClient
) {
    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ) {
        require(correo.isNotBlank()) {
            "Debes ingresar tu correo"
        }
        require(contrasena.isNotBlank()) {
            "Debes ingresar tu contraseña"
        }

        client.auth.signInWith(Email) {
            email = correo.trim()
            password = contrasena
        }
    }

    fun usuarioAutenticado() =
        client.auth.currentUserOrNull()

    suspend fun cerrarSesion() {
        client.auth.signOut()
    }
}
