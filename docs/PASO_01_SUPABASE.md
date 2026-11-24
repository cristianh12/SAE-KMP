# SAE-KMP — Daniel | Paso 01: base de datos y autorización

## 1. Requisitos
- Estar en `feature/daniel-manunga` y con árbol limpio (`git status`).
- Proyecto Supabase propio `sae-kmp` creado desde https://supabase.com/dashboard.
- No colocar contraseñas, `service_role` ni claves secretas en archivos del repositorio.

## 2. Ejecutar migración
1. Entrar a Supabase Dashboard > proyecto SAE > **SQL Editor** > New query.
2. Abrir `supabase/migrations/20261119_001_academico.sql` en editor local, copiar contenido y ejecutar **Run**.
3. Usar un proyecto nuevo. La migración está diseñada para ejecutarse una sola vez, no para reejecuciones.
4. Si aparece error, copiar el texto exacto; la transacción `BEGIN/COMMIT` evita tablas a medias en errores normales.
5. Comprobar en **Database > Tables** las ocho tablas: perfiles, roles_usuario, materias, cursos, matriculas, evaluaciones, calificaciones, historial_notas.
6. En **Database > Policies**, confirmar RLS activo en todas las tablas.

## 3. Pruebas de estructura (SQL Editor)
```sql
select tablename, rowsecurity
from pg_tables
where schemaname = 'public'
  and tablename in ('perfiles','roles_usuario','materias','cursos','matriculas','evaluaciones','calificaciones','historial_notas')
order by tablename;
```
Deben aparecer ocho filas con `rowsecurity = true`.

## 4. Cuentas independientes
En **Authentication > Users**, crear dos usuarios de prueba con correos que controlen ustedes. El trigger crea automáticamente perfiles y les asigna rol `estudiante`. Para designar al docente, desde **SQL Editor** y solo como administrador del proyecto:
```sql
-- Sustituir por el UUID REAL del usuario docente en Authentication > Users.
update public.roles_usuario
set rol = 'docente'
where usuario_id = 'REEMPLAZAR-POR-UUID-DEL-DOCENTE';
```
Nunca permitir que la app asigne directamente el rol docente.

## 5. Datos reales de prueba (solo tras verificar los dos usuarios)
Insertar una materia desde SQL Editor:
```sql
insert into public.materias (codigo, nombre, creditos)
values ('REDES-01', 'Redes de Computadores', 4);
```
Luego, el docente crea un curso con su cuenta autenticada (próxima etapa Kotlin). Para una prueba administrativa inicial, se puede insertar un curso desde SQL Editor usando los UUID reales de materia y docente. Registrar la matrícula de un estudiante desde SQL Editor; la app de cliente no tiene permisos para falsificar matrículas.

## 6. Reglas de seguridad previstas
- Un usuario nuevo es `estudiante` por defecto.
- No se admiten actualizaciones de rol desde el cliente.
- Un estudiante solo ve notas publicadas de sus propios cursos.
- Un docente administra evaluaciones y calificaciones de sus cursos.
- Las notas se validan contra la matrícula.
- Insertar, actualizar o borrar notas crea un registro de auditoría no editable por clientes.
- Un estudiante puede actualizar solo campos permitidos de su propio perfil.
- La matrícula y el catálogo de materias se gestionan administrativamente en esta primera etapa.

## 7. Antes del primer commit de Daniel
```powershell
git branch --show-current
git status
git add supabase/migrations/20261119_001_academico.sql docs/PASO_01_SUPABASE.md
git diff --cached --check
git commit -m "feat(daniel): definir esquema academico y politicas RLS"
git push origin feature/daniel-manunga
```
**Solo confirmar después de ejecutar el SQL sin errores, revisar las políticas y verificar las ocho tablas.**

## 8. Continuación
Siguiente paso: revisar `gradle/libs.versions.toml`, `shared/build.gradle.kts` y versiones exactas Kotlin/Ktor; integrar SDK Supabase de forma compatible, sin inventar versiones. Luego login, perfil, cursos, notas y calendario.

## Atención sobre producción
Esta es una primera migración para un prototipo académico. Antes de exponerlo a usuarios reales, revisar permisos con cuentas separadas, casos negativos, retención de auditorías, cambios de curso/porcentaje y gestión administrativa de roles y matrículas. No cargar datos personales sensibles reales sin autorización.
