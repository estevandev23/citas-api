# citas-api

Repositorio backend del proyecto. El primer incremento contiene registro USER, login JWT y rotación de refresh token.

## Estado del incremento de identidad

- Java 21, Spring Boot 3.5.16, Maven, Spring Security y Spring Data JPA.
- Dominio y aplicación desacoplados de adaptadores HTTP/persistencia; transacciones en el adaptador.
- MySQL 8.4 mediante Flyway `V1__identity.sql`: usuarios, roles, relación N:M y sesiones de refresh.
- Contrato REST en `docs/wiki/llm-wiki/wiki/rest-contracts.md`.
- `mvn test` ejecuta pruebas de integración HTTP/persistencia con H2 en modo MySQL. No requiere credenciales locales para las pruebas.

Para ejecutar la aplicación se necesitan las variables de `./.env.example` con valores propios en el entorno. No se debe usar el valor de ejemplo como secreto. La URL y origen CORS del frontend son configurables.

El flujo implementado cubre registro, login y refresh con rotación. Logout y recuperación de contraseña pertenecen a incrementos posteriores.

## Documentación compartida
- `docs/wiki/scrum/`: épicas/HU generadas con la Skill Scrum.
- `docs/wiki/llm-wiki/`: única LLM Wiki global del workspace.
- `automations/n8n/`: JSON exportados en S5/S6.

Lee el PRD en la carpeta raíz antes de inicializar Spring Boot.
