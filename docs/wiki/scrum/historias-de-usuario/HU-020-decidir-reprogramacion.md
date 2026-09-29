---
id: HU-020
tipo: historia-de-usuario
titulo: "Decidir reprogramación"
estado: Completada
epica: "[[EP-006-reprogramacion-y-decisiones-administrativas]]"
---
# HU-020 — Decidir reprogramación
ADMIN aprueba o rechaza una reprogramación pendiente con motivo al rechazar.

## Definition of Done
- [x] Aprobación mueve slots y cita; rechazo libera reserva provisional y conserva la cita original.
- [x] Bandeja y controles ADMIN verificados.

## Evidencia
`GET/POST /api/v1/admin/reschedules`, `SchedulingService#decideReschedule`, E2E Docker.
