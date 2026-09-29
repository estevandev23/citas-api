---
id: HU-009
tipo: historia-de-usuario
titulo: "Registrar profesional"
estado: Completada
epica: "[[EP-003-gestion-de-profesionales]]"
---
# HU-009 — Registrar profesional
ADMIN puede crear un profesional ficticio con usuario, código, matrícula, especialidad y sede.

## Definition of Done
- [x] Endpoint ADMIN, validación de asignaciones, rol PROFESSIONAL y UI operativa.
- [x] Prueba E2E de creación y consulta de profesionales en Docker.

## Evidencia
`SchedulingController#createProfessional`, `SchedulingService#createProfessional`, `StaffSchedulingView`, `POST /api/v1/admin/professionals`.
