# Mapa Scrum / Spec-Driven Development — Sistema ficticio de citas

## Estado

Plan inicial generado el 2026-09-17 a partir de `PRD.md`, `RESTRICCIONES_TECNICAS.md` y `database/REQUISITOS_NORMALIZACION_3FN.md`. Las HU-001 a HU-020 tienen implementación y evidencia Docker; HU-021 a HU-023 tienen JSON n8n preparado sin credenciales y quedan listas para activación en la instancia del trainer.

## Épicas

- [[EP-001-identidad-y-acceso]] — registro, autenticación, recuperación y navegación por rol.
- [[EP-002-perfil-y-catalogos]] — perfil, afiliación y catálogos fijos/configurables.
- [[EP-003-gestion-de-profesionales]] — alta, asignación y activación de profesionales.
- [[EP-004-agenda-y-disponibilidad]] — bloques de agenda y consulta de slots reservables.
- [[EP-005-ciclo-de-vida-de-citas]] — creación, consulta, cancelación, agenda y cierre.
- [[EP-006-reprogramacion-y-decisiones-administrativas]] — reprogramación y decisiones ADMIN.
- [[EP-007-automatizacion-operativa]] — automatizaciones posteriores sin alterar el núcleo funcional.

## Incrementos sugeridos

| Incremento | Resultado funcional verificable | HU propuestas |
|---|---|---|
| Sprint 1 | Identidad y acceso seguro para USER | HU-001 a HU-004 |
| Sprint 2 | Perfil, catálogos y configuración de profesionales | HU-005 a HU-010 |
| Sprint 3 | Agenda publicada y disponibilidad consultable | HU-011 a HU-012 |
| Sprint 4 | Creación, consulta, cancelación y operación profesional de citas | HU-013 a HU-017 |
| Sprint 5 | Decisiones administrativas y reprogramación | HU-018 a HU-020 |
| Sprint 6 | Automatizaciones posteriores de laboratorio | HU-021 a HU-023 |

Los incrementos no expresan duración, capacidad ni estimaciones temporales. Cada HU debe aprobarse individualmente antes de desarrollo.

## Trazabilidad por fuentes

- RF-01 a RF-03, RF-20 y seguridad: [[EP-001-identidad-y-acceso]].
- RF-04 a RF-06 y 3FN de catálogos/afiliación: [[EP-002-perfil-y-catalogos]].
- RF-07: [[EP-003-gestion-de-profesionales]].
- RF-08 a RF-10: [[EP-004-agenda-y-disponibilidad]].
- RF-11, RF-13, RF-14, RF-16, RF-17 y RF-19: [[EP-005-ciclo-de-vida-de-citas]].
- RF-12, RF-15 y RF-18: [[EP-006-reprogramacion-y-decisiones-administrativas]].
- PRD §10: [[EP-007-automatizacion-operativa]].

## Decisiones pendientes antes de desarrollo de HU dependientes

- Activación controlada de Gmail/MCP en la instancia n8n del trainer.
- Publicación de `main` tras revisión final; `develop` conserva la trazabilidad de trabajo.

## Regla de aprobación y evidencia

Una HU solo pasa de `Borrador` a `Aprobada` mediante confirmación explícita del usuario. Antes de cerrar una HU, su matriz de evidencia debe demostrar cada CA y cada ítem aplicable de DoD.
