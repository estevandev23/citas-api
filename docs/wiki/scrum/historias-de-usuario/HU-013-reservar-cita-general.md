---
id: HU-013
tipo: historia-de-usuario
titulo: "Reservar cita general"
estado: Completada
epica: "[[EP-005-ciclo-de-vida-de-citas]]"
---
# HU-013 — Reservar cita general
USER reserva Medicina General y obtiene `APPROVED` automáticamente si el slot continúa disponible.

## Definition of Done
- [x] Reserva transaccional y slot único.
- [x] Estado APPROVED e historial SYSTEM verificables.

## Evidencia
`POST /api/v1/appointments`, `SchedulingService#book`, recorrido REST MySQL y cita demo.
