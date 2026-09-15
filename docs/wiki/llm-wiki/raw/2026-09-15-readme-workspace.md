# Proyecto FCV — Sistema ficticio de agendamiento de citas

Plantilla de trabajo para sesiones S2 a S6 de formación de agentes. Los repositorios `citas-api` y `citas-web` empiezan sin lógica de negocio para que el estudiante los construya con agentes, especificaciones, pruebas, automatización y evidencia Git.

El dominio es académico: sedes y algunos nombres de especialidades se apoyan en información pública; pacientes, profesionales, credenciales, EPS, planes, horarios y citas son sintéticos. No representa sistemas o procesos internos reales de FCV.

## Estructura y stack objetivo

La raíz orquesta dos repositorios Git independientes: `citas-api` y `citas-web`. Backend: Java 21, Spring Boot 3.5.x, Maven, hexagonal, JPA, Flyway, MySQL 8.4, REST/JSON y JWT access/refresh. Frontend: React o Angular con TypeScript y Node 24 LTS, sin Express/BFF y consumiendo REST directamente.

La única LLM Wiki global se versiona en `citas-api/docs/wiki/llm-wiki/`. Las Skills disponibles son `scrum-spec-orchestrator` y `stitch-design-to-frontend`.

## Flujo esperado

PRD + restricciones → Scrum specs → épicas/HU/CA/DoD → Stitch/diseño aprobado/AI Studio → `citas-web` ↕ REST ↕ `citas-api` → MySQL → S3 verificación → S4 autonomía → S5-S6 MCP+n8n.

## Infraestructura y Git

Docker puede levantar MySQL y toolchains. `scripts/init-repos.ps1` crea `main` como rama estable y `develop` como trabajo. La lógica se construye en `develop` y se fusiona a `main` cuando el incremento es estable.

Los JSON n8n se versionan en `citas-api/automations/n8n/`. `database/reference/db.sql` es solución de referencia que el trainer puede ocultar durante normalización.
