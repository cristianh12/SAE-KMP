# SAE-KMP — Adaptación visual de SAE iOS

Referente consultado: capturas de pantalla del informe «Informe_SAE_iOS_Corregido_Segun_Guia.pdf». No constituye portación literal del código SwiftUI.

## Cambios
- Paleta morada sobre superficie clara, tarjetas y jerarquía visual como el prototipo iOS.
- Cinco secciones: Inicio, Materias, Calendario, Recursos y Perfil.
- Teléfono (< 840dp): navegación inferior, paneles en una columna.
- Desktop (>= 840dp): navegación lateral, calendario y agenda en paralelo.
- Calendario: celdas con altura controlada (38 o 46dp), sin `aspectRatio(1f)`, evitando deformaciones con ventanas grandes.
- Encabezados, detalles y controles navegables, estados vacíos en lugar de eventos o calificaciones inventados.

## Pendiente de integración por equipo
- Pantallas funcionales de materias, recursos, perfil y autenticación.
- Base de datos SQLite/SQLDelight para actividades, materias, notas y documentos.
- Sin persistencia: la selección de fecha se reinicia al cerrar; no se presenta como dato almacenado.

## Probar Desktop
`./gradlew :desktopApp:run` (Windows: `.\\gradlew.bat :desktopApp:run`)

Redimensionar la ventana, comprobar 800dp/900dp, navegación lateral/inferior, cambio de mes, día seleccionado y desplazamiento.

## Probar Android en teléfono físico
1. Activar Opciones de desarrollador y Depuración USB en Android.
2. Conectar cable de datos y aceptar permiso de depuración en el dispositivo.
3. Ejecutar `adb devices` (o `& "$env:LOCALAPPDATA\\Android\\Sdk\\platform-tools\\adb.exe" devices`).
4. Con el dispositivo seleccionado en Android Studio, ejecutar configuración `androidApp` (Run ▶), o instalar el APK debug con `adb install -r androidApp/build/outputs/apk/debug/androidApp-debug.apk` si esa ruta corresponde al build.
5. Comprobar diseño vertical, pestañas, calendarios de cinco/seis semanas, rotación y scroll.

Una compilación APK exitosa NO reemplaza la prueba en teléfono.
