## Análisis del proyecto — ProyectoBase

Fecha del análisis: 2025-11-10

Este documento resume y describe las features, la estructura y las partes clave del proyecto Android localizado en la carpeta `ProyectoBase`.

---

## Resumen rápido

- Nombre de la app: `ProyectoBase`
## Análisis del proyecto — ProyectoBase

Fecha del análisis: 2025-11-10

Este documento resume y describe las features, la estructura y las partes clave del proyecto Android localizado en la carpeta `ProyectoBase`.

---

## Resumen rápido

- Nombre de la app: `ProyectoBase`
- Lenguaje: Kotlin
- Tipo: Aplicación Android (módulo `app` dentro de `ProyectoBase`)
- Propósito aparente: proyecto educativo/demo con pantallas de ejemplo (login local, operaciones aritméticas, conversor de moneda y comunicación simple con un servicio web para insertar/consultar alumnos).

Se recomienda abrir el sub-proyecto `ProyectoBase` en Android Studio (el README del repo lo indica explícitamente).

---

## Features principales (detalle por feature)

1) Inicio de sesión local (demo)
   - Archivo principal: `app/src/main/java/com/example/proyectobase/MainActivity.kt`
   - UI: `app/src/main/res/layout/activity_main.xml`
   - Comportamiento: solicita `usuario` y `contraseña` en dos `EditText`, compara contra credenciales hardcodeadas (`ch.munozs` / `admin123`) y, si coinciden, abre `MainActivity8` enviando extras `sesion` y `par_contrasena`.
   - Observaciones: credenciales en código. No es seguro; solo para demo.

2) Navegación y pantallas de ejemplo
   - Activities: `MainActivity`, `MainActivity2`, `MainActivity3`, `MainActivity4`, `MainActivity5`, `MainActivity6`, `MainActivity7`, `MainActivity8`.
   - Cada activity tiene su layout en `app/src/main/res/layout/activity_main*.xml`.
   - Uso consistente de edge-to-edge: cada Activity aplica `ViewCompat.setOnApplyWindowInsetsListener(..., WindowInsetsCompat.Type.systemBars())` para ajustar padding.

3) Calculadora / Operaciones matemáticas
   - Código: `app/src/main/java/com/example/proyectobase/funciones/OpMatematicas.kt`
   - UI: `activity_main4.xml` y lógica en `MainActivity4.kt`.
   - Observaciones: hay un bug donde la opción seleccionada del `Spinner` se lee una sola vez en `onCreate` (variable `str_op_selected`) en lugar de leerla dentro del evento `onClick` — se debe corregir para respetar la selección actual.

4) Conversor Pesos -> USD
   - Código: `app/src/main/java/com/example/proyectobase/usdpesos/Conversor.kt`
   - UI: `MainActivity5.kt` con `activity_main5.xml`.
   - Observaciones: tipo de cambio hardcodeado (900). Manejo de excepciones/confusión en `finally` que imprime siempre "error: division en cero".

5) Menú/ListView demo
   - `MainActivity7` muestra un `ListView` con opciones y muestra `Toast` al seleccionar.

6) Envío de formulario a API (insertar alumno)
   - UI: `MainActivity8.kt` (formulario nombre/apellido/grupo/seccion, botones Guardar y Tomar foto)
   - Lógica de inserción: `app/src/main/java/com/example/proyectobase/funciones/InsertarAlumnosAPI.kt` (usa `lifecycleScope.launch` y `AlumnosRepository`)
   - Cliente HTTP: `app/src/main/java/com/example/proyectobase/API/ApiDuocClient.kt` (Retrofit + Moshi + OkHttp con `HttpLoggingInterceptor`)
   - Servicio: `app/src/main/java/com/example/proyectobase/API/ApiDuocService.kt` (GET `apiduoc/consulta.php`, POST `apiduoc/insert.php`)
   - Repositorio: `app/src/main/java/com/example/proyectobase/API/AlumnosRepository.kt` (usa `withContext(Dispatchers.IO)` y envuelve en `Result`)
   - Observaciones: `MainActivity8` pasa callbacks `onSuccess`/`onError` que crean `Toast` pero no siempre llaman `.show()`; sin embargo `InsertarAlumnosAPI` ya muestra Toasts. Falta validación de campos y feedback (progress) durante la operación.

---

## Estructura de archivos y paquetes (mapa rápido)

