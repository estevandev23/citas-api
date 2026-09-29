---
id: HU-021
tipo: historia-de-usuario
titulo: "Recordar citas próximas"
estado: Preparada
epica: "[[EP-007-automatizacion-operativa]]"
---
# HU-021 — Recordar citas próximas
El workflow importable consulta citas `APPROVED`, evita estados cancelados/rechazados y prepara un correo de laboratorio.

## Definition of Done
- [x] JSON versionado sin credenciales y flujo inactivo hasta configurar Gmail/API del trainer.
- [x] Validación estructural automatizada.

## Evidencia
`automations/n8n/WF-001-appointment-reminders.json`, `scripts/validate-n8n-json.ps1`.
