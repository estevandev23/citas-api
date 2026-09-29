---
id: HU-017
tipo: historia-de-usuario
titulo: "Consultar y cerrar agenda profesional"
estado: Completada
epica: "[[EP-005-ciclo-de-vida-de-citas]]"
---
# HU-017 — Consultar y cerrar agenda profesional
PROFESSIONAL consulta su agenda y marca citas aplicables como `COMPLETED` o `NO_SHOW`.

## Definition of Done
- [x] Ownership por profesional, filtro diario y transiciones auditadas.
- [x] Vista profesional y botones de cierre implementados.

## Evidencia
`GET /api/v1/professional/appointments`, `POST /api/v1/professional/appointments/{id}/close`, `StaffSchedulingView`.
