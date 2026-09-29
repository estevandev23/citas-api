---
id: HU-011
tipo: historia-de-usuario
titulo: "Gestionar bloques de agenda"
estado: Completada
epica: "[[EP-004-agenda-y-disponibilidad]]"
---
# HU-011 — Gestionar bloques de agenda
PROFESSIONAL crea, edita y elimina bloques futuros sin solapamientos ni pérdida de citas comprometidas.

## Definition of Done
- [x] Crear, editar y eliminar bloque; validación de sede, pasado y solapamiento.
- [x] Bloque con slots comprometidos no puede alterarse destructivamente.

## Evidencia
`POST/PATCH/DELETE /api/v1/professional/availability`, `SchedulingService`, controles de bloques en `StaffSchedulingView`.
