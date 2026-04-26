Alfa Queso Sistema de gestion 
es una solución móvil de alto rendimiento diseñada para la optimización de la cadena de suministro y control de inventarios en el sector lácteo. La plataforma integra una arquitectura reactiva para garantizar la integridad de los datos y una toma de decisiones basada en métricas en tiempo real.

Características Principales
Dashboard Reactivo: Visualización instantánea del stock total de productos y métricas de ventas.
Gestión de Inventario: Registro, actualización y eliminación de productos (Queso de Hoja, Queso Geo, Queso de Freír, etc.).
Flujo de Datos en Tiempo Real: Implementación de Kotlin Flows para asegurar que cualquier cambio en la base de datos se refleje automáticamente en la interfaz de usuario sin necesidad de recargar.
Arquitectura Limpia (Clean Architecture): Separación clara de responsabilidades entre la capa de datos, dominio y presentación.

Stack Tecnológico
Lenguaje: Kotlin
UI: Jetpack Compose (Arquitectura moderna y declarativa).
Base de Datos: Room implementa una arquitectura Offline-First, utilizando Room (como capa de abstracción sobre SQLite) para garantizar un acceso local, rápido y fluido a los datos del inventario, incluso sin conexión a internet. Esto se complementa con una API REST encargada de la sincronización y el respaldo de la información en la nube en tiempo real. Gracias a este enfoque híbrido y al uso de flujos reactivos (Flows), el sistema asegura que el Dashboard refleje automáticamente cualquier cambio en el stock, ofreciendo una solución de gestión robusta y profesional para el negocio.
Inyección de Dependencias: Hilt (Dagger Hilt para un código más desacoplado y testeable).
Arquitectura: MVVM (Model-View-ViewModel) + Clean Architecture.
Serialización: Moshi para la conversión de datos JSON/DTO.

Arquitectura de Datos
El proyecto sigue un flujo unidireccional de datos para garantizar la integridad de la información:
Entidades (Data Layer): Representación de las tablas en Room (ProductoEntity).
Mappers: Funciones de extensión que transforman objetos de base de datos a modelos de dominio (toDomain, toEntity).
Repositorios: Interfaz y su implementación que actúan como única fuente de verdad (InventarioRepositoryImpl).
Casos de Uso (Use Cases): Lógica de negocio pura e independiente.
ViewModels: Manejo del estado de la UI y comunicación con la capa de dominio.
