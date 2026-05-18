# Sistema Backend de Gestión Logística

Backend desarrollado en Java con Spring Boot para la gestión de operaciones logísticas.  
El sistema está diseñado con una arquitectura basada en microservicios, permitiendo separar responsabilidades y facilitar la escalabilidad del proyecto.

## Descripción del proyecto

Este proyecto permite gestionar distintas áreas de una empresa logística, incluyendo solicitudes de servicio, rutas, depósitos, transporte, tarifas y usuarios.

La solución está compuesta por varios microservicios independientes, conectados mediante un API Gateway y protegidos con autenticación y autorización utilizando Keycloak.

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker
- Docker Compose
- Spring Cloud Gateway
- Keycloak
- OAuth2 / JWT
- Swagger / OpenAPI
- Maven

## Arquitectura

El sistema está organizado en distintos módulos y servicios:

- **API Gateway**: centraliza el acceso a los microservicios.
- **Servicio de Atención**: gestiona solicitudes y operaciones relacionadas con clientes.
- **Servicio de Transporte**: administra camiones, transportistas y recursos de transporte.
- **Servicio de Rutas**: gestiona rutas logísticas y planificación.
- **Servicio de Depósitos**: administra depósitos y ubicaciones.
- **Servicio de Tarifas**: calcula y gestiona costos asociados al servicio logístico.
- **Servicio de Usuarios**: administra información relacionada con usuarios del sistema.
- **Common**: módulo compartido con clases reutilizables entre servicios.

## Funcionalidades principales

- Gestión de solicitudes logísticas.
- Administración de camiones y transportistas.
- Gestión de rutas.
- Administración de depósitos.
- Cálculo y gestión de tarifas.
- Gestión de usuarios.
- Seguridad centralizada con Keycloak.
- Autenticación y autorización mediante OAuth2/JWT.
- Documentación de APIs con Swagger/OpenAPI.
- Despliegue local mediante Docker Compose.

## Requisitos previos

Antes de ejecutar el proyecto, es necesario tener instalado:

- Java 17 o superior
- Maven
- Docker
- Docker Compose
- Git

## Configuración de variables de entorno

El proyecto utiliza variables de entorno para evitar exponer datos sensibles dentro del código.

Crear un archivo `.env` en la raíz del proyecto tomando como referencia el archivo `.env.example`.

Ejemplo:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=logistica_db

KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=admin

GOOGLE_API_KEY=your_google_api_key_here
