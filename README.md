#  Sistema Parqueadero - Frontend Swing

**Aplicación de escritorio desarrollada en Java Swing con Spring Boot para la gestión de parqueaderos**

---

##  Descripción

Frontend de escritorio que consume la API REST del Sistema de Parqueadero. Desarrollado con arquitectura por capas siguiendo principios SOLID y clean code.

### Características Principales

-  **CRUD completo de Vehículos** con ingreso y registro de salida
-  **Gestión de Tarifas** por tipo de vehículo
-  **Administración de Clientes** con descuentos según tipo (Regular, VIP, Eventual)
-  **Cálculo automático de cobros** basado en tiempo de permanencia
-  **Interfaz gráfica intuitiva** con JTabbedPane
-  **Comunicación con API REST** mediante RestTemplate

---

## 🏗️ Arquitectura del Proyecto

```
SistemaParqueaderoFrontend/
├── src/main/java/com/parqueadero/frontend/
│   ├── FrontendApplication.java          # Clase principal Spring Boot
│   │
│   ├── client/                           # Capa de Cliente API
│   │   ├── VehiculoApiClient.java       # Consume /api/vehiculos
│   │   ├── TarifaApiClient.java         # Consume /api/tarifas
│   │   ├── ClienteApiClient.java        # Consume /api/clientes
│   │   └── CobroApiClient.java          # Consume /cobro
│   │
│   ├── dto/                              # Objetos de Transferencia
│   │   ├── VehiculoDTO.java
│   │   ├── TarifaDTO.java
│   │   ├── ClienteDTO.java
│   │   └── CobroResponseDTO.java
│   │
│   ├── window/                           # Interfaces Gráficas
│   │   ├── MainFrame.java               # Ventana principal
│   │   ├── VehiculoPanel.java           # Panel de vehículos
│   │   ├── TarifaPanel.java             # Panel de tarifas
│   │   ├── ClientePanel.java            # Panel de clientes
│   │   └── CobroPanel.java              # Panel de cobros
│   │
│   └── config/
│       └── RestTemplateConfig.java       # Configuración HTTP
│
└── src/main/resources/
    └── application.properties            # Configuración del backend URL
```

### **Separación por Capas**

1. **Capa de Presentación (window/)**: Interfaces gráficas Swing
2. **Capa de Cliente API (client/)**: Comunicación con el backend
3. **Capa de Transferencia (dto/)**: Objetos para intercambio de datos
4. **Capa de Configuración (config/)**: Beans de Spring

---

##  Requisitos Previos

- **JDK 17** o superior
- **Gradle 8.x**
- **Backend funcionando** en `http://localhost:9090/SistemaParqueadero`

---

##  Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/SistemaParqueaderoFrontend.git
cd SistemaParqueaderoFrontend
```

### 2. Configurar URL del Backend

Edita `src/main/resources/application.properties`:

```properties
api.parqueadero.base-url=http://localhost:9090/SistemaParqueadero
```

### 3. Compilar y ejecutar

```bash
# Con Gradle Wrapper
./gradlew bootRun

# O generar JAR ejecutable
./gradlew build
java -jar build/libs/SistemaParqueaderoFrontend-1.0.0.jar
```

---

##  Uso de la Aplicación

### Panel de Vehículos 
- **Registrar**: Ingresa placa, modelo y tipo de vehículo
- **Listar**: Visualiza todos los vehículos con estado (Activo/Pagado)
- **Eliminar**: Elimina registros por placa

### Panel de Tarifas 
- **Crear**: Define precio por hora según tipo de vehículo
- **Visualizar**: Consulta todas las tarifas activas

### Panel de Clientes 
- **Registrar**: Captura datos del cliente con tipo y descuento
- **Tipos**: Eventual (0%), Regular (10%), VIP (20%)

### Panel de Cobro 
- **Seleccionar**: Escoge vehículo activo de la lista
- **Cobrar**: Calcula automáticamente el monto según tiempo transcurrido
- **Ticket**: Genera resumen detallado con total a pagar

---

## 🔌 Conexión con el Backend

El frontend consume los siguientes endpoints:

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/api/vehiculos` | GET | Lista todos los vehículos |
| `/api/vehiculos` | POST | Registra nuevo vehículo |
| `/api/vehiculos?placa={placa}` | DELETE | Elimina vehículo |
| `/api/tarifas` | GET | Obtiene todas las tarifas |
| `/api/tarifas` | POST | Crea nueva tarifa |
| `/api/clientes` | GET | Lista todos los clientes |
| `/api/clientes` | POST | Registra nuevo cliente |
| `/cobro?placa={placa}` | POST | Registra salida y calcula cobro |

---

##  Tecnologías Utilizadas

- **Java 17** - Lenguaje de programación
- **Spring Boot 3.2.0** - Framework de aplicación
- **Swing** - Framework de interfaz gráfica
- **RestTemplate** - Cliente HTTP para consumir API
- **Lombok** - Reducción de código boilerplate
- **Gradle** - Gestión de dependencias

---

##  Dependencias Principales

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.cloud:spring-cloud-starter-openfeign:4.1.0'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}
```

---

##  Notas del Desarrollador

### Decisiones de Diseño

1. **Spring Boot + Swing**: Aprovecha la inyección de dependencias para componentes UI
2. **RestTemplate**: Simplifica las llamadas HTTP al backend
3. **DTOs independientes**: Desacoplamiento entre frontend y backend
4. **JTabbedPane**: Navegación intuitiva entre módulos

### Mejoras Futuras

- [ ] Implementar validación de campos con expresiones regulares
- [ ] Agregar paginación en tablas con muchos registros
- [ ] Incluir gráficos de estadísticas (vehículos por día, ingresos)
- [ ] Exportar reportes a PDF/Excel
- [ ] Implementar autenticación de usuarios

---

##  Autor

**Kevin David**  
Proyecto Académico - 2025  
[GitHub](https://github.com/tu-usuario)

---

##  Licencia

Este proyecto es de uso académico bajo licencia MIT.

---

##  Troubleshooting

### Error "Connection refused"
- Verifica que el backend esté corriendo en `http://localhost:9090`
- Revisa `application.properties` tiene la URL correcta

### No aparecen datos en las tablas
- Confirma que el backend tenga datos iniciales (ejecuta `parkingDB.sql`)
- Revisa los logs del backend para errores

### Interfaz no se muestra
- Asegúrate de que `java.awt.headless=false` en `FrontendApplication`
- Verifica que tu sistema soporte GUI (no WSL sin X11)

---

**¡Sistema listo para uso académico! 🎓**