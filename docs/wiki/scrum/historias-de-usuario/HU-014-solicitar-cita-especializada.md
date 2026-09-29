---
id: HU-014
tipo: historia-de-usuario
titulo: "Solicitar cita especializada"
estado: Completada
epica: "[[EP-005-ciclo-de-vida-de-citas]]"
---
# HU-014 — Solicitar cita especializada
USER solicita una especialidad no general y el sistema retiene el slot con estado `REQUESTED`.

## Definition of Done
- [x] Retención evita doble reserva.
- [x] Rechazo libera slots y exige motivo administrativo.

## Evidencia
`SchedulingService#book/decide`, recorrido especializado REQUESTED → REJECTED en Docker.
