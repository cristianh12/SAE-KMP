package com.sae.multiplatform.data.repository



import com.russhwolf.settings.Settings
import kotlinx.serialization.json.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.random.Random

data class RegistroBienestar(
    val id: String,
    val fecha: String,
    val estadoAnimo: Int,
    val estres: Int,
    val horasSueno: Double,
    val cargaAcademica: Int
)

class BienestarRepository(
    usuarioId: String
) {

    private val settings: Settings = Settings()

    private val prefijo = "sae_bienestar_${usuarioId}_"

    // Registrar una encuesta de bienestar
    @OptIn(ExperimentalTime::class)
    fun guardarRegistro(
        estadoAnimo: Int,
        estres: Int,
        horasSueno: Double,
        cargaAcademica: Int
    ): RegistroBienestar {

        require(estadoAnimo in 1..5) {
            "Selecciona un estado de ánimo válido"
        }

        require(estres in 1..5) {
            "Selecciona un nivel de estrés válido"
        }

        require(horasSueno.isFinite() &&
                horasSueno in 0.0..24.0) {
            "Ingresa una cantidad válida de horas de sueño"
        }

        require(cargaAcademica in 1..5) {
            "Selecciona una carga académica válida"
        }

        val ahora = Clock.System.now()

        val registro = RegistroBienestar(
            id = "${ahora.toEpochMilliseconds()}_${Random.nextLong()}",
            fecha = ahora.toString(),
            estadoAnimo = estadoAnimo,
            estres = estres,
            horasSueno = horasSueno,
            cargaAcademica = cargaAcademica
        )

        val contenido = buildJsonObject {
            put("id", registro.id)
            put("fecha", registro.fecha)
            put("estadoAnimo", registro.estadoAnimo)
            put("estres", registro.estres)
            put("horasSueno", registro.horasSueno)
            put("cargaAcademica", registro.cargaAcademica)
        }

        settings.putString(
            prefijo + registro.id,
            contenido.toString()
        )

        return registro
    }

    // Consultar el historial personal
    fun obtenerHistorial(): List<RegistroBienestar> {

        return settings.keys
            .filter { it.startsWith(prefijo) }
            .mapNotNull { clave ->

                val contenido = settings.getStringOrNull(clave)
                    ?: return@mapNotNull null

                val registro = Json
                    .parseToJsonElement(contenido)
                    .jsonObject

                RegistroBienestar(
                    id = registro["id"]
                        ?.jsonPrimitive?.contentOrNull
                        ?: return@mapNotNull null,

                    fecha = registro["fecha"]
                        ?.jsonPrimitive?.contentOrNull
                        ?: return@mapNotNull null,

                    estadoAnimo = registro["estadoAnimo"]
                        ?.jsonPrimitive?.intOrNull
                        ?: return@mapNotNull null,

                    estres = registro["estres"]
                        ?.jsonPrimitive?.intOrNull
                        ?: return@mapNotNull null,

                    horasSueno = registro["horasSueno"]
                        ?.jsonPrimitive?.doubleOrNull
                        ?: return@mapNotNull null,

                    cargaAcademica = registro["cargaAcademica"]
                        ?.jsonPrimitive?.intOrNull
                        ?: return@mapNotNull null
                )
            }
            .sortedByDescending {
                it.id.substringBefore("_").toLongOrNull()
                    ?: 0L
            }
    }

    // Eliminar un registro personal
    fun eliminarRegistro(registroId: String) {
        settings.remove(prefijo + registroId)
    }

    // Eliminar todo el historial del usuario
    fun eliminarHistorial() {

        settings.keys
            .filter { it.startsWith(prefijo) }
            .forEach { clave ->
                settings.remove(clave)
            }
    }

    // Generar recomendaciones generales de bienestar
    fun obtenerRecomendaciones(
        registro: RegistroBienestar
    ): List<String> {

        val recomendaciones = mutableListOf<String>()

        if (registro.estadoAnimo <= 2) {
            recomendaciones.add(
                "Dedica tiempo a una actividad que disfrutes y considera conversar con alguien de confianza."
            )
        }

        if (registro.estres >= 4) {
            recomendaciones.add(
                "Realiza pausas durante tus jornadas de estudio y organiza tus actividades por prioridad."
            )
        }

        if (registro.horasSueno < 7.0) {
            recomendaciones.add(
                "Procura mantener horarios regulares de descanso y dormir lo suficiente."
            )
        }

        if (registro.cargaAcademica >= 4) {
            recomendaciones.add(
                "Divide tus trabajos académicos en tareas pequeñas y distribúyelas durante la semana."
            )
        }

        if (registro.estadoAnimo <= 2 &&
            registro.estres >= 4) {

            recomendaciones.add(
                "Si el malestar persiste, considera acudir a los servicios de bienestar universitario."
            )
        }

        if (recomendaciones.isEmpty()) {
            recomendaciones.add(
                "Continúa manteniendo hábitos saludables y un equilibrio entre el estudio y el descanso."
            )
        }

        return recomendaciones
    }
}
