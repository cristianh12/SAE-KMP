# Plan y responsabilidades — SAE-KMP

## Etapa 1 — Cristian Hoyos (`feature/cristian-hoyos`)
- Configurar Git y el proyecto Android + Desktop.
- Integrar la marca SAE, el tema, la estructura visual y la navegación compartida.
- Entregar Bienvenida, Dashboard inicial y Acerca de.
- Implementar calendario interactivo mensual con selección de fechas, disponible en Android y Desktop.
- Coordinar eventos académicos persistentes con la etapa 2 (todavía no implementados).

## Etapa 2 — Integrante 2 (rama individual)
- Integrar SQLDelight/SQLite; repositorios y operaciones reales.
- Crear materias, notas, trabajos y detalle de cada materia.
- Persistir eventos y recordatorios del calendario, sin datos de ejemplo.

## Etapa 3 — Integrante 3 (rama individual)
- Desarrollar Bienestar y su persistencia.
- Implementar la Biblioteca tecnológica con una API pública y guardados locales.
- Integrar y probar Android y Desktop, completar créditos y documentación.

## Criterios de aceptación de esta etapa
1. Compilar `:shared:compileKotlinJvm`.
2. Abrir Desktop sin excepciones y visualizar el logo SAE.
3. Navegar Bienvenida → Dashboard → Acerca de → Dashboard → Bienvenida.
4. Compilar y ejecutar Android para comprobar el mismo flujo.
5. Confirmar que el commit pertenece a `feature/cristian-hoyos`.

**Importante:** Las funciones aún no construidas no se anuncian como operativas ni
se rellenan con datos de mentira; cada integración se verificará antes de marcarse como hecha.
