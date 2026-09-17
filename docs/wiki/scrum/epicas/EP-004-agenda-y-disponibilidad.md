---
id: EP-004
tipo: epica
titulo: "Agenda y disponibilidad"
estado: Borrador
historias: ["[[HU-011-gestionar-bloques-de-agenda]]", "[[HU-012-consultar-disponibilidad]]"]
dependencias: ["[[EP-003-gestion-de-profesionales]]"]
---
# EP-004 — Agenda y disponibilidad
## Objetivo
Publicar agenda profesional válida y mostrar franjas reservables.
## Valor esperado
El usuario elige horarios basados en disponibilidad real.
## Actores
- PROFESSIONAL; USER.
## Alcance
- Bloques futuros por sede y consulta filtrable de slots de 30/60 minutos.
## Fuera de alcance
- Reserva de cita y decisiones administrativas.
## Reglas de negocio
- Sin pasado ni solapamientos; sede habilitada; slots de 30 min; 60 min exige consecutividad.
## Dependencias
- [[EP-003-gestion-de-profesionales]]
## Historias de usuario
- [[HU-011-gestionar-bloques-de-agenda]]
- [[HU-012-consultar-disponibilidad]]
## Criterio de completitud de la épica
- [ ] Todas las HU están `Completada`.
- [ ] Las reglas RN-05 a RN-08 aplicables tienen evidencia.
## Riesgos e incógnitas
- Cita comprometida, zona horaria y concurrencia pendientes.
