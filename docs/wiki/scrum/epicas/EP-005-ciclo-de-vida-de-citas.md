---
id: EP-005
tipo: epica
titulo: "Ciclo de vida de citas"
estado: Borrador
historias: ["[[HU-013-reservar-cita-general]]", "[[HU-014-solicitar-cita-especializada]]", "[[HU-015-consultar-mis-citas]]", "[[HU-016-cancelar-cita]]", "[[HU-017-consultar-y-cerrar-agenda-profesional]]"]
dependencias: ["[[EP-004-agenda-y-disponibilidad]]"]
---
# EP-005 — Ciclo de vida de citas
## Objetivo
Permitir crear, consultar, cancelar y cerrar citas respetando sus estados y auditoría.
## Valor esperado
Una experiencia de cita trazable para USER y PROFESSIONAL.
## Actores
- USER; PROFESSIONAL.
## Alcance
- Cita general/especializada, mis citas, cancelación, agenda profesional, COMPLETED y NO_SHOW.
## Fuera de alcance
- Reprogramación y decisión ADMIN.
## Reglas de negocio
- General auto-aprobada; especializada retenida; cancelación libera slots; toda transición auditable.
## Dependencias
- [[EP-004-agenda-y-disponibilidad]]
## Historias de usuario
- [[HU-013-reservar-cita-general]]
- [[HU-014-solicitar-cita-especializada]]
- [[HU-015-consultar-mis-citas]]
- [[HU-016-cancelar-cita]]
- [[HU-017-consultar-y-cerrar-agenda-profesional]]
## Criterio de completitud de la épica
- [ ] Todas las HU están `Completada`.
- [ ] Las transiciones incluidas tienen historial verificable.
## Riesgos e incógnitas
- Falta máquina de estados completa y política de concurrencia/retención.
