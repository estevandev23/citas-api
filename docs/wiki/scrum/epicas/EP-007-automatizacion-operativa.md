---
id: EP-007
tipo: epica
titulo: "Automatización operativa"
estado: En validación
historias: ["[[HU-021-recordar-citas-proximas]]", "[[HU-022-notificar-cambios-de-estado]]", "[[HU-023-resumir-operacion-diaria]]"]
dependencias: ["[[EP-005-ciclo-de-vida-de-citas]]", "[[EP-006-reprogramacion-y-decisiones-administrativas]]"]
---
# EP-007 — Automatización operativa
## Objetivo
Añadir automatizaciones n8n posteriores sin modificar el núcleo funcional.
## Valor esperado
Comunicación y visibilidad operativa de laboratorio sobre citas.
## Actores
- USER; ADMIN; operación de laboratorio.
## Alcance
- Recordatorios, notificación de cambios y resumen diario.
## Fuera de alcance
- Datos reales, credenciales embebidas, SMS/WhatsApp y modificación del núcleo funcional.
## Reglas de negocio
- Workflows JSON versionados en `automations/n8n/`; credenciales fuera del repositorio.
## Dependencias
- [[EP-005-ciclo-de-vida-de-citas]]
- [[EP-006-reprogramacion-y-decisiones-administrativas]]
## Historias de usuario
- [[HU-021-recordar-citas-proximas]]
- [[HU-022-notificar-cambios-de-estado]]
- [[HU-023-resumir-operacion-diaria]]
## Criterio de completitud de la épica
- [x] Las HU priorizadas tienen JSON importable y sin credenciales.
## Riesgos e incógnitas
- Integración n8n/MCP y estrategia de entrega de correo requieren configuración del trainer.
