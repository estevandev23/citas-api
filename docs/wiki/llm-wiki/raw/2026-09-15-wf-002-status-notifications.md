# WF-002 — Notificación por cambio de estado

**Trigger:** webhook recibido desde `citas-api`.

## Eventos mínimos

- Cita especializada `APPROVED`/`REJECTED`.
- Reprogramación `APPROVED`/`REJECTED`.
- Cancelación.

## Requisitos

- Validar payload mínimo.
- Ramificar por tipo/estado.
- Gmail con mensaje coherente.
- Respuesta webhook determinista.
- Error/reintento/trazabilidad.
- Secretos y credenciales fuera del JSON.

## Entregable

`WF-002-status-notifications.json`.
