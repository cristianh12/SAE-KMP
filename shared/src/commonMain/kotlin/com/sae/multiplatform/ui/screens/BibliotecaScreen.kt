
package com.sae.multiplatform.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.sae.multiplatform.data.repository.BibliotecaRepository
import com.sae.multiplatform.data.repository.FavoritosRepository
import com.sae.multiplatform.data.repository.Libro
import com.sae.multiplatform.ui.theme.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun BibliotecaScreen(
    usuarioId: String
) {
    key(usuarioId) {
        BibliotecaContenido(usuarioId)
    }
}

@Composable
private fun BibliotecaContenido(
    usuarioId: String
) {
    val repository = remember {
        BibliotecaRepository()
    }

    val favoritosRepository = remember(usuarioId) {
        FavoritosRepository(usuarioId)
    }

    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    var consulta by remember {
        mutableStateOf("")
    }

    var libros by remember {
        mutableStateOf<List<Libro>>(emptyList())
    }

    var favoritos by remember {
        mutableStateOf<List<Libro>>(emptyList())
    }

    var mostrarFavoritos by remember {
        mutableStateOf(false)
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    var busquedaRealizada by remember {
        mutableStateOf(false)
    }

    var mensajeError by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(favoritosRepository) {
        try {
            favoritos = favoritosRepository.obtenerFavoritos()
        } catch (e: Exception) {
            mensajeError = "No fue posible cargar tus favoritos."
        }
    }

    DisposableEffect(repository) {
        onDispose {
            repository.cerrar()
        }
    }

    PageFrame {

        Text(
            text = "Biblioteca",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = SaeNavy
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Encuentra recursos para complementar tu aprendizaje.",
            color = SaeMuted
        )

        Spacer(Modifier.height(20.dp))

        // Apartados de la biblioteca
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (!mostrarFavoritos) {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Explorar")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        mostrarFavoritos = false
                        mensajeError = null
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Explorar")
                }
            }

            if (mostrarFavoritos) {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Mis favoritos")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        mostrarFavoritos = true
                        mensajeError = null
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Mis favoritos")
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (!mostrarFavoritos) {

            SaePanel(Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    value = consulta,
                    onValueChange = {
                        consulta = it
                        mensajeError = null
                    },
                    label = {
                        Text("Buscar libros")
                    },
                    placeholder = {
                        Text("Ej. Programación, redes, Kotlin")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !cargando
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargando &&
                            consulta.isNotBlank(),
                    onClick = {
                        cargando = true
                        mensajeError = null
                        libros = emptyList()
                        busquedaRealizada = false

                        scope.launch {
                            try {
                                libros = repository.buscarLibros(
                                    consulta.trim()
                                )

                                busquedaRealizada = true

                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: Exception) {
                                mensajeError =
                                    "No fue posible consultar los libros. Verifica tu conexión."
                            } finally {
                                cargando = false
                            }
                        }
                    }
                ) {
                    Text("Buscar libros")
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Información proporcionada por Open Library",
                    fontSize = 12.sp,
                    color = SaeMuted
                )
            }

            Spacer(Modifier.height(20.dp))
        }

        mensajeError?.let { mensaje ->
            SaePanel {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(12.dp))
        }

        val librosVisibles = if (mostrarFavoritos) {
            favoritos
        } else {
            libros
        }

        when {
            cargando && !mostrarFavoritos -> {
                CircularProgressIndicator()

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Buscando libros...",
                    color = SaeMuted
                )
            }

            mostrarFavoritos && favoritos.isEmpty() -> {
                SaePanel {
                    Text(
                        text = "Todavía no tienes libros favoritos.",
                        fontWeight = FontWeight.Bold,
                        color = SaeNavy
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Explora la biblioteca y guarda los libros que más te interesen.",
                        color = SaeMuted
                    )
                }
            }

            librosVisibles.isNotEmpty() -> {

                Text(
                    text = if (mostrarFavoritos) {
                        "Libros guardados (${librosVisibles.size})"
                    } else {
                        "Resultados (${librosVisibles.size})"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaeNavy
                )

                Spacer(Modifier.height(12.dp))

                librosVisibles.forEach { libro ->

                    key(libro.id) {

                        val esFavorito = favoritos.any {
                            it.id == libro.id
                        }

                        LibroCard(
                            libro = libro,
                            esFavorito = esFavorito,

                            onAbrir = {
                                try {
                                    uriHandler.openUri(
                                        libro.enlace
                                    )
                                } catch (e: Exception) {
                                    mensajeError =
                                        "No fue posible abrir el enlace."
                                }
                            },

                            onFavorito = {
                                try {
                                    favoritosRepository
                                        .alternarFavorito(libro)

                                    favoritos =
                                        favoritosRepository
                                            .obtenerFavoritos()

                                    mensajeError = null

                                } catch (e: Exception) {
                                    mensajeError =
                                        "No fue posible actualizar tus favoritos."
                                }
                            }
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                }
            }

            busquedaRealizada -> {
                SaePanel {
                    Text(
                        text = "No se encontraron libros para esta búsqueda.",
                        color = SaeMuted
                    )
                }
            }

            mensajeError == null -> {
                SaePanel {
                    Text(
                        text = "Explora nuestra biblioteca",
                        fontWeight = FontWeight.Bold,
                        color = SaeNavy
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Busca por título, autor o tema para descubrir recursos académicos.",
                        color = SaeMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun LibroCard(
    libro: Libro,
    esFavorito: Boolean,
    onAbrir: () -> Unit,
    onFavorito: () -> Unit
) {
    SaePanel(Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Portada del libro
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SaePurpleSoft),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sin portada",
                    fontSize = 11.sp,
                    color = SaeMuted
                )

                if (libro.portadaUrl != null) {
                    AsyncImage(
                        model = libro.portadaUrl + "?default=false",
                        contentDescription = "Portada de ${libro.titulo}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Información del libro
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = libro.titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaeNavy
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Autor: ${libro.autores}",
                    color = SaeMuted,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Año: ${libro.anio ?: "No disponible"}",
                    color = SaeMuted,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onAbrir
                ) {
                    Text("Ver libro")
                }

                if (esFavorito) {
                    Button(
                        onClick = onFavorito
                    ) {
                        Text("★ Quitar favorito")
                    }
                } else {
                    OutlinedButton(
                        onClick = onFavorito
                    ) {
                        Text("☆ Guardar favorito")
                    }
                }
            }
        }
    }
}
