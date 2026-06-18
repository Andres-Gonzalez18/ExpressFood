# ExpressFood 🍔📱

**ExpressFood** es una aplicación móvil de entrega de comida diseñada con una arquitectura moderna y robusta, que permite la gestión tanto para clientes como para administradores en tiempo real.

## 🚀 Características Principales

### 👤 Panel de Clientes
- **Menú Digital:** Visualización de productos disponibles con precios, descripciones y calificaciones.
- **Carrito de Compras:** Gestión persistente de productos antes de realizar el pedido.
- **Seguimiento de Pedidos:** Historial detallado de órdenes realizadas.
- **Reportes de Consumo:** Visualización de gastos y hábitos de compra.
- **Modo Offline:** Acceso al menú y datos básicos incluso sin conexión a internet gracias a la base de datos local.

### 👑 Panel de Administrador
- **Dashboard de Control:** Vista general del estado del negocio.
- **Gestión de Pedidos:** Control de flujo de órdenes en tiempo real.
- **Reportes Avanzados:** Generación de reportes diarios y totales mensuales.
- **Sincronización:** Actualización automática con la nube para mantener los datos al día.

### 🔐 Seguridad y Autenticación
- **Firebase Auth:** Inicio de sesión seguro (soporte para Email y Google Auth).
- **Roles de Usuario:** Diferenciación estricta entre flujos de Cliente y Administrador.

---

## 🛠️ Stack Tecnológico

- **Lenguaje:** [Kotlin](https://kotlinlang.org/)
- **Arquitectura:** MVVM (Model-View-ViewModel) para una separación de responsabilidades limpia.
- **Base de Datos Local:** [Room Persistence Library](https://developer.android.com/training/data-storage/room) para persistencia offline.
- **Base de Datos en la Nube:** [Firebase Firestore](https://firebase.google.com/docs/firestore) para sincronización en tiempo real.
- **Inyección de Dependencias/Componentes:** Android Jetpack (ViewModel, LiveData, Flow).
- **Tareas en Segundo Plano:** [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) para sincronización de datos asíncrona.
- **Interfaz de Usuario:** XML con ViewBinding y Material Design 3.
- **Carga de Imágenes:** [Glide](https://github.com/bumptech/glide) para manejo eficiente de caché de imágenes.

---

## 📂 Estructura del Proyecto

```text
edu.proyecto.expressfoodapp
├── data                # Capa de datos (Repositorios, Local, Remoto)
│   ├── local           # Configuración de Room (DAOs, Entidades, DB)
│   ├── remote          # Lógica de Firebase Firestore
│   └── repository      # Fuente de la verdad (decide si usar Local o Remoto)
├── ui                  # Capa de presentación (Activities y Adapters)
│   ├── auth            # Login y Registro
│   ├── admin           # Dashboard, Pedidos y Reportes Administrativos
│   └── client          # Menú, Carrito, Pedidos y Reportes de Cliente
├── viewmodel           # Lógica de negocio vinculada a la UI
├── worker              # Tareas de sincronización en segundo plano
└── utils               # Clases de ayuda y constantes
```

---

## 🔄 Flujo de Sincronización

El proyecto utiliza un sistema de **Sincronización Híbrida**:
1. Los datos se descargan de **Firestore** y se almacenan en **Room**.
2. La UI observa los datos de la base de datos local (Room) mediante `Flow`.
3. El `SyncOrdersWorker` asegura que los pedidos locales y remotos estén siempre alineados, permitiendo que la app funcione correctamente bajo condiciones de red inestables.

---

## ⚙️ Configuración Requerida

Para ejecutar este proyecto, asegúrate de:
1. Tener un archivo `google-services.json` válido en la carpeta `/app`.
2. Habilitar **Email/Password** y **Google Sign-In** en tu consola de Firebase.
3. Crear las colecciones `products`, `orders` y `users` en Firestore.

---
© 2024 ExpressFood App - Proyecto de Desarrollo Móvil.
