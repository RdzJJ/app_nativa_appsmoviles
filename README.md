#  VITA

**VITA** es una aplicación móvil enfocada en la creación y seguimiento de hábitos saludables.  
Permite a los usuarios registrar sus rutinas, consultar estadísticas de progreso y administrar su perfil de forma simple e intuitiva.


## 🚀 Funcionalidades principales

- **Inicio de sesión y registro de usuario**
  - Autenticación con correo y contraseña.
- **Gestión de hábitos**
  - Crear, visualizar y eliminar hábitos personalizados.
  - Definir frecuencia: diaria, semanal, mensual o por hora.
- **Estadísticas**
  - Visualización del progreso mediante gráficos de barras y circulares.
- **Perfil de usuario**
  - Consulta de nombre, apellido y correo registrado.

  ## 🧩 Arquitectura general

- **Frontend:** Kotlin (Android)
- **Backend:** Firebase Authentication y Firebase Realtime Database
- **Diseño:** Prototipado en Figma 
- **Integración:** Sincronización en tiempo real entre vistas y datos del usuario

## 📱 Estructura de pantallas

1. **Login:** Acceso de usuarios registrados.  
2. **Sign Up:** Registro de nuevos usuarios.  
3. **Inicio:** Vista principal con hábitos creados.  
4. **Crear hábito:** Formulario para añadir un nuevo hábito.  
5. **Estadísticas:** Visualización del progreso en gráficos.  
6. **Perfil:** Información básica del usuario.

---

## 🧪 TRABAJO FINAL: AUTOMATIZACIÓN DE PRUEBAS CON IA

### 📚 Entregable Completado (Noviembre 2025)

Este proyecto incluye un **trabajo final académico** sobre "Diseño y Automatización de Pruebas a partir de Historias de Usuario con IA".

### ✅ ¿QUÉ SE ENTREGÓ?

- **5 Historias de Usuario** en formato INVEST con criterios de aceptación Gherkin
- **7 Casos de Prueba Manual** documentados profesionalmente (TC001-TC007)
- **3 Prompts de IA** documentados + Análisis Crítico de respuestas
- **13 Tests Automatizados** con Espresso (6 unitarios + 7 E2E)
- **~3,000 líneas** de documentación y código de pruebas
- **Guías completas** de ejecución y troubleshooting

### 📋 ESTRUCTURA DE ENTREGABLES

```
/docs/                                    (9 documentos profesionales)
├── README.md                            ⭐ COMIENZA AQUÍ
├── GUIA_VISUAL_RAPIDA.md                (Mapas y diagramas)
├── TABLAS_REFERENCIA_RAPIDA.md          (15 tablas de referencia)
├── README_AUTOMATIZACION.md              (Resumen ejecutivo completo)
├── INDICE_ENTREGABLES.md                 (Matriz de cobertura)
├── TC001_TC007_CasosPruebaLogin.md       (7 Casos Manual)
├── HISTORIAS_USUARIO_HU002_HU005.md      (HU002-HU005)
├── PROMPTS_Y_REVISIONES.md               (3 Prompts IA + Análisis)
└── GUIA_EJECUCION_TESTS.md               (Instrucciones paso a paso)

/app/src/androidTest/java/.../           (2 clases de prueba)
├── LoginScreenTest.kt                   (6 Tests Unitarios)
└── LoginIntegrationTest.kt              (7 Tests E2E)

RAÍZ:
├── RESUMEN_EJECUTIVO_FINAL.md           (Resumen rápido)
└── ENTREGABLE_FINAL.txt                 (ASCII art visual)
```

### 🚀 INICIO RÁPIDO

**Para revisar la documentación completa:**
```
1. Abre: docs/README.md
2. Sigue los links en orden de lectura
3. Tiempo estimado: 90 minutos
```

**Para ejecutar los tests:**
```powershell
./gradlew connectedAndroidTest
# Los reportes se generan en: app/build/reports/androidTests/connected/
```

**Para ver un resumen visual:**
```
1. Lee: RESUMEN_EJECUTIVO_FINAL.md (5 minutos)
2. Lee: docs/GUIA_VISUAL_RAPIDA.md (10 minutos)
3. Lee: docs/TABLAS_REFERENCIA_RAPIDA.md (5 minutos)
```

### 📊 MÉTRICAS CLAVE

| Métrica | Cantidad | Status |
|---------|----------|--------|
| Historias de Usuario | 5 | ✅ |
| Casos de Prueba Manual | 7 | ✅ |
| Tests Automatizados | 13 | ✅ |
| Prompts IA Documentados | 3 | ✅ |
| Líneas de Código/Docs | ~3,000 | ✅ |
| Cobertura de TC | 100% | ✅ |

### 🎯 REQUISITOS CUMPLIDOS

- ✅ 5 HU en INVEST + Criterios Gherkin
- ✅ 5+ Casos de Prueba Manual (7 entregados)
- ✅ 3 Prompts IA + Análisis Crítico
- ✅ 1+ Caso Automatizado (13 tests)
- ✅ Evidencias y Reportes Completos

### 📖 DOCUMENTACIÓN PRINCIPAL

- **[RESUMEN_EJECUTIVO_FINAL.md](./RESUMEN_EJECUTIVO_FINAL.md)** - Resumen rápido (5 min)
- **[docs/README.md](./docs/README.md)** - Punto de entrada (5 min)
- **[docs/README_AUTOMATIZACION.md](./docs/README_AUTOMATIZACION.md)** - Visión general (15 min)
- **[docs/TC001_TC007_CasosPruebaLogin.md](./docs/TC001_TC007_CasosPruebaLogin.md)** - Casos manual (20 min)
- **[docs/PROMPTS_Y_REVISIONES.md](./docs/PROMPTS_Y_REVISIONES.md)** - IA documentada (20 min)
- **[docs/GUIA_EJECUCION_TESTS.md](./docs/GUIA_EJECUCION_TESTS.md)** - Cómo ejecutar (10 min)

### 🧪 AUTOMATIZACIÓN

Los tests están implementados con **Espresso + JUnit4** y cubren:
- ✅ UI Testing (Búsqueda de elementos, clics, validaciones)
- ✅ Integration Testing (E2E con Firebase)
- ✅ Validaciones de entrada
- ✅ Manejo de errores
- ✅ Navegación

**Usuario de prueba para ejecutar tests:**
```
Email: test@gmail.com
Contraseña: test1234
```

---

## 🔗 Enlaces Útiles

- [Documentación Completa](./docs/)
- [Resumen Ejecutivo](./RESUMEN_EJECUTIVO_FINAL.md)
- [Guía Visual Rápida](./docs/GUIA_VISUAL_RAPIDA.md)
- [Tablas de Referencia](./docs/TABLAS_REFERENCIA_RAPIDA.md)