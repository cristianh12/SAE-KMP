package com.sae.multiplatform.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseProvider {

    fun crearCliente(
        projectUrl: String,
        publishableKey: String
    ): SupabaseClient {

        require(projectUrl.startsWith("https://")) {
            "La URL de Supabase debe utilizar HTTPS"
        }

        require(publishableKey.startsWith("sb_publishable_")) {
            "Debes utilizar la clave publica de Supabase"
        }

        return createSupabaseClient(
            supabaseUrl = projectUrl,
            supabaseKey = publishableKey
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
}
