---
id: HU-010
tipo: historia-de-usuario
titulo: "Configurar habilitaciones del profesional"
estado: Completada
epica: "[[EP-003-gestion-de-profesionales]]"
---
# HU-010 — Configurar habilitaciones del profesional
ADMIN asigna especialidades y sedes N:M y puede activar/desactivar profesionales.

## Definition of Done
- [x] Relaciones normalizadas, especialidad primaria y autorización ADMIN.
- [x] Disponibilidad solo publica sedes/especialidades habilitadas.

## Evidencia
Migración `V5__scheduling.sql`, `professional_specialty`, `professional_facility`, `toggleProfessional` y recorrido Docker.
