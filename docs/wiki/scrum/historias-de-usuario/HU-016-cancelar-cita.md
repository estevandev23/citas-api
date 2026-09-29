---
id: HU-016
tipo: historia-de-usuario
titulo: "Cancelar cita"
estado: Completada
epica: "[[EP-005-ciclo-de-vida-de-citas]]"
---
# HU-016 — Cancelar cita
USER cancela una cita futura propia no terminal y libera sus slots con historial.

## Definition of Done
- [x] Ownership, futuro, transición CANCELLED y liberación de slots.
- [x] Acción disponible desde el portal.

## Evidencia
`POST /api/v1/appointments/{id}/cancel`, `SchedulingService#cancel` y `SchedulingView`.
