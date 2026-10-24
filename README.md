# AutoRent — Sistema de Alquiler de Autos

Aplicación REST para registrar autos y clientes, consultar autos disponibles y crear, consultar y finalizar alquileres.

## Tecnologías utilizadas

- Java 17.
- Spring Boot 4.1.2.
- Spring MVC (`spring-boot-starter-webmvc`).
- Spring Data JPA (`spring-boot-starter-data-jpa`) y Jakarta Persistence.
- H2 (`com.h2database:h2`), con versión administrada por Spring Boot.
- Maven Wrapper (`mvnw` y `mvnw.cmd`).

El proyecto también declara `spring-boot-starter-validation` y dependencias de prueba para JPA, Validation y Web MVC. Las reglas de validación descritas en este documento están implementadas en los servicios; los controladores no declaran validación automática de los cuerpos mediante anotaciones.

## Requisitos previos

- JDK 17.
- Un terminal en la raíz del proyecto. Se puede usar el Maven Wrapper incluido, por lo que no es necesario instalar Maven globalmente.

## Instalación y ejecución local

Desde PowerShell, en la raíz del proyecto:

```powershell
.\mvnw.cmd spring-boot:run
```

Para compilar y ejecutar las pruebas automatizadas:

```powershell
.\mvnw.cmd test
```

## Configuración de la base de datos

La configuración de `src/main/resources/application.properties` utiliza H2 en memoria:

- URL: `jdbc:h2:mem:autorent`
- Controlador: `org.h2.Driver`
- Usuario: `sa`
- Contraseña: vacía
- `spring.jpa.hibernate.ddl-auto=create-drop`: Hibernate crea el esquema al iniciar y lo elimina al cerrar.
- `spring.jpa.show-sql=true`: muestra las consultas SQL.
- `spring.jpa.open-in-view=false`.
- Consola H2 habilitada en `/h2-console`.

## Arquitectura del proyecto

El código sigue una organización por dominio y puertos/adaptadores:

- `com.autorent.domain.model`: modelos `Auto`, `Cliente`, `Alquiler` y el enum `EstadoAlquiler`, con sus estados y operaciones de dominio.
- `com.autorent.application.port.input`: contratos de los casos de uso para registrar autos y clientes, y crear y finalizar alquileres.
- `com.autorent.application.service`: `AutoService`, `ClienteService` y `AlquilerService`, que implementan los casos de uso y contienen las reglas de negocio.
- `com.autorent.application.port.output`: contratos de persistencia para autos, clientes y alquileres.
- `com.autorent.infrastructure.adapter.input.rest`: controladores REST que reciben las solicitudes y llaman a los casos de uso o puertos de consulta.
- `com.autorent.infrastructure.adapter.output.persistence.adapter`: adaptadores que implementan los puertos de salida y convierten entre modelos de dominio y entidades JPA.
- `com.autorent.infrastructure.adapter.output.persistence.entity`: entidades JPA `AutoEntity`, `ClienteEntity` y `AlquilerEntity`.
- `com.autorent.infrastructure.adapter.output.persistence.repository`: repositorios Spring Data JPA.
- `com.autorent.config.BeanConfiguration`: configuración de los beans de casos de uso.
- `com.autorent.AutorentApplication`: punto de entrada de Spring Boot.

## Funcionalidades implementadas

- Registrar autos y consultarlos por ID o listar los disponibles.
- Registrar clientes y consultarlos por ID.
- Crear alquileres para un cliente y un auto existentes, consultar un alquiler por ID y finalizarlo.
- Calcular la duración en días y el valor total del alquiler a partir del precio diario del auto.
- Cambiar el estado del auto a `ALQUILADO` al crear un alquiler y a `DISPONIBLE` al finalizarlo.
- Persistir los datos mediante JPA en la base H2 configurada.

## Endpoints REST

Los controladores están bajo `/api/autos`, `/api/clientes` y `/api/alquileres`.

