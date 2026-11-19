# Pruebas manuales de etapa 1

En la raíz del proyecto, dentro de PowerShell:

```powershell
git branch --show-current
.\gradlew.bat --stop
.\gradlew.bat :shared:compileKotlinJvm --rerun-tasks
.\gradlew.bat :desktopApp:run
```

Si Desktop inicia correctamente, comprobar:
- [ ] Bienvenida muestra el logo oficial SAE.
- [ ] «Ingresar a SAE» abre el Dashboard.
- [ ] «Acerca de SAE» abre la descripción y los créditos.
- [ ] «Volver al panel» regresa al Dashboard.
- [ ] «Salir» vuelve a la bienvenida.

Validación Android desde Android Studio: seleccionar `androidApp` y un emulador.
O compilar con:

```powershell
.\gradlew.bat :androidApp:assembleDebug
```

- [ ] La aplicación se abre en Android y muestra el mismo logo.
- [ ] Se completa el mismo recorrido de navegación.
- [ ] Se adjuntan capturas de Android y Desktop al informe.

**Nota:** Si el error de `Res` persiste tras copiar la corrección, verificar
`shared/build.gradle.kts`, la ubicación de `sae_logo.png`, sincronizar Gradle y
reconstruir. No copiar carpetas `.git` dentro de otras carpetas del proyecto.
