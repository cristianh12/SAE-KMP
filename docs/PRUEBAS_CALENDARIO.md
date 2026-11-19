# Pruebas manuales — Calendario SAE

1. Ejecutar `:shared:compileKotlinJvm` y `:desktopApp:run`.
2. Ingresar a SAE y pulsar **Abrir calendario**.
3. Comprobar mes/año actuales según el dispositivo, días ordenados de lunes a domingo, y día actual resaltado.
4. Pulsar mes anterior y siguiente, incluyendo diciembre ↔ enero.
5. Seleccionar un día y comprobar fecha mostrada abajo.
6. Pulsar **Ir a hoy** para regresar al mes y fecha actuales.
7. Comprobar mensaje de ausencia de eventos: no se generan actividades falsas.
8. Volver al Dashboard y comprobar navegación.
9. Repetir en Android con `:androidApp:assembleDebug` e instalación/ejecución.
10. Ejecutar `:shared:jvmTest` para probar bisiestos, encabezado semanal y cambio de año.

**Alcance**: la vista de calendario no guarda eventos. La persistencia se implementará en la etapa 2 con repositorios compartidos.