| Método | Ruta | Propósito | Respuestas implementadas |
|---|---|---|---|
| `POST` | `/api/autos` | Registrar un auto. | `201 Created`; el cuerpo es el auto creado. |
| `GET` | `/api/autos/{id}` | Consultar un auto por ID. | `200 OK`; `404 Not Found` si no se encuentra. |
| `GET` | `/api/autos/disponibles` | Listar autos cuyo estado es `DISPONIBLE`. | `200 OK`; el cuerpo es una lista de autos. |
| `POST` | `/api/clientes` | Registrar un cliente. | `201 Created`; `400 Bad Request` por argumentos no válidos; `409 Conflict` si el documento ya existe. |
| `GET` | `/api/clientes/{id}` | Consultar un cliente por ID. | `200 OK`; `404 Not Found` si no se encuentra. |
| `POST` | `/api/alquileres` | Crear un alquiler. | `201 Created`; `400 Bad Request` por argumentos no válidos; `409 Conflict` si el auto no está disponible. Los errores tratados devuelven un objeto con la clave `error`. |
| `GET` | `/api/alquileres/{id}` | Consultar un alquiler por ID. | `200 OK`; `404 Not Found` con `{"error":"El alquiler no existe"}` si no se encuentra. |
| `PUT` | `/api/alquileres/{id}/finalizar` | Finalizar un alquiler. | `200 OK`; `404 Not Found` si el alquiler o auto no existe; `409 Conflict` si el alquiler ya está finalizado. Los errores tratados devuelven un objeto con la clave `error`. |

Los cuerpos JSON de registro usan estos campos:

```json
{
  "placa": "ABC123",
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2022,
  "precioPorDia": 75.50
}
```

```json
{
  "nombre": "Ana Pérez",
  "documento": "DOC-12345",
  "telefono": "3001234567",
  "email": "ana@example.com"
}
```

```json
{
  "clienteId": 1,
  "autoId": 1,
  "fechaInicio": "2026-10-24",
  "fechaFin": "2026-10-27"
}
```

El tercer ejemplo requiere IDs existentes: sustituye `clienteId` y `autoId` por los valores devueltos al registrar y consultar los recursos; los números del ejemplo no son IDs reservados. Las fechas corresponden al formato `LocalDate` `AAAA-MM-DD`.

## Ejemplos de uso

1. Envía el JSON de auto a `POST /api/autos` y conserva el `id` devuelto.
2. Envía el JSON de cliente a `POST /api/clientes` y conserva el `id` devuelto.
3. Usa ambos IDs reales y las fechas en `POST /api/alquileres`. La respuesta contiene el alquiler creado.
4. Consulta el alquiler con `GET /api/alquileres/{id}` o finalízalo con `PUT /api/alquileres/{id}/finalizar`.

## Validaciones y reglas de negocio

- Para autos, `placa`, `marca` y `modelo` no pueden ser nulos ni quedar vacíos; la placa se recorta y convierte a mayúsculas, y marca y modelo se recortan. `precioPorDia` debe ser mayor que cero. El auto nuevo inicia en estado `DISPONIBLE`. La entidad JPA declara la placa no nula y única.
- Para clientes, nombre, documento y teléfono son obligatorios; el email debe contener `@`. Los valores se recortan y el email se convierte a minúsculas. No se permite repetir el documento, tanto en el servicio como mediante una restricción única de la entidad.
- Para crear un alquiler, los IDs de cliente y auto y ambas fechas son obligatorios; la fecha final debe ser posterior a la inicial; el cliente y el auto deben existir; y el estado del auto debe ser `DISPONIBLE`. La duración se calcula como los días entre las fechas, sin incluir la fecha final, y el total es esa duración multiplicada por `precioPorDia`.
- Un alquiler nuevo tiene estado `ACTIVO`. No se puede finalizar dos veces. Al finalizarlo, el estado pasa a `FINALIZADO` y el auto asociado vuelve a `DISPONIBLE`.

No se observa una validación de rango para `anio` ni una validación de formato de email más allá de comprobar que contenga `@`.

## Pruebas automatizadas

La prueba existente es `com.autorent.AutorentApplicationTests.contextLoads`, anotada con `@SpringBootTest`; comprueba la carga del contexto de la aplicación. Para ejecutarla junto con las pruebas del proyecto:

```powershell
.\mvnw.cmd test
```