# Customer API

API REST de gestión de clientes desarrollada con **Spring Boot 3.5.8** y **Java 21**, estructurada con **arquitectura hexagonal (Ports & Adapters)**.

## Arquitectura Hexagonal

El proyecto separa la lógica de negocio de los detalles de infraestructura mediante tres capas:

```
com.yappa.customerapi/
│
├── domain/                          # Núcleo de negocio (sin dependencias de framework)
│   ├── model/
│   │   ├── Cliente.java             # Aggregate root (factory methods: crear/reconstituir)
│   │   ├── Cuit.java                # Value Object (record) — formato XX-XXXXXXXX-X
│   │   ├── Email.java               # Value Object (record) — validación formato y longitud
│   │   └── Telefono.java            # Value Object (record) — validación longitud
│   ├── exception/
│   │   └── ClienteNotFoundException.java
│   └── port/
│       ├── in/                      # Puertos de entrada (Use Cases)
│       │   ├── CrearClienteUseCase.java
│       │   ├── ObtenerClienteUseCase.java
│       │   ├── BuscarClienteUseCase.java
│       │   ├── ActualizarClienteUseCase.java
│       │   └── EliminarClienteUseCase.java
│       └── out/
│           └── ClienteRepository.java  # Puerto de salida (persistencia)
│
├── application/                     # Implementación de casos de uso
│   └── service/ClienteServiceImpl.java  # Implementa los 5 Use Cases
│
└── infrastructure/                  # Adaptadores (detalles técnicos)
    ├── adapter/
    │   ├── in/rest/                 # Adaptador de entrada: REST API
    │   │   ├── ClienteController.java
    │   │   ├── dto/                 # Request/Response DTOs
    │   │   ├── mapper/              # Mapeo DTO <-> Dominio
    │   │   └── exception/           # Manejo global de errores
    │   └── out/persistence/         # Adaptador de salida: Base de datos
    │       ├── ClientePersistenceAdapter.java
    │       ├── ClienteJpaRepository.java
    │       ├── ClienteSearchJdbcRepository.java
    │       ├── entity/ClienteEntity.java  # Entidad JPA
    │       └── mapper/              # Mapeo Entidad <-> Dominio
    └── config/                      # Configuración (OpenAPI, etc.)
```

### Value Objects

El dominio utiliza **Java Records** como Value Objects para encapsular validaciones y garantizar inmutabilidad:

| Value Object | Validación |
|---|---|
| `Cuit` | Formato argentino `XX-XXXXXXXX-X` (regex) |
| `Email` | No vacío, máximo 150 caracteres, formato con `@` y dominio |
| `Telefono` | No vacío, máximo 30 caracteres |

Estos Value Objects se validan automáticamente en el constructor (compact constructor del record), lanzando `IllegalArgumentException` si los datos son inválidos.

### Use Cases

Los puertos de entrada están definidos como interfaces individuales siguiendo el **Interface Segregation Principle (ISP)**:

| Use Case | Método | Descripción |
|---|---|---|
| `CrearClienteUseCase` | `create(Cliente)` | Crear un nuevo cliente |
| `ObtenerClienteUseCase` | `getAll()`, `getById(Long)` | Obtener uno o todos los clientes |
| `BuscarClienteUseCase` | `searchByNombre(String)` | Búsqueda por nombre (función PostgreSQL) |
| `ActualizarClienteUseCase` | `update(Long, Cliente)` | Actualización parcial de cliente |
| `EliminarClienteUseCase` | `delete(Long)` | Eliminar cliente por ID |

El controller inyecta cada Use Case por su interfaz específica, sin depender de la implementación concreta.

### Principios aplicados

- **Domain** no tiene dependencias de Spring, JPA ni Lombok. Usa POJOs puros, Records (Value Objects) e interfaces (ports) que definen contratos.
- **Application** implementa la lógica de negocio usando solo los ports definidos en el dominio.
- **Infrastructure** contiene los adaptadores que conectan el mundo exterior con el dominio:
  - **Adapter IN (REST)**: recibe requests HTTP, convierte DTOs a modelos de dominio y delega al puerto de entrada.
  - **Adapter OUT (Persistence)**: implementa el puerto de salida usando JPA y JDBC, convirtiendo entre entidades JPA y modelos de dominio.
- **Modelo de dominio rico**: `Cliente` usa factory methods (`crear` / `reconstituir`) y métodos de actualización que validan invariantes. No expone setters públicos.

## Stack tecnológico

- Java 21
- Spring Boot 3.5.8
- Spring Data JPA + PostgreSQL
- Flyway (migraciones de base de datos)
- Lombok (entidades JPA)
- MapStruct (declarado en build)
- Springdoc OpenAPI 2.8.15 (documentación Swagger)
- Testcontainers (tests de integración)

## Requisitos previos

- Java 21+
- Docker y Docker Compose

## Ejecución

### Con Docker Compose (recomendado)

Levanta la base de datos y la aplicación:

```bash
docker compose up -d
```

La API estará disponible en `http://localhost:3000`.

### Solo la base de datos + ejecución local

```bash
docker compose up db -d
./mvnw spring-boot:run
```

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/clientes` | Listar todos los clientes |
| GET | `/api/clientes/{id}` | Obtener cliente por ID |
| GET | `/api/clientes/search?q={nombre}` | Buscar clientes por nombre |
| POST | `/api/clientes` | Crear cliente |
| PUT | `/api/clientes/{id}` | Actualizar cliente (parcial) |
| DELETE | `/api/clientes/{id}` | Eliminar cliente |

### Ejemplo: crear cliente

```bash
curl -X POST http://localhost:3000/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan",
    "apellido": "Pérez",
    "razonSocial": "JP Servicios",
    "cuit": "20-12345678-9",
    "fechaNacimiento": "1990-05-15",
    "telefonoCelular": "1155551234",
    "email": "juan.perez@example.com"
  }'
```

## Documentación API

Swagger UI disponible en: `http://localhost:3000/docs`

## Tests

El proyecto cuenta con tests unitarios y de integración organizados por capa:

### Tests unitarios

| Capa | Tests |
|------|-------|
| Domain (Value Objects) | `CuitTest`, `EmailTest`, `TelefonoTest` |
| Domain (Modelo) | `ClienteTest`, `ClienteNotFoundExceptionTest` |
| Application (Service) | `ClienteServiceImplTest` |
| Infrastructure (REST) | `ClienteControllerTest`, `ClienteRestMapperTest`, `GlobalExceptionHandlerTest` |
| Infrastructure (Persistence) | `ClientePersistenceAdapterTest`, `ClientePersistenceMapperTest` |
| Infrastructure (Config) | `OpenApiConfigTest` |

### Test de integración

`ClientesApiIT` — test end-to-end con `@SpringBootTest` + Testcontainers (PostgreSQL 16). Ejecuta el ciclo completo: crear, obtener, buscar, actualizar y eliminar.

### Ejecutar tests

Requieren Docker para Testcontainers:

```bash
./mvnw test
```

## Base de datos

PostgreSQL 16 con migraciones Flyway:

- `V1__schema.sql` — Tabla `clientes`, constraints, trigger de `fecha_modificacion`, datos iniciales
- `V2__functions.sql` — Función `search_clientes_by_nombre` para búsqueda por nombre
