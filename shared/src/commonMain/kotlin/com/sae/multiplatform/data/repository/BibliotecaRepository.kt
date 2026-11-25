package com.sae.multiplatform.data.repository


import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.*

data class Libro(
    val id: String,
    val titulo: String,
    val autores: String,
    val anio: Int?,
    val portadaId: Int?
) {
    val portadaUrl: String?
        get() = portadaId?.let {
            "https://covers.openlibrary.org/b/id/$it-M.jpg"
        }

    val enlace: String
        get() = "https://openlibrary.org$id"
}

class BibliotecaRepository {

    private val cliente = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 20000
            connectTimeoutMillis = 15000
        }
    }

    suspend fun buscarLibros(
        consulta: String
    ): List<Libro> {

        require(consulta.isNotBlank()) {
            "Debes ingresar un término de búsqueda"
        }

        val respuesta = cliente.get(
            "https://openlibrary.org/search.json"
        ) {
            parameter("q", consulta.trim())

            parameter(
                "fields",
                "key,title,author_name,first_publish_year,cover_i"
            )

            parameter("limit", 20)
        }

        check(respuesta.status.isSuccess()) {
            "No fue posible consultar Open Library"
        }

        val contenido = Json.parseToJsonElement(
            respuesta.bodyAsText()
        ).jsonObject

        val documentos = contenido["docs"]?.jsonArray
            ?: emptyList()

        return documentos.mapNotNull { elemento ->

            val registro = elemento.jsonObject

            val clave = registro["key"]
                ?.jsonPrimitive?.contentOrNull
                ?: return@mapNotNull null

            val id = when {
                clave.startsWith("/works/") -> clave
                clave.startsWith("OL") -> "/works/$clave"
                else -> return@mapNotNull null
            }

            val titulo = registro["title"]
                ?.jsonPrimitive?.contentOrNull
                ?: return@mapNotNull null

            val autores = (
                    registro["author_name"] as? JsonArray
                    )?.mapNotNull {
                    it.jsonPrimitive.contentOrNull
                }?.joinToString(", ")
                ?.takeIf { it.isNotBlank() }
                ?: "Autor desconocido"

            val anio = registro["first_publish_year"]
                ?.jsonPrimitive?.intOrNull

            val portadaId = registro["cover_i"]
                ?.jsonPrimitive?.intOrNull

            Libro(
                id = id,
                titulo = titulo,
                autores = autores,
                anio = anio,
                portadaId = portadaId
            )
        }.distinctBy { it.id }
    }

    fun cerrar() {
        cliente.close()
    }
}
