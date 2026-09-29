---
id: HU-015
tipo: historia-de-usuario
titulo: "Consultar mis citas"
estado: Completada
epica: "[[EP-005-ciclo-de-vida-de-citas]]"
---
# HU-015 — Consultar mis citas
USER consulta sus citas con sede, profesional, especialidad, fecha, estado y motivo.

## Definition of Done
- [x] Ownership por `patient_user_id` y filtro por estado.
- [x] Tarjetas de citas visibles en el portal.

## Evidencia
`GET /api/v1/appointments/me`, `SchedulingView` y E2E paciente.