- `ProyectoBase/`
  - `build.gradle.kts` (top-level plugin config)
  - `gradle/libs.versions.toml` (centraliza versiones: AGP 8.8.0, Kotlin 1.9.24, etc.)
  - `app/`
    - `build.gradle.kts` (config del módulo app: compileSdk 35, minSdk 24, dependencias: core-ktx, appcompat, material, retrofit, moshi, okhttp, coroutines, lifecycle)
    - `src/main/AndroidManifest.xml` (declara permisos `INTERNET`, `ACCESS_NETWORK_STATE`, y las Activities)
    - `src/main/java/com/example/proyectobase/` (código Kotlin)
      - `MainActivity*.kt` (pantallas)
      - `funciones/InsertarAlumnosAPI.kt`, `OpMatematicas.kt`
      - `API/ApiDuocService.kt`, `ApiDuocClient.kt`, `AlumnosRepository.kt`, `Alumno.kt`
      - `usdpesos/Conversor.kt`
    - `src/main/res/` (layouts `activity_main*.xml`, `values/strings.xml`, `values/themes.xml`, `xml/data_extraction_rules.xml`, `xml/backup_rules.xml`)

---

## Dependencias y versiones clave

- AGP: 8.8.0 (en `gradle/libs.versions.toml`)
- Kotlin: 1.9.24
- AndroidX: core-ktx 1.10.1, appcompat 1.6.1, material 1.10.0, activity 1.8.0, constraintlayout 2.1.4
- Retrofit: 2.11.0
- Moshi: 1.15.1 (con `KotlinJsonAdapterFactory`)
- OkHttp Logging Interceptor: 4.12.0
- Coroutines Android: 1.8.1
- Lifecycle KTX: 2.8.4

Estas versiones están centralizadas en `gradle/libs.versions.toml` y referenciadas por `build.gradle.kts`.

---

## Permisos y manifest

