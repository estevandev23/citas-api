---
id: HU-019
tipo: historia-de-usuario
titulo: "Solicitar reprogramación"
estado: Completada
epica: "[[EP-006-reprogramacion-y-decisiones-administrativas]]"
---
# HU-019 — Solicitar reprogramación
USER solicita una nueva franja para una cita aprobada propia; la original se conserva mientras la petición está pendiente.

## Definition of Done
- [x] Nueva franja retenida, profesional/especialidad conservados y ownership validado.
- [x] UI de solicitud y respuesta de estado implementadas.

## Evidencia
`POST /api/v1/appointments/{id}/reschedules`, migración V5 y recorrido Docker de reprogramación.
