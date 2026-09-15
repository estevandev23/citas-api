# WF-001 — Recordatorio de citas próximas

**Trigger:** Schedule.

**Objetivo:** consultar citas `APPROVED` dentro de una ventana configurable (por ejemplo, próximas 24 h), enviar Gmail al usuario ficticio/de laboratorio y registrar resultado.

## Requisitos

- No enviar recordatorio a `CANCELLED`/`REJECTED`.
- Evitar duplicado para la misma cita/ventana según estrategia del estudiante.
- Manejar API no disponible.
- Credenciales fuera del JSON.
- Ejecutar prueba controlada antes de activar.

## Entregable

`WF-001-appointment-reminders.json`.
