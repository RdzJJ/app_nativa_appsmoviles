# Vita Habitos - Aplicación de Seguimiento de Hábitos

Una aplicación nativa de Android desarrollada en Kotlin para ayudar a los usuarios a construir y mantener buenos hábitos con una interfaz moderna y hermosa.

## 🎨 Características Principales

- ✅ **Sistema de Autenticación**: Login y registro de usuarios (preparado para Firebase)
- 🧑‍💼 **Perfil de Usuario**: Gestión de datos personales y visualización de estadísticas
- 📝 **Gestión de Hábitos**: Crear, editar y eliminar hábitos personalizados
- 📅 **Frecuencias Flexibles**: Diario, semanal, mensual o cada hora
- 🎯 **Seguimiento de Progreso**: Marcar hábitos como completados y ver estadísticas
- 📊 **Estadísticas Detalladas**: Racha actual, tasa de cumplimiento y resumen general
- ⏰ **Recordatorios**: Configurar horarios de notificación personalizados
- 🎨 **Interfaz Moderna**: Diseño hermoso con Material Design 3, Jetpack Compose y paleta de colores azul-verde

## 🎯 Paleta de Colores

- **Primary (Oscuro)**: `#19183B` - Púrpura oscuro
- **Secondary (Gris)**: `#708993` - Gris pizarra
- **Tertiary (Verde)**: `#A1C2BD` - Verde suave
- **Background (Claro)**: `#E7F2EF` - Cian muy claro

## 🔧 Tecnologías Utilizadas

- **Kotlin** - Lenguaje de programación principal
- **Jetpack Compose** - UI moderna y declarativa
- **Room Database** - Base de datos local para persistencia
- **Hilt** - Inyección de dependencias
- **Navigation Compose** - Navegación entre pantallas
- **Material Design 3** - Sistema de diseño
- **Coroutines & Flow** - Programación asíncrona
- **Firebase** - Preparado para autenticación y Firestore (próximamente)

## 📁 Estructura del Proyecto

```
app/src/main/java/com/habitsapp/
├── data/
│   ├── database/          # Room database y DAOs
│   ├── model/            # Entidades de datos (Habit, User)
│   └── repository/       # Repositorio de datos y autenticación
├── di/                   # Módulos de inyección de dependencias
├── ui/
│   ├── components/       # Componentes reutilizables
│   ├── navigation/       # Configuración de navegación
│   ├── screens/
│   │   ├── auth/        # Pantalla de login/signup
│   │   ├── habits/      # Pantalla de hábitos
│   │   ├── profile/     # Pantalla de perfil
│   │   ├── statistics/  # Pantalla de estadísticas
│   │   └── home/        # Pantalla contenedora principal
│   ├── theme/           # Temas y colores
│   └── viewmodel/       # ViewModels
└── HabitsApplication.kt  # Clase Application principal
```

## 🚀 Funcionalidades Principales

### 1. Autenticación

- Registro de nuevos usuarios
- Inicio de sesión con email y contraseña
- Validación de formularios
- Manejo seguro de errores
- Sistema dummy preparado para Firebase

### 2. Gestión de Hábitos

- Crear nuevos hábitos con nombre, descripción y frecuencia
- Editar hábitos existentes
- Eliminar hábitos con confirmación
- Configurar recordatorios personalizados

### 3. Seguimiento de Progreso

- Marcar hábitos como completados para el día actual
- Ver racha actual de cada hábito
- Calcular tasa de cumplimiento automáticamente
- Historial de completados

### 4. Perfil de Usuario

- Visualizar información personal
- Editar perfil (nombre, teléfono, biografía)
- Ver estadísticas personales:
  - Total de hábitos creados
  - Mejor racha alcanzada
  - Fecha de registro
- Cerrar sesión

### 5. Estadísticas

- Resumen general de todos los hábitos
- Total de hábitos activos
- Hábitos completados hoy
- Racha más larga alcanzada
- Tasa de cumplimiento promedio

### 6. Frecuencias Soportadas

- **Diario**: Cada 24 horas
- **Semanal**: Cada 7 días
- **Mensual**: Cada 30 días (aproximado)
- **Cada hora**: Cada 60 minutos

## 💻 Requisitos de Instalación

1. **Requisitos**:

   - Android Studio Hedgehog o superior
   - SDK mínimo: API 26 (Android 8.0)
   - SDK objetivo: API 35 (Android 15)
   - Java 17

2. **Configuración**:

   ```bash
   git clone [repository-url]
   cd app_nativa_appsmoviles
   ```

3. **Ejecutar**:
   - Abrir el proyecto en Android Studio
   - Sincronizar dependencias Gradle
   - Ejecutar en emulador o dispositivo físico

## 🏗️ Arquitectura

La aplicación sigue los principios de **Clean Architecture** y **MVVM**:

- **Presentation Layer**: Jetpack Compose UI + ViewModels
- **Domain Layer**: Casos de uso y lógica de negocio
- **Data Layer**: Repository pattern con Room database y autenticación

## 🗄️ Base de Datos

Utiliza Room para persistencia local con las siguientes entidades:

- **Habit**: Información del hábito (nombre, descripción, frecuencia, etc.)
- **HabitCompletion**: Registro de completados por fecha
- **User**: Información del usuario (email, nombre, teléfono, bio, etc.)

## 🔐 Sistema de Autenticación (Dummy)

Actualmente implementado como dummy (simulado). Para integrar Firebase:

1. Reemplazar `AuthRepository.kt` con llamadas a Firebase Auth
2. Actualizar los modelos si es necesario
3. La UI, ViewModel y navegación funcionarán igual

## 📋 Cambios Recientes (v2.0)

- ✅ Renombrado de HabitsApp a **Vita Habitos**
- ✅ Nueva paleta de colores (azul a verde)
- ✅ Sistema de login y registro
- ✅ Pantalla de perfil con estadísticas
- ✅ Mejora general de UX/UI
- ✅ Traducción completa al español
- ✅ Añadido .gitignore completo

## 🚀 Próximas Características

- [ ] Integración con Firebase Authentication
- [ ] Sincronización en la nube con Firestore
- [ ] Sistema de notificaciones push
- [ ] Gráficos de progreso más detallados
- [ ] Exportar datos
- [ ] Temas personalizables
- [ ] Widgets para pantalla de inicio
- [ ] Sistema de amigos y desafíos grupales
- [ ] Insignias y logros

## 📝 Lenguaje

La aplicación está completamente en **español** incluyendo:

- Interfaz de usuario
- Mensajes de validación
- Mensajes de error
- Ayuda y documentación

## 📞 Contacto

Desarrollado como proyecto académico para el curso de Aplicaciones Móviles.

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo LICENSE para detalles.
