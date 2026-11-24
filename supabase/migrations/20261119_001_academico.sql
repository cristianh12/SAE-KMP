-- SAE-KMP | Migración 001 | Ejecutar una sola vez en Supabase SQL Editor.
-- Requiere un proyecto Supabase con auth.users. No pegar contraseñas ni claves privadas.
begin;

create schema if not exists private;

create table public.perfiles (
  id uuid primary key references auth.users(id) on delete cascade,
  nombre text not null default '',
  fecha_nacimiento date,
  programa text,
  semestre smallint check (semestre between 1 and 20),
  codigo_estudiantil text,
  creado_en timestamptz not null default now(),
  actualizado_en timestamptz not null default now()
);

create table public.roles_usuario (
  usuario_id uuid primary key references auth.users(id) on delete cascade,
  rol text not null default 'estudiante' check (rol in ('estudiante','docente','administrador'))
);

create table public.materias (
  id uuid primary key default gen_random_uuid(),
  codigo text not null unique,
  nombre text not null,
  creditos smallint not null default 0 check (creditos between 0 and 30)
);

create table public.cursos (
  id uuid primary key default gen_random_uuid(),
  materia_id uuid not null references public.materias(id),
  docente_id uuid not null references auth.users(id),
  periodo text not null,
  grupo text not null default 'A',
  creado_en timestamptz not null default now(),
  unique (materia_id, docente_id, periodo, grupo)
);

create table public.matriculas (
  curso_id uuid not null references public.cursos(id) on delete cascade,
  estudiante_id uuid not null references auth.users(id) on delete cascade,
  matriculado_en timestamptz not null default now(),
  primary key(curso_id, estudiante_id)
);

create table public.evaluaciones (
  id uuid primary key default gen_random_uuid(),
  curso_id uuid not null references public.cursos(id) on delete cascade,
  titulo text not null,
  porcentaje numeric(5,2) not null check (porcentaje > 0 and porcentaje <= 100),
  fecha date,
  creada_en timestamptz not null default now()
);

create table public.calificaciones (
  id uuid primary key default gen_random_uuid(),
  evaluacion_id uuid not null references public.evaluaciones(id) on delete cascade,
  estudiante_id uuid not null references auth.users(id) on delete cascade,
  nota numeric(3,2) not null check (nota >= 0 and nota <= 5),
  publicada boolean not null default false,
  actualizada_en timestamptz not null default now(),
  unique (evaluacion_id, estudiante_id)
);

create table public.historial_notas (
  id bigint generated always as identity primary key,
  calificacion_id uuid not null,
  operacion text not null check (operacion in ('INSERT','UPDATE','DELETE')),
  anterior jsonb,
  nuevo jsonb,
  modificado_por uuid,
  modificado_en timestamptz not null default now()
);

create index idx_cursos_docente on public.cursos(docente_id);
create index idx_matriculas_estudiante on public.matriculas(estudiante_id);
create index idx_evaluaciones_curso on public.evaluaciones(curso_id);
create index idx_calificaciones_estudiante on public.calificaciones(estudiante_id);
create index idx_calificaciones_evaluacion on public.calificaciones(evaluacion_id);
create index idx_historial_notas_calificacion on public.historial_notas(calificacion_id);

-- Función no expuesta por Data API; su propietario debe ser postgres (SQL Editor).
create function private.rol_actual()
returns text language sql stable security definer set search_path = ''
as $$
  select r.rol from public.roles_usuario r where r.usuario_id = (select auth.uid())
$$;

create function private.es_docente_del_curso(p_curso uuid)
returns boolean language sql stable security definer set search_path = ''
as $$
  select exists (
    select 1 from public.cursos c
    where c.id = p_curso and c.docente_id = (select auth.uid())
      and (select private.rol_actual()) in ('docente','administrador')
  )
$$;

create function private.esta_matriculado(p_curso uuid)
returns boolean language sql stable security definer set search_path = ''
as $$
  select exists (
    select 1 from public.matriculas m
    where m.curso_id = p_curso and m.estudiante_id = (select auth.uid())
  )
$$;

create function private.docente_de_evaluacion(p_evaluacion uuid)
returns boolean language sql stable security definer set search_path = ''
as $$
  select exists (
    select 1 from public.evaluaciones e
    where e.id = p_evaluacion and (select private.es_docente_del_curso(e.curso_id))
  )
$$;

-- Crear automáticamente un perfil y un rol SIN aceptar el rol desde user_metadata.
create function private.crear_perfil_al_registrar()
returns trigger language plpgsql security definer set search_path = ''
as $$
begin
  insert into public.perfiles(id, nombre) values (new.id, '');
  insert into public.roles_usuario(usuario_id, rol) values (new.id, 'estudiante');
  return new;
end;
$$;
create trigger trg_sae_crear_perfil
  after insert on auth.users
  for each row execute function private.crear_perfil_al_registrar();

-- Comprobar coherencia entre evaluación, curso, matrícula y rol del alumno.
create function private.validar_calificacion()
returns trigger language plpgsql security definer set search_path = ''
as $$
declare v_curso uuid;
begin
  select e.curso_id into v_curso from public.evaluaciones e where e.id = new.evaluacion_id;
  if v_curso is null or not exists (
    select 1 from public.matriculas m
    join public.roles_usuario r on r.usuario_id = m.estudiante_id
    where m.curso_id = v_curso and m.estudiante_id = new.estudiante_id and r.rol = 'estudiante'
  ) then
    raise exception 'La calificación requiere un estudiante matriculado en el curso';
  end if;
  new.actualizada_en := now();
  return new;
