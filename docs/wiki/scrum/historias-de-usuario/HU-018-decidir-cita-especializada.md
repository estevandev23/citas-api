---
id: HU-018
tipo: historia-de-usuario
titulo: "Decidir cita especializada"
estado: Completada
epica: "[[EP-006-reprogramacion-y-decisiones-administrativas]]"
---
# HU-018 — Decidir cita especializada
ADMIN aprueba o rechaza solicitudes `REQUESTED`; el rechazo exige motivo.

## Definition of Done
- [x] Autorización ADMIN, transición explícita, motivo e historial.
- [x] Rechazo libera slots retenidos.

## Evidencia
`GET/POST /api/v1/admin/appointments`, `SchedulingService#decide`, bandeja administrativa.
