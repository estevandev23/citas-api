# Auditoría de entrega S2–S6

Fecha de cierre: 2026-09-29.

## Resultado

El núcleo funcional S2–S4 está implementado y usable contra los contenedores Docker. Se completaron los artefactos de entrega que faltaban para S5–S6: JSON importables de WF-001/WF-002, JSON opcional WF-003, hooks locales de calidad/secretos y logs verificables de goals/loops.

## Matriz

| Sesión | Requisito | Estado | Evidencia |
|---|---|---|---|
| S2 | Backend Spring/Flyway/MySQL, JWT, frontend ejecutable, Scrum y wiki | Cumple | `src/`, `db/migration/`, `docs/wiki/`, commits históricos de bootstrap/login |
| S3 | Reserva general/especializada, decisiones ADMIN, slots, autorización y pruebas | Cumple funcionalmente | `SchedulingService`, `SchedulingController`, recorrido REST Docker/MySQL, `docs/wiki/llm-wiki/wiki/delivery-and-evidence.md` |
| S3 | Hook de pruebas y bloqueo de secretos | Cumple | `.githooks/pre-commit` en ambos repos, `scripts/install-hooks.ps1` |
| S4 | Cancelación, reprogramación, agenda profesional, cierre, perfil y catálogos | Cumple funcionalmente | API REST, `SchedulingView`, `StaffSchedulingView`, `PatientProfileView`, `LOOP-01` y `LOOP-02` |
| S4 | Loops Builder/Verifier con stop condition y log | Cumple | `docs/evidence/loops/*.json` |
| S5 | WF-001 JSON sin credenciales | Cumple | `automations/n8n/WF-001-appointment-reminders.json` |
| S5 | Análisis de contenido no confiable/riesgos | Cumple | `docs/wiki/llm-wiki/wiki/risks-and-open-questions.md`, `wiki/security.md` |
| S6 | WF-002 JSON sin credenciales | Cumple | `automations/n8n/WF-002-status-notifications.json` |
| S6 | WF-003 opcional | Cumple como bonus | `automations/n8n/WF-003-daily-operational-summary.json` |
| S6 | JSON validado/importable | Cumple estructuralmente | `scripts/validate-n8n-json.ps1` |
| S6 | Sustentación y trazabilidad Git | Cumple en repositorio | `main` permanece estable y `develop` contiene commits separados para S3, S4, S5 y S6 en ambos repos; S2 queda representado por los commits históricos de bootstrap/login |

## Configuración externa pendiente

Los workflows quedan inactivos (`active: false`) y no contienen credenciales. Para activarlos en n8n el trainer debe configurar el nodo Gmail y las variables `CITAS_API_BASE_URL`, `CITAS_API_TOKEN` y, para WF-003, `OPERATIONS_EMAIL`. La activación y la invocación MCP contra una instancia externa no se simulan ni se hacen con credenciales dentro del repositorio; por eso permanecen como pendiente operativo del entorno del trainer, no como faltante de código/versionado.

## Validación de uso

- Paciente sintético: login, portal, disponibilidad y cita aprobada.
- Administrador sintético: bandeja de solicitudes y catálogos configurables.
- Backend: suite Maven en Docker.
- Frontend: Vitest, typecheck, build y lint en Docker.
