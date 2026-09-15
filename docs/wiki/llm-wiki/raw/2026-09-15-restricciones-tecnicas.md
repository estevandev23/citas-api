# Restricciones técnicas y Definition of Architecture

## Backend

- Java 21 LTS, Spring Boot 3.5.x y Maven.
- Arquitectura hexagonal: dominio/aplicación independientes de adaptadores.
- Spring Data JPA, MySQL 8.4 y Flyway.
- Spring Security con JWT access/refresh.
- API REST JSON; Actuator health recomendado.

## Frontend

- Node.js 24 LTS y TypeScript.
- React o Angular según selección/exportación de Stitch + Google AI Studio.
- Sin Express ni BFF; REST directo a `citas-api`.
- URL backend configurable por environment.
- Diseño aprobado Stitch/AI Studio como fuente visual de verdad.

## Base de datos

- Normalización mínima 3FN.
- `database/reference/db.sql` es solución de referencia del trainer.
- Catálogos fijos por seed y datos sintéticos.
- Justificar claves, cardinalidades y dependencias funcionales.

## Repositorios y Git

Solo repos públicos `citas-api` y `citas-web`. `main` es estable y `develop` es trabajo. Como mínimo, un commit trazable por sesión S2-S6; no reescribir historia para ocultar progreso.

## Documentación y variables de entorno

En `citas-api/docs/wiki/` viven `scrum/` y `llm-wiki/`. No subir contraseñas DB, secretos JWT, OAuth Gmail/n8n, tokens MCP ni credenciales personales. Cada repo contiene `.env.example` sin secretos reales.

## Pruebas y n8n

S3+: pruebas de dominio, aplicación e integración relevante backend; build/typecheck y pruebas aplicables frontend; validación de contrato cross-repo en funcionalidades clave. Los workflows n8n se exportan/versionan como JSON en `citas-api/automations/n8n/`; credenciales se configuran fuera del JSON.
