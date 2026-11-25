
# SAE-KMP — Sistema de Acompañamiento Estudiantil

Proyecto académico desarrollado con Kotlin Multiplatform y
Compose Multiplatform en Android Studio, dirigido a las plataformas
Android y Desktop (JVM/Windows).

SAE tiene como objetivo facilitar la organización académica,
el acceso a recursos educativos y el bienestar estudiantil
desde una misma aplicación.

## Funcionalidades implementadas

### Organización académica

- Pantalla de bienvenida e inicio de sesión.
- Dashboard estudiantil.
- Calendario mensual interactivo.
- Autenticación mediante Supabase.
- Perfiles de estudiantes y docentes.
- Gestión de materias, cursos y matrículas.
- Gestión de evaluaciones y calificaciones.
- Navegación adaptable para Android y Desktop.

### Biblioteca tecnológica

- Integración con la API pública de Open Library.
- Búsqueda de libros por título, autor o temática.
- Consulta de títulos, autores y años de publicación.
- Visualización de portadas mediante Coil.
- Acceso a las fichas originales de Open Library.
- Guardado y eliminación de libros favoritos.
- Persistencia local de favoritos por usuario.

### Bienestar estudiantil

- Registro del estado de ánimo.
- Registro del nivel de estrés.
- Registro de horas de sueño.
- Registro de la carga académica.
- Recomendaciones generales según las respuestas.
- Consulta del historial de bienestar.
- Eliminación individual y completa de registros.
- Persistencia local por usuario.

Las recomendaciones de bienestar son orientativas y no
reemplazan la atención de un profesional de la salud.

## Tecnologías utilizadas

| Tecnología | Función |
|---|---|
| Kotlin Multiplatform | Código compartido entre plataformas |
| Compose Multiplatform | Desarrollo de interfaces gráficas |
| Supabase | Autenticación y gestión de datos académicos |
| PostgreSQL | Base de datos de Supabase |
| Ktor | Comunicación mediante peticiones HTTP |
| Open Library API | Consulta de información bibliográfica |
| Coil | Carga de imágenes y portadas |
| Multiplatform Settings | Persistencia local |
| Git y GitHub | Control de versiones y colaboración |
| Gradle | Gestión de dependencias y compilación |

## Estructura del proyecto

```text
androidApp/
desktopApp/
shared/
  src/commonMain/
    composeResources/
    kotlin/com/sae/multiplatform/
      data/
        remote/
        repository/
      navigation/
      calendar/
      ui/
        screens/
        theme/
gradle/
docs/
```

## Requisitos

- Android Studio con soporte para Kotlin Multiplatform.
- JDK 21.
- Android SDK.
- Conexión a Internet.
- Proyecto de Supabase configurado.
- Archivo local de configuración de Supabase.

La configuración de Supabase debe realizarse siguiendo
las instrucciones de docs/PASO_01_SUPABASE.md.

No se deben subir credenciales privadas al repositorio.

## Compilación y ejecución

Desde PowerShell, en la raíz del proyecto:

### Compilar el módulo compartido

```powershell
.\gradlew.bat :shared:compileKotlinJvm
```

### Ejecutar Desktop

```powershell
.\gradlew.bat :desktopApp:run
```

### Compilar Android

```powershell
.\gradlew.bat :androidApp:assembleDebug
```

### Compilar Desktop

```powershell
.\gradlew.bat :desktopApp:compileKotlin
```

Para ejecutar Android también se puede seleccionar androidApp
y un emulador desde Android Studio.

## Equipo del proyecto

| Integrante | Responsabilidad |
|---|---|
| Cristian Hoyos | Arquitectura, diseño, navegación, Dashboard y calendario |
| Daniel Mañunga | Supabase, autenticación y gestión académica |
| Juan Guerrero | Biblioteca tecnológica, bienestar y persistencia local |

## Trabajo colaborativo

- Rama principal: main.
- Etapa 1: feature/cristian-hoyos.
- Etapa 2: feature/daniel-manunga.
- Etapa 3: feature/juanmi.

Los cambios se desarrollan en ramas individuales y se integran
mediante Pull Requests.

No se deben subir archivos de compilación, configuraciones
locales ni credenciales privadas.

## Estado de validación

- [x] Compilación del módulo compartido.
- [x] Compilación Android.
- [x] Compilación Desktop.
- [x] Implementación de la biblioteca tecnológica.
- [x] Implementación del módulo de bienestar.
- [x] Actualización de créditos.
- [x] Exclusión de SupabaseLocalConfig.kt del repositorio.
- [ ] Validación funcional completa en Android y Desktop.
- [ ] Confirmación de persistencia después de reiniciar.
- [ ] Verificación integral de eventos académicos persistentes.

Los favoritos y registros de bienestar se almacenan localmente
por usuario y dispositivo. No se sincronizan entre plataformas
y su almacenamiento no está cifrado por la aplicación.

La compilación exitosa no sustituye las pruebas funcionales.

## Documentación

- [Plan de etapas](docs/PLAN_ETAPAS.md)
- [Configuración de Supabase](docs/PASO_01_SUPABASE.md)
- [Pruebas de la etapa 1](docs/PRUEBAS_ETAPA_1.md)
- [Pruebas del calendario](docs/PRUEBAS_CALENDARIO.md)
- [Pruebas de la etapa 3](docs/PRUEBAS_ETAPA_3.md)

Proyecto académico — 2026.
