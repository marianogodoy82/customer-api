# Customer API

API REST de gestión de clientes desarrollada con **Spring Boot 4.0.2** y **Java 21**, estructurada con **arquitectura hexagonal (Ports & Adapters)**.

## Arquitectura Hexagonal

El proyecto separa la lógica de negocio de los detalles de infraestructura mediante tres capas:

```
com.yappa.customerapi/
│
├── domain/                          # Núcleo de negocio (sin dependencias de framework)
│   ├── model/Cliente.java           # Modelo de dominio (POJO puro)
│   ├── exception/                   # Excepciones de dominio
│   └── port/
│       ├── in/ClienteService.java   # Puerto de entrada (casos de uso)
│       └── out/ClienteRepository.java  # Puerto de salida (persistencia)
│
├── application/                     # Implementación de casos de uso
│   └── service/ClienteServiceImpl.java
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

### Principios aplicados

- **Domain** no tiene dependencias de Spring, JPA ni Lombok. Es un POJO puro con interfaces (ports) que definen contratos.
- **Application** implementa la lógica de negocio usando solo los ports definidos en el dominio.
- **Infrastructure** contiene los adaptadores que conectan el mundo exterior con el dominio:
  - **Adapter IN (REST)**: recibe requests HTTP, convierte DTOs a modelos de dominio y delega al puerto de entrada.
  - **Adapter OUT (Persistence)**: implementa el puerto de salida usando JPA y JDBC, convirtiendo entre entidades JPA y modelos de dominio.

## Stack tecnológico

- Java 21
- Spring Boot 4.0.2
- Spring Data JPA + PostgreSQL
- Flyway (migraciones de base de datos)
- Lombok (entidades JPA)
- Springdoc OpenAPI (documentación Swagger)
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

Los tests de integración usan Testcontainers con PostgreSQL. Requieren Docker:

```bash
./mvnw test
```

## Base de datos

PostgreSQL 16 con migraciones Flyway:

- `V1__schema.sql` — Tabla `clientes`, constraints, trigger de `fecha_modificacion`, datos iniciales
- `V2__functions.sql` — Función `search_clientes_by_nombre` para búsqueda por nombre
