# SAE-KMP — Sistema de Acompañamiento Estudiantil

Microproyecto de **Kotlin Multiplatform** realizado en Android Studio para **Android + Desktop (JVM/Windows)**.
El proyecto se basa en el concepto de SAE y comparte el código de la interfaz mediante **Compose Multiplatform**.

## Estado de esta entrega: etapa 1 + calendario navegable

- [x] Plantilla Android / Desktop e inicialización de Git.
- [x] Logo SAE oficial en `shared/src/commonMain/composeResources/drawable/sae_logo.png`.
- [x] Configuración de la clase generada `Res` para cargar el logo.
- [x] Tema visual compartido.
- [x] Bienvenida, Dashboard inicial y Acerca de SAE.
- [x] Navegación de esas tres pantallas en `commonMain`.
- [x] Calendario mensual navegable, selección de fecha y regreso a hoy (sin eventos simulados).
- [ ] Crear y consultar eventos académicos con persistencia (integración con repositorio de etapa 2).
- [ ] Materias, notas y trabajos con persistencia.
- [ ] Bienestar con persistencia.
- [ ] Biblioteca de telemática conectada a una API.
- [ ] Validación final de funcionalidades en las dos plataformas.

**Los datos académicos no están simulados**: el Dashboard presenta información de estado
mientras aún no hay repositorios que permitan obtener datos reales. En etapas posteriores
se mostrará exclusivamente información registrada por el usuario o recibida de la API.

## Carpetas

```text
androidApp/        Entrada Android y recursos específicos del sistema
desktopApp/        Entrada Windows Desktop (JVM)
shared/
  src/commonMain/
    composeResources/drawable/sae_logo.png
    kotlin/com/sae/multiplatform/
      App.kt
      navigation/SaeScreen.kt
      navigation/SaeNavigationHost.kt
      ui/screens/WelcomeScreen.kt
      ui/screens/DashboardScreen.kt
      ui/screens/AboutScreen.kt
      ui/screens/CalendarScreen.kt
      calendar/CalendarDate.kt
      calendar/Today.kt
      ui/theme/SaeTheme.kt
docs/              Plan de las tres etapas y comprobaciones
```

## Requisitos

Android Studio con soporte para KMP, JDK compatible con la plantilla (JDK 21),
Android SDK y conexión a Internet la primera vez para descargar las dependencias.

En PowerShell, desde la raíz del proyecto:

```powershell
.\gradlew.bat :shared:compileKotlinJvm
.\gradlew.bat :desktopApp:run
.\gradlew.bat :androidApp:assembleDebug
```

La ventana de Desktop se cierra para finalizar `:desktopApp:run`.
Para Android también puedes elegir `androidApp` y un emulador desde Android Studio.

Si la IDE todavía marca el import `com.sae.multiplatform.resources.Res`, pulsa
**Sync Project with Gradle Files** y compila de nuevo. La clase `Res` se genera
automáticamente a partir de `shared/build.gradle.kts` y los recursos de `composeResources`.

## Git / trabajo colaborativo

- Rama base: `main`.
- Primera etapa: `feature/cristian-hoyos`.
- Las otras etapas deben desarrollarse en ramas individuales nuevas, partiendo de `main`
  después de integrar la anterior mediante Pull Request.
- Registrar cambios reales y verificables. No subir `.gradle`, `build`, `.idea`,
  `local.properties` ni credenciales.

Ver [docs/PLAN_ETAPAS.md](docs/PLAN_ETAPAS.md) y
[docs/PRUEBAS_ETAPA_1.md](docs/PRUEBAS_ETAPA_1.md).
