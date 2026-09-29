---
id: HU-022
tipo: historia-de-usuario
titulo: "Notificar cambios de estado"
estado: Preparada
epica: "[[EP-007-automatizacion-operativa]]"
---
# HU-022 — Notificar cambios de estado
El webhook importable valida y clasifica cambios de cita/reprogramación y responde de forma determinista.

## Definition of Done
- [x] JSON versionado sin credenciales, validación de eventos y respuesta 200/422.
- [ ] Activación contra n8n del trainer y Gmail individual quedan fuera de este workspace.

## Evidencia
`automations/n8n/WF-002-status-notifications.json`, `scripts/validate-n8n-json.ps1`.
