---
id: HU-012
tipo: historia-de-usuario
titulo: "Consultar disponibilidad"
estado: Completada
epica: "[[EP-004-agenda-y-disponibilidad]]"
---
# HU-012 — Consultar disponibilidad
USER consulta slots filtrados por sede, especialidad, profesional y fecha, respetando duración de 30/60 minutos.

## Definition of Done
- [x] Slots consecutivos y exclusión de reservas/retenciones.
- [x] Selector de filtros y estados de carga, vacío y error en `SchedulingView`.

## Evidencia
`GET /api/v1/availability`, `SchedulingService#potentialSlots`, recorrido E2E de paciente.
