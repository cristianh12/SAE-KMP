package com.sae.multiplatform.data.repository



import com.russhwolf.settings.Settings
import kotlinx.serialization.json.*

class FavoritosRepository(
    usuarioId: String
) {

    private val settings: Settings = Settings()

    private val prefijo = "sae_favoritos_${usuarioId}_"

    // Obtener todos los libros guardados
    fun obtenerFavoritos(): List<Libro> {

        return settings.keys
            .filter { it.startsWith(prefijo) }
            .mapNotNull { clave ->

                val contenido = settings.getStringOrNull(clave)
                    ?: return@mapNotNull null

                val registro = Json
                    .parseToJsonElement(contenido)
                    .jsonObject

                Libro(
                    id = registro["id"]
                        ?.jsonPrimitive?.contentOrNull
                        ?: return@mapNotNull null,

                    titulo = registro["titulo"]
                        ?.jsonPrimitive?.contentOrNull
                        ?: return@mapNotNull null,

                    autores = registro["autores"]
                        ?.jsonPrimitive?.contentOrNull
                        ?: "Autor desconocido",

                    anio = registro["anio"]
                        ?.jsonPrimitive?.intOrNull,

                    portadaId = registro["portadaId"]
                        ?.jsonPrimitive?.intOrNull
                )
            }
            .sortedBy { it.titulo.lowercase() }
    }

    // Guardar un libro como favorito
    fun guardarFavorito(libro: Libro) {

        val contenido = buildJsonObject {
            put("id", libro.id)
            put("titulo", libro.titulo)
            put("autores", libro.autores)
            put("anio", JsonPrimitive(libro.anio))
            put("portadaId", JsonPrimitive(libro.portadaId))
        }

        settings.putString(
            claveLibro(libro.id),
            contenido.toString()
        )
    }

    // Eliminar un libro de favoritos
    fun eliminarFavorito(libroId: String) {
        settings.remove(claveLibro(libroId))
    }

    // Comprobar si un libro ya está guardado
    fun esFavorito(libroId: String): Boolean {
        return settings.hasKey(claveLibro(libroId))
    }

    // Cambiar el estado de favorito
    fun alternarFavorito(libro: Libro): Boolean {

        return if (esFavorito(libro.id)) {
            eliminarFavorito(libro.id)
            false
        } else {
            guardarFavorito(libro)
            true
        }
    }

    private fun claveLibro(libroId: String): String {
        return prefijo + libroId.substringAfterLast("/")
    }
}
