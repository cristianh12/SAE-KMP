
# Plan y responsabilidades — SAE-KMP

## Etapa 1 — Cristian Hoyos

Rama: feature/cristian-hoyos

- Configuración de Git y del proyecto Android y Desktop.
- Implementación de la arquitectura multiplataforma.
- Integración de la marca y el logo de SAE.
- Desarrollo del tema visual compartido.
- Implementación de la navegación.
- Desarrollo de las pantallas de bienvenida y Dashboard.
- Implementación del calendario mensual interactivo.
- Creación de la pantalla Acerca de SAE.
- Diseño adaptable para diferentes tamaños de pantalla.

## Etapa 2 — Daniel Mañunga

Rama: feature/daniel-manunga

- Integración de Supabase como servicio de datos.
- Configuración de autenticación.
- Implementación de perfiles y roles de usuario.
- Desarrollo de repositorios académicos.
- Gestión de cursos, materias y matrículas.
- Gestión de evaluaciones y calificaciones.
- Integración de las funcionalidades académicas
  con la interfaz compartida.

### Cambio respecto al plan inicial

La propuesta inicial contemplaba SQLDelight y SQLite.
Durante el desarrollo se decidió utilizar Supabase y
PostgreSQL para la gestión de datos académicos.

La persistencia de eventos académicos del calendario
queda pendiente de verificación integral.

## Etapa 3 — Juan Guerrero

Rama: feature/juanmi

### Biblioteca tecnológica

- Integración con Open Library mediante Ktor.
- Desarrollo de BibliotecaRepository.
- Implementación de BibliotecaScreen.
- Búsqueda de recursos bibliográficos reales.
- Visualización de portadas mediante Coil.
- Acceso a las fichas originales de los libros.
- Desarrollo de FavoritosRepository.
- Persistencia local de favoritos por usuario.

### Bienestar estudiantil

- Desarrollo de BienestarRepository.
- Implementación de BienestarScreen.
- Registro del estado de ánimo.
- Registro del nivel de estrés.
- Registro de horas de sueño.
- Registro de carga académica.
- Generación de recomendaciones mediante reglas.
- Consulta y eliminación del historial.
- Persistencia local mediante Multiplatform Settings.

### Integración final

- Conexión de la biblioteca con la navegación principal.
- Acceso a bienestar desde el Dashboard.
- Actualización de créditos y responsabilidades.
- Actualización de la documentación.
- Compilación del proyecto en Android y Desktop.
- Preparación de pruebas funcionales.
- Entrega mediante la rama individual.

## Criterios de aceptación final

- [x] Compilar el módulo compartido.
- [x] Compilar Android.
- [x] Compilar Desktop.
- [x] Integrar la biblioteca con Open Library.
- [x] Implementar favoritos locales.
- [x] Implementar las encuestas de bienestar.
- [x] Implementar el historial y recomendaciones.
- [x] Integrar las funcionalidades con la navegación.
- [x] Actualizar los créditos.
- [x] Excluir la configuración local de Supabase de Git.
- [ ] Confirmar las pruebas funcionales en Android y Desktop.
- [ ] Confirmar persistencia y separación de datos por usuario.
- [ ] Integrar las ramas mediante Pull Request.

## Consideraciones

Las funciones pendientes de comprobación no se consideran
validadas únicamente por una compilación exitosa.

Los resultados de la biblioteca provienen de Open Library,
sin utilizar libros de ejemplo en la aplicación.

Las recomendaciones de bienestar se generan mediante
condiciones programadas en Kotlin y no constituyen
diagnósticos médicos.

Los datos locales no se sincronizan entre dispositivos
y no cuentan con cifrado implementado por SAE.
