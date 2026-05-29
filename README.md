# OrDexxa Backend

Backend del MVP de OrDexxa para la funcionalidad **Registrar Proveedor**.

## Contexto

OrDexxa es una aplicación web para la gestión de ventas y pedidos de una microempresa mayorista de vaporizadores.

El alcance implementado en este MVP es el registro de proveedores.

## Tecnologías

- Java 21
- Spring Boot 3.5.14
- Maven
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Arquitectura limpia adaptada a la línea base de UkoParking

## Arquitectura

El backend separa responsabilidades en:

- `features`: casos de uso del negocio.
- `application`: puertos de entrada, DTOs y lógica de aplicación.
- `usecase`: implementación del caso de uso.
- `infrastructure`: adaptadores REST y persistencia JPA.
- `initializer`: configuración de beans.
- `crosscutting`: utilidades y manejo transversal.

## Caso de uso implementado

### Registrar proveedor

Endpoint interno del backend:

POST http://localhost:8081/api/proveedores

Este endpoint no está pensado para ser consumido directamente por el usuario final.  
El acceso externo se realiza mediante el API Gateway.

## Ejemplo de request

{
  "businessName": "Distribuciones Vapor S.A.S.",
  "documentNumber": "901234567-8",
  "email": "proveedor@ordexxa.com",
  "phoneNumber": "3001234567",
  "address": "Medellín, Antioquia"
}

## Ejemplo de response

{
  "id": "uuid-generado",
  "businessName": "Distribuciones Vapor S.A.S.",
  "documentNumber": "901234567-8",
  "message": "Proveedor registrado exitosamente"
}

## Base de datos local

La base de datos local usada para pruebas es PostgreSQL.

Configuración usada:

spring.datasource.url=jdbc:postgresql://localhost:5433/ordexxa_db
spring.datasource.username=ordexxa_user
spring.datasource.password=ordexxa_password

## Ejecución

Compilar:

./mvnw clean compile

Ejecutar:

./mvnw spring-boot:run

El backend corre en:

http://localhost:8081

## Prueba con Postman

Para probar directamente el backend:

POST http://localhost:8081/api/proveedores

Para probar el flujo correcto mediante API Gateway:

POST http://localhost:8080/api/proveedores

## Evidencias recomendadas

- Backend corriendo en puerto 8081.
- API Gateway corriendo en puerto 8080.
- Postman respondiendo 201 Created mediante el gateway.
- Consulta SQL mostrando proveedores registrados en PostgreSQL.

---

## Configuración cloud-ready e integraciones futuras

El backend de OrDexxa está preparado para despliegue posterior en nube mediante variables de entorno y configuración externalizada.

### Variables externalizadas

El archivo `.env.example` documenta las variables necesarias para configurar:

- Puerto del backend.
- Conexión a PostgreSQL.
- SMTP local o real, incluyendo SendGrid.
- Tiempo de expiración del código de verificación.
- Swagger/OpenAPI.
- Actuator y Prometheus.
- JPA/Hibernate.
- CORS para frontend, gateway o dominios cloud.

No se deben versionar secretos reales, API keys, contraseñas ni tokens.

### Redis

Redis puede integrarse después como caché para catálogos, parámetros, plantillas de notificación, sesiones o tokens.  
La lógica actual ya separa catálogos, repositorios y servicios, por lo que Redis debe agregarse como infraestructura, sin rehacer los casos de uso.

### Auth0

Auth0 puede integrarse después reemplazando el token manual actual por OAuth2/JWT.  
La autenticación está concentrada en `AuthService` y la autorización del registro de proveedor se valida antes de ejecutar el caso de uso, lo que facilita migrar hacia Spring Security Resource Server con Auth0.

### Vault o gestor de secretos

El backend ya lee configuración sensible desde variables de entorno.  
Más adelante, esas variables pueden provenir de Vault, Render, Azure, GitHub Actions u otro gestor de secretos.

### Grafana, Prometheus y OpenTelemetry

El backend ya expone métricas mediante Actuator y Prometheus.  
Grafana puede conectarse posteriormente a Prometheus para dashboards.  
OpenTelemetry puede agregarse después para trazas distribuidas sin cambiar la lógica de negocio.

### WAF y API Gateway

El WAF debe ubicarse preferiblemente en el API Gateway o en la infraestructura cloud.  
El backend ya queda preparado con CORS configurable, errores controlados y respuestas sin stacktraces técnicos para el usuario final.

### Despliegue cloud

Para nube, el backend debe configurarse mediante variables de entorno, no modificando código fuente.  
Esto permite conectar PostgreSQL externo, SendGrid, frontend en Vercel, API Gateway en Render u otros servicios equivalentes.
