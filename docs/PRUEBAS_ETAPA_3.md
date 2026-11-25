

\# Pruebas de la etapa 3 — SAE-KMP



Responsable: Juan Guerrero



Rama: feature/juanmi



\## Objetivo



Verificar la integración de la biblioteca tecnológica

y el módulo de bienestar estudiantil en Android y Desktop.



\## 1. Pruebas de compilación



Fecha: 24 de noviembre de 2026.



| Módulo | Resultado | Tiempo |

|---|---|---|

| shared:compileKotlinJvm | BUILD SUCCESSFUL | 26 segundos |

| androidApp:assembleDebug | BUILD SUCCESSFUL | 31 segundos |

| desktopApp:compileKotlin | BUILD SUCCESSFUL | 14 segundos |



Se presentó una advertencia relacionada con las versiones

XML del Android SDK, sin impedir la compilación.



\## 2. Pruebas de la biblioteca



\- \[ ] Acceder desde Recursos.

\- \[ ] Buscar libros mediante Open Library.

\- \[ ] Visualizar títulos y autores.

\- \[ ] Visualizar los años de publicación.

\- \[ ] Comprobar la carga de portadas.

\- \[ ] Abrir la ficha original de un libro.

\- \[ ] Guardar libros favoritos.

\- \[ ] Consultar los favoritos.

\- \[ ] Eliminar un libro favorito.

\- \[ ] Cerrar y volver a abrir SAE.

\- \[ ] Confirmar la persistencia de los favoritos.

\- \[ ] Verificar el comportamiento sin Internet.



\## 3. Pruebas de bienestar



\- \[ ] Acceder desde el Dashboard.

\- \[ ] Seleccionar el estado de ánimo.

\- \[ ] Seleccionar el nivel de estrés.

\- \[ ] Registrar las horas de sueño.

\- \[ ] Seleccionar la carga académica.

\- \[ ] Guardar una encuesta.

\- \[ ] Consultar las recomendaciones.

\- \[ ] Consultar el historial.

\- \[ ] Eliminar un registro individual.

\- \[ ] Eliminar el historial completo.

\- \[ ] Confirmar persistencia después de reiniciar.

\- \[ ] Regresar al Dashboard.



\## 4. Validación multiplataforma



| Prueba | Android | Desktop |

|---|---|---|

| Compilación | Exitosa | Exitosa |

| Inicio de sesión | Por confirmar | Por confirmar |

| Biblioteca | Por confirmar | Por confirmar |

| Portadas | Por confirmar | Por confirmar |

| Favoritos | Por confirmar | Por confirmar |

| Bienestar | Por confirmar | Por confirmar |

| Historial persistente | Por confirmar | Por confirmar |

| Separación de usuarios | Por confirmar | Por confirmar |

| Navegación | Por confirmar | Por confirmar |



\## 5. Seguridad y privacidad



Se verificó que SupabaseLocalConfig.kt está excluido

mediante la línea 20 del archivo .gitignore.



El comando git ls-files no mostró dicho archivo,

confirmando que no está rastreado por Git.



Los favoritos y registros de bienestar utilizan claves

separadas por identificador de usuario.



La separación de datos entre cuentas debe comprobarse

mediante pruebas funcionales.



El almacenamiento local no está cifrado por SAE.



\## 6. Conclusión



Se implementaron los módulos de biblioteca tecnológica

y bienestar estudiantil, integrándolos con la navegación

compartida del proyecto.



Las compilaciones de Shared, Android y Desktop finalizaron

correctamente.



Queda pendiente completar y registrar los resultados de

las pruebas funcionales antes de considerar validada

la totalidad de las funcionalidades.



