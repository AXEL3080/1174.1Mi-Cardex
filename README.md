<div align="center">

  # 📱 Mi Cardex - Android App
  
  ![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
  ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
  ![Android Studio](https://img.shields.io/badge/Android%20Studio-Koala-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white)
  ![Room DB](https://img.shields.io/badge/Room-Database-4285F4?style=for-the-badge&logo=sqlite&logoColor=white)

  <p align="center">
    <b>Aplicación móvil nativa para la gestión y control reactivo de calificaciones y materias universitarias.</b>
    <br />
    <i>Desarrollada para la asignatura de Programación de Dispositivos Móviles para Android</i>
  </p>

  <img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="600" alt="Android Dev Banner">

</div>

---

## Características Principales

- **Cálculo Automático**: Obtención en tiempo real del promedio general y total de créditos cursados.
- **Persistencia Local**: Integración con **Room Database** para almacenar registros offline de manera segura.
- **UI Moderna**: Diseñada al 100% con **Jetpack Compose** y Material Design 3.
- **Arquitectura MVVM**: Separación limpia de responsabilidades mediante `Repository`, `ViewModel` y flujos de datos asíncronos (`Flow` / `StateFlow`).

---

## Tech Stack & Herramientas

<div align="left">

| Categoría | Tecnología |
| :--- | :--- |
| **Lenguaje** | `Kotlin` |
| **Interfaz (UI)** | `Jetpack Compose` & `Material 3` |
| **Base de Datos** | `Room Persistence Library` (SQLite) |
| **Arquitectura** | `MVVM` (Model-View-ViewModel) + `Repository Pattern` |
| **Async & Streams** | `Kotlin Coroutines` & `Flow` |

</div>

---

## Arquitectura de la Base de Datos

El módulo de base de datos implementa las anotaciones y bloques estructurales fundamentales de Room:

```mermaid
graph TD
    UI[ CardexScreen - UI] -->|Observa StateFlow| VM[ MateriaViewModel]
    VM -->|Llama métodos| REPO[ MateriaRepository]
    REPO -->|Consulta / Actualiza| DAO[ MateriaDao - @Dao]
    DAO -->|Persiste datos| DB[( AppDatabase & Materia - @Entity)]

    style UI fill:#1E88E5,stroke:#fff,stroke-width:2px,color:#fff
    style VM fill:#43A047,stroke:#fff,stroke-width:2px,color:#fff
    style REPO fill:#FB8C00,stroke:#fff,stroke-width:2px,color:#fff
    style DAO fill:#8E24AA,stroke:#fff,stroke-width:2px,color:#fff
    style DB fill:#00ACC1,stroke:#fff,stroke-width:2px,color:#fff
```

<details>
<summary><b>Ver detalle de los componentes de Room</b></summary>

- **`@Entity`**: Define la tabla `materias` con sus campos (`id`, `clave`, `nombre`, `creditos`, `calificacion`, `periodo`).
- **`@Dao`**: Métodos de acceso como `@Query`, `@Insert`, `@Update` y `@Delete`.
- **`@Database`**: Instancia Singleton que extiende de `RoomDatabase`.
- **`Repository`**: Aísla las fuentes de datos para el ViewModel.
- **`ViewModel`**: Sostiene los estados de la UI sobreviviendo a cambios de configuración.

</details>

---

## Cómo ejecutar el proyecto

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/AXEL3080/1174.1Mi-Cardex.git](https://github.com/AXEL3080/1174.1Mi-Cardex.git)
Abrir en Android Studio:

Selecciona Open an existing project.

Navega hasta la carpeta clonada.

Sincronizar Gradle & Ejecutar:

Deja que Gradle descargue las dependencias.

Selecciona un emulador o dispositivo físico con Android 8.0 (API 26) o superior y presiona Run (Shift + F10).

👤 Autor
Axel Gerardo Ramos

Ingeniería en Sistemas Computacionales

Tecnológico Nacional de México - Campus Tuxtla Gutiérrez

<div align="center">
  <img src="https://media.giphy.com/media/puiUgSNbuLhbj0irE0/giphy.gif" width="300" alt="Divertido GIF">
</div>
