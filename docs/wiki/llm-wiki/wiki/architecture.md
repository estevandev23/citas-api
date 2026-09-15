# Arquitectura

## Estado

HECHO y DECISIÓN — verificado el 2026-09-15.

## Síntesis

El backend será Java 21 LTS con Spring Boot 3.5.x, Maven, arquitectura hexagonal, Spring Data JPA, MySQL 8.4, Flyway, Spring Security y JWT access/refresh. El dominio y la aplicación deben mantenerse independientes de los adaptadores.

El frontend consume directamente la API REST JSON de Spring Boot; no hay Express ni BFF. La URL del backend debe configurarse por ambiente.

**DEC-001 — Framework web:** Next.js con TypeScript. Esta decisión aprobada por la instrucción de orquestación del 2026-09-15 concreta la alternativa React/Angular de la documentación base. Cualquier AGENTS local de `citas-web` se depurará cuando el proyecto generado exista.

La única LLM Wiki global vive en `citas-api/docs/wiki/llm-wiki/`. Los workflows n8n se almacenarán como JSON en `citas-api/automations/n8n/`.

## Evidencia

- [PRD 1.0](../raw/2026-09-15-prd-v1.md)
- [Restricciones técnicas](../raw/2026-09-15-restricciones-tecnicas.md)
- [README del workspace](../raw/2026-09-15-readme-workspace.md)
- [README de citas-web](../raw/2026-09-15-readme-web.md)

## Enlaces relacionados

- [Decisiones](decisions.md)
- [Contratos REST](rest-contracts.md)
- [Seguridad](security.md)