end;
$$;
create trigger trg_sae_validar_calificacion
  before insert or update on public.calificaciones
  for each row execute function private.validar_calificacion();

create function private.auditar_calificacion()
returns trigger language plpgsql security definer set search_path = ''
as $$
begin
  insert into public.historial_notas(calificacion_id,operacion,anterior,nuevo,modificado_por)
  values (
    coalesce(new.id,old.id), tg_op,
    case when tg_op in ('UPDATE','DELETE') then to_jsonb(old) else null end,
    case when tg_op in ('INSERT','UPDATE') then to_jsonb(new) else null end,
    auth.uid()
  );
  return coalesce(new,old);
end;
$$;
create trigger trg_sae_auditar_calificacion
  after insert or update or delete on public.calificaciones
  for each row execute function private.auditar_calificacion();

-- RLS y privilegios: las políticas no sustituyen a los GRANT/REVOKE.
alter table public.perfiles enable row level security;
alter table public.roles_usuario enable row level security;
alter table public.materias enable row level security;
alter table public.cursos enable row level security;
alter table public.matriculas enable row level security;
alter table public.evaluaciones enable row level security;
alter table public.calificaciones enable row level security;
alter table public.historial_notas enable row level security;

revoke all on public.perfiles, public.roles_usuario, public.materias, public.cursos,
  public.matriculas, public.evaluaciones, public.calificaciones, public.historial_notas
  from anon, authenticated;
grant select on public.perfiles, public.roles_usuario, public.materias, public.cursos,
  public.matriculas, public.evaluaciones, public.calificaciones to authenticated;
grant update(nombre, fecha_nacimiento, programa, semestre, codigo_estudiantil) on public.perfiles to authenticated;
grant insert, update, delete on public.cursos, public.evaluaciones, public.calificaciones to authenticated;
-- Materias y matrículas: solo se administran con SQL Editor/operaciones administrativas confiables.
-- Historial: ningún cliente puede escribir, actualizar ni borrar auditorías.

revoke all on function private.rol_actual(), private.es_docente_del_curso(uuid),
  private.esta_matriculado(uuid), private.docente_de_evaluacion(uuid) from public;
grant usage on schema private to authenticated;
grant execute on function private.rol_actual(), private.es_docente_del_curso(uuid),
  private.esta_matriculado(uuid), private.docente_de_evaluacion(uuid) to authenticated;

create policy perfiles_select on public.perfiles for select to authenticated
  using (id = (select auth.uid()) or (select private.rol_actual()) = 'administrador');
create policy perfiles_update on public.perfiles for update to authenticated
  using (id = (select auth.uid())) with check (id = (select auth.uid()));
create policy roles_select on public.roles_usuario for select to authenticated
  using (usuario_id = (select auth.uid()));
create policy materias_select on public.materias for select to authenticated using (true);

create policy cursos_select on public.cursos for select to authenticated
  using ((select private.es_docente_del_curso(id)) or (select private.esta_matriculado(id)) or (select private.rol_actual()) = 'administrador');
create policy cursos_insert on public.cursos for insert to authenticated
  with check (docente_id = (select auth.uid()) and (select private.rol_actual()) = 'docente');
create policy cursos_update on public.cursos for update to authenticated
  using ((select private.es_docente_del_curso(id)))
  with check (docente_id = (select auth.uid()) and (select private.rol_actual()) = 'docente');
create policy cursos_delete on public.cursos for delete to authenticated
  using ((select private.es_docente_del_curso(id)));

create policy matriculas_select on public.matriculas for select to authenticated
  using (estudiante_id = (select auth.uid()) or (select private.es_docente_del_curso(curso_id)));
create policy evaluaciones_select on public.evaluaciones for select to authenticated
  using ((select private.es_docente_del_curso(curso_id)) or (select private.esta_matriculado(curso_id)));
create policy evaluaciones_insert on public.evaluaciones for insert to authenticated
  with check ((select private.es_docente_del_curso(curso_id)));
create policy evaluaciones_update on public.evaluaciones for update to authenticated
  using ((select private.es_docente_del_curso(curso_id)))
  with check ((select private.es_docente_del_curso(curso_id)));
create policy evaluaciones_delete on public.evaluaciones for delete to authenticated
  using ((select private.es_docente_del_curso(curso_id)));

create policy calificaciones_select on public.calificaciones for select to authenticated
  using (
    (select private.docente_de_evaluacion(evaluacion_id))
    or (
      estudiante_id = (select auth.uid()) and publicada
      and exists (
        select 1 from public.evaluaciones e
        where e.id = evaluacion_id and (select private.esta_matriculado(e.curso_id))
      )
    )
  );
create policy calificaciones_insert on public.calificaciones for insert to authenticated
  with check ((select private.docente_de_evaluacion(evaluacion_id)));
create policy calificaciones_update on public.calificaciones for update to authenticated
  using ((select private.docente_de_evaluacion(evaluacion_id)))
  with check ((select private.docente_de_evaluacion(evaluacion_id)));
create policy calificaciones_delete on public.calificaciones for delete to authenticated
  using ((select private.docente_de_evaluacion(evaluacion_id)));

commit;