- `AndroidManifest.xml` declara:
  - `<uses-permission android:name="android.permission.INTERNET" />`
  - `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
- Activities listadas, con `MainActivity` como LAUNCHER.

---

## Observaciones técnicas y bugs detectados (prioridad alta/mediana)

1. Credenciales hardcodeadas en `MainActivity.kt` — prioridad ALTA (seguridad / demo only).
2. `MainActivity4`: lectura del `Spinner` fuera del `onClick` — BUG funcional (no respeta la selección real). — prioridad MEDIA.
3. `OpMatematicas.dividir` no comprueba división por cero — puede lanzar excepción. — prioridad ALTA.
4. `Conversor.convertir_usd_string` imprime mensaje de error en `finally` siempre — confuso. — prioridad BAJA.
5. `MainActivity8` callbacks muestran Toasts sin llamar `.show()` en alguna ruta — UX inconsistente. — prioridad BAJA.
6. `HttpLoggingInterceptor` con nivel BODY está activo sin condicionar por build type — no deseable en release. — prioridad MEDIA.
7. Falta validación de formulario y feedback de carga (ProgressBar) en operaciones de red — prioridad MEDIA.

---

## Recomendaciones y mejoras (acciones sugeridas)

Prioritarias:
- Remover credenciales hardcodeadas. Si es demo, mover a `BuildConfig.DEBUG` o a un recurso de test.
- Añadir comprobación de división por cero en `OpMatematicas.dividir` y validar entrada numérica.
- Corregir lectura del `Spinner` dentro del `btnCalcular.setOnClickListener { ... }` en `MainActivity4`.
- Añadir validaciones (campos vacíos) y `Toast`/`Snackbar` antes de enviar a la API.

Medianas:
- Añadir `ProgressBar` o `Dialog` durante llamadas a `AlumnosRepository` para mejorar UX.
- Condicionar el `HttpLoggingInterceptor` para que solo esté activo en `debug` builds.
- Extraer constantes (tipo de cambio, baseUrl) a `object Config` o `BuildConfig`.

Refactor y limpieza:
- Extraer un `BaseActivity` que centralice el ajuste de `WindowInsets` y evitar duplicación.
- Renombrar `MainActivity2/3/4/...` a nombres semánticos (CalculatorActivity, ConverterActivity, FormActivity, etc.).
- Añadir pruebas unitarias para `OpMatematicas` y `Conversor`.

---

## Comandos rápidos para compilar (Windows - cmd.exe)

Abre una terminal en la carpeta `ProyectoBase` y ejecuta:

```cmd
cd "c:\Users\mirko\Desktop\Desarrollo de Aplicaciones moviles\android_duoc_pao-main\android_duoc_pao-main\ProyectoBase"
gradlew.bat assembleDebug
```

O usa Android Studio: "Open an existing project" -> selecciona la carpeta `ProyectoBase`.

---

## Cambios rápidos que puedo aplicar ahora (elige uno si quieres que lo implemente):

- Corregir `MainActivity4` para leer el `Spinner` dentro del `onClick`.
- Añadir chequeo de división por cero en `OpMatematicas.dividir`.
- Añadir `.show()` en los `Toast` faltantes.
- Extraer `Config` con `BASE_URL` y `TIPO_CAMBIO`.

---

## Patrones de diseño y estilo de código observados

Resumen: el proyecto sigue un estilo sencillo y didáctico, con separación básica de responsabilidades pero sin una arquitectura formal estricta (por ejemplo, no usa ViewModel/DI de forma consistente). Aun así se identifican patrones y buenas prácticas parciales que pueden aprovecharse y formalizarse para proyectos productivos.

- Singleton: `ApiDuocClient` y objetos utilitarios (`Conversor`, `OpMatematicas`, `MiPrimerObjeto`) están implementados como `object` singletons, lo que facilita el acceso global a instancias compartidas.
- Repository: `AlumnosRepository` implementa el patrón Repository (encapsula llamadas a la capa de red y ofrece una API única para la capa superior). Buen diseño para desacoplar la fuente de datos.
- Service / Adapter: `ApiDuocService` es la interfaz Retrofit (contrato de red) — patrón Adapter/Service claro entre la aplicación y el API REST.
- Wrapper asíncrono y lifecycle-aware: `InsertarAlumnosAPI` actúa como capa intermedia que usa `owner.lifecycleScope.launch` para ejecutar corutinas respetando el ciclo de vida de la Activity; esto ayuda a evitar fugas y simplifica la interacción UI->Repo.
- Data classes DTO: `Alumno`, `AlumnoInsertRequest`, `InsertResponse` usan data classes Kotlin para modelado de datos/DTOs — adecuado y idiomático.

Estilo de código y convención:
- Paquetes organizados por funcionalidad: `API`, `funciones`, `usdpesos` — buena separación a nivel de paquete.
- Activities como controladores de UI: cada Activity gestiona su propio layout y lógica de eventos (pattern MVP-lite / controlador UI). Falta ViewModel para separar lógica de negocio de la UI.
- Uso directo de `Intent` y `Extras` para paso de datos entre Activities — sencillo y apropiado para apps pequeñas.

Limitaciones actuales y recomendaciones de patrón objetivo (para producción):
- Migrar hacia MVVM: introducir `ViewModel` por pantalla (o por flujo) para mantener la lógica de UI fuera de las Activities. Esto facilita tests unitarios y manejo de estado (LiveData/StateFlow).
- Inyección de dependencias: usar Hilt (o Koin) para inyectar `ApiDuocClient`, `AlumnosRepository`, y otras dependencias; facilita pruebas y configuración por build types.
- Separar casos de uso (UseCases/Interactors): encapsular la lógica de negocio (p. ej. validación de formulario, transformaciones) en clases de UseCase para mejorar testabilidad y reuso.
- Manejo robusto de errores: implementar un `NetworkResult` / `Resource` sealed class (Loading / Success / Error) para comunicar estado al UI y mostrar loaders / errores.
- Estrategia de almacenamiento: si la app necesita datos offline (p. ej. recetas), añadir fuente local (Room) y aplicar patrón Repository con fuentes remota/local.

---

## Prompt final (para IA) — Cómo desarrollar apps Android de este tipo (uso: complementar contexto para crear una app de recetas)

A continuación hay un prompt listo para usar por otra IA (o por ti) como contexto / contrato para generar código, estructura y artefactos para una aplicación Android similar. Está diseñado para que la IA comprenda el estilo, patrones y requisitos técnicos deseados y produzca código consistente con este proyecto base.

USO: colocar este prompt como "contexto" cuando pidas a la IA generar pantallas, endpoints o un esqueleto de proyecto para una app de recetas.

--- PROMPT PARA IA ---

Contexto:
- Basarse en un proyecto Android Kotlin sencillo llamado "ProyectoBase" con patrón Repository, Retrofit+Moshi, coroutines y uso de `object` para singletons.
- Objetivo: generar una aplicación Android de recetas que siga las mismas convenciones y estilos de este repositorio. Mantener compatibilidad con `minSdk 24` y `compileSdk 35`.

Requisitos de arquitectura (obligatorio):
1. Usar arquitectura MVVM: Activities/Fragments deben delegar lógica a ViewModels (AndroidX ViewModel + StateFlow o LiveData).
2. Inyección de dependencias: usar Hilt para proporcionar `Retrofit`, `OkHttpClient`, `Repository` y `ViewModel`.
3. Patrón Repository: crear `RecipesRepository` que proporcione métodos `getRecipes()`, `getRecipe(id)`, `createRecipe(request)`, `updateRecipe()` y `deleteRecipe()` y combine fuentes remota (Retrofit) y local (Room) si se solicita soporte offline.
4. Networking: usar Retrofit con Moshi. Configurar `OkHttpClient` con logging solo en `debug` y con timeouts razonables. Base URL configurable vía `BuildConfig`.
5. Concurrency: usar Kotlin Coroutines y `Dispatchers.IO` para operaciones de red/BD; exponer resultados a la UI con `StateFlow` o `LiveData` dentro del ViewModel.

Requisitos de código y estilo:
- Mantener paquetes por funcionalidad: `ui.recipes`, `ui.details`, `data.api`, `data.local`, `data.repository`, `domain` (usecases), `util`.
- Usar data classes para modelos y DTOs. Mantener nombres claros: `Recipe`, `RecipeRequest`, `RecipeResponse`.
- Crear objetos singletons `ApiClient` o `NetworkModule` provistos por Hilt.
- Añadir validaciones de formulario y manejo de errores con mensajería amigable al usuario (Snackbars/Dialogs).

UI y UX (mínimo):
- Pantalla principal: lista de recetas (`RecyclerView`) con título, imagen en miniatura y tiempo de preparación.
- Pantalla detalle: imagen grande, ingredientes, pasos, botones para editar / eliminar / compartir.
- Pantalla crear/editar receta: formulario con campos: nombre, descripción, ingredientes (lista dinámica), pasos (lista dinámica), categorías, tiempo, porciones y foto (tomar o seleccionar de galería).
- Soportar subida de imagen: convertir a Base64 o multipart/form-data según API.

API y contrato esperado:
- Endpoints (ejemplo REST):
  - GET /recipes -> List<Recipe>
  - GET /recipes/{id} -> Recipe
  - POST /recipes -> CreateResponse
  - PUT /recipes/{id} -> UpdateResponse
  - DELETE /recipes/{id} -> DeleteResponse
- Si el backend usa JSON, usar Moshi con `KotlinJsonAdapterFactory`.

Tests y calidad:
- Añadir unit tests para ViewModel, UseCases y utilidades (p. ej. conversores). Usar JUnit + MockK o Mockito.
- Añadir pruebas instrumentadas mínimas (opcional) para flujo crítico.
- Integrar ktlint/detekt y configurar un GitHub Action que ejecute `./gradlew build` y `./gradlew ktlintCheck`.

Build y configuración:
- Mantener versiones en `gradle/libs.versions.toml`.
- Configurar `buildTypes` (debug/release) y usar `BuildConfig` para valores sensibles (no almacenar secretos en repositorio).

Producción y seguridad:
- No hardcodear credenciales ni claves. Usar `gradle.properties` o servicios de CI/CD para variables privadas.
- En release, desactivar logging de cuerpo HTTP y habilitar minify/proguard con reglas prudentes.

Salida esperada por la IA cuando se le pida "Genera el esqueleto de la app de recetas":
1. Estructura de carpetas y archivos (lista).  
2. Código base para: `RecipesRepository`, `ApiService`, `ApiClient`, `RecipeViewModel`, `RecipeListFragment` (o Activity), `RecipeDetailFragment`, `CreateEditRecipeFragment`, y los layouts XML correspondientes.
3. Módulo Hilt (`NetworkModule`, `DatabaseModule`) y ejemplos de binding para ViewModel.
4. Unit tests skeleton para ViewModel y Repository.
5. README.md con instrucciones de build y endpoints esperados.

Notas finales:
- Este prompt se debe usar como contexto adicional junto al contenido del repositorio `ProyectoBase`. La IA debe respetar la organización de paquetes, las convenciones de nombres y el uso de corutinas/Retrofit/Moshi.
- Uso específico: el objetivo final es producir una app Android de recetas coherente con este estilo; los endpoints y el esquema de datos pueden adaptarse según el backend, pero la estructura y los patrones arquitectónicos deben mantenerse.

--- FIN DEL PROMPT ---

---

He añadido estas secciones al documento `ANALISIS_DE_PROYECTO.md`.
