---
id: EP-006
tipo: epica
titulo: "Reprogramación y decisiones administrativas"
estado: Completada
historias: ["[[HU-018-decidir-cita-especializada]]", "[[HU-019-solicitar-reprogramacion]]", "[[HU-020-decidir-reprogramacion]]"]
dependencias: ["[[EP-005-ciclo-de-vida-de-citas]]"]
---
# EP-006 — Reprogramación y decisiones administrativas
## Objetivo
Dar a ADMIN una bandeja trazable para resolver solicitudes especializadas y reprogramaciones.
## Valor esperado
Cambios de cita controlados sin perder la reserva original.
## Actores
- ADMIN; USER.
## Alcance
- Aprobación/rechazo especializado y ciclo PENDING de reprogramación.
## Fuera de alcance
- Cambio de profesional mediante reprogramación.
## Reglas de negocio
- Rechazo con motivo; la cita original persiste hasta aprobar; liberar reservas correctas.
## Dependencias
- [[EP-005-ciclo-de-vida-de-citas]]
## Historias de usuario
- [[HU-018-decidir-cita-especializada]]
- [[HU-019-solicitar-reprogramacion]]
- [[HU-020-decidir-reprogramacion]]
## Criterio de completitud de la épica
- [x] Todas las HU están `Completada`.
- [x] No se pierde la cita original durante reprogramación pendiente.
## Riesgos e incógnitas
- Payloads de notificación y transiciones permitidas pendientes.
