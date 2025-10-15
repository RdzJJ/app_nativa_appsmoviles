# HabitsApp - Aplicación de Buenos Hábitos

Una aplicación nativa de Android desarrollada en Kotlin para ayudar a los usuarios a construir y mantener buenos hábitos.

## Características

- ✅ **Gestión de Hábitos**: Crear, editar y eliminar hábitos personalizados
- 📅 **Frecuencias Flexibles**: Diario, semanal, mensual o cada hora
- 🎯 **Seguimiento de Progreso**: Marcar hábitos como completados y ver estadísticas
- 📊 **Estadísticas Detalladas**: Racha actual, tasa de cumplimiento y resumen general
- ⏰ **Recordatorios**: Configurar horarios de notificación personalizados
- 🎨 **Interfaz Moderna**: Diseño con Material Design 3 y Jetpack Compose

## Tecnologías Utilizadas

- **Kotlin** - Lenguaje de programación principal
- **Jetpack Compose** - UI moderna y declarativa
- **Room Database** - Base de datos local para persistencia
- **Hilt** - Inyección de dependencias
- **Navigation Compose** - Navegación entre pantallas
- **Material Design 3** - Sistema de diseño
- **Coroutines & Flow** - Programación asíncrona

## Estructura del Proyecto

```
app/src/main/java/com/habitsapp/
├── data/
│   ├── database/          # Room database y DAOs
│   ├── model/            # Entidades de datos
│   └── repository/       # Repositorio de datos
├── di/                   # Módulos de inyección de dependencias
├── ui/
│   ├── components/       # Componentes reutilizables
│   ├── navigation/       # Configuración de navegación
│   ├── screens/          # Pantallas de la aplicación
│   ├── theme/           # Temas y colores
│   └── viewmodel/       # ViewModels
└── HabitsApplication.kt  # Clase Application principal
```

## Funcionalidades Principales

### 1. Gestión de Hábitos
- Crear nuevos hábitos con nombre, descripción y frecuencia
- Editar hábitos existentes
- Eliminar hábitos con confirmación
- Configurar recordatorios personalizados

### 2. Seguimiento de Progreso
- Marcar hábitos como completados para el día actual
- Ver racha actual de cada hábito
- Calcular tasa de cumplimiento automáticamente
- Historial de completados

### 3. Estadísticas
- Resumen general de todos los hábitos
- Total de hábitos activos
- Hábitos completados hoy
- Racha más larga alcanzada
- Tasa de cumplimiento promedio

### 4. Frecuencias Soportadas
- **Diario**: Cada 24 horas
- **Semanal**: Cada 7 días
- **Mensual**: Cada 30 días (aproximado)
- **Cada hora**: Cada 60 minutos

## Instalación y Configuración

1. **Requisitos**:
   - Android Studio Hedgehog o superior
   - SDK mínimo: API 24 (Android 7.0)
   - SDK objetivo: API 34 (Android 14)

2. **Configuración**:
   ```bash
   git clone [repository-url]
   cd app_nativa_appsmoviles
   ```

3. **Ejecutar**:
   - Abrir el proyecto en Android Studio
   - Sincronizar dependencias Gradle
   - Ejecutar en emulador o dispositivo físico

## Arquitectura

La aplicación sigue los principios de **Clean Architecture** y **MVVM**:

- **Presentation Layer**: Jetpack Compose UI + ViewModels
- **Domain Layer**: Casos de uso y lógica de negocio
- **Data Layer**: Repository pattern con Room database

## Base de Datos

Utiliza Room para persistencia local con las siguientes entidades:

- **Habit**: Información del hábito (nombre, descripción, frecuencia, etc.)
- **HabitCompletion**: Registro de completados por fecha
- **HabitWithCompletions**: Relación entre hábito y sus completados

## Próximas Características

- [ ] Sistema de notificaciones push
- [ ] Gráficos de progreso más detallados
- [ ] Exportar datos
- [ ] Temas personalizables
- [ ] Widgets para pantalla de inicio
- [ ] Sincronización en la nube

## Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## Contacto

Desarrollado como proyecto académico para el curso de Aplicaciones Móviles.
