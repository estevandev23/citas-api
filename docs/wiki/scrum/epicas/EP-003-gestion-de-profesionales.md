---
id: EP-003
tipo: epica
titulo: "Gestión de profesionales"
estado: Borrador
historias: ["[[HU-009-registrar-profesional]]", "[[HU-010-configurar-habilitaciones-profesional]]"]
dependencias: ["[[EP-002-perfil-y-catalogos]]"]
---
# EP-003 — Gestión de profesionales
## Objetivo
Permitir a ADMIN crear profesionales ficticios y definir dónde y qué pueden atender.
## Valor esperado
Profesionales correctamente habilitados para agenda y citas.
## Actores
- ADMIN.
## Alcance
- Registro, especialidad primaria, N:M especialidad/sede y activación.
## Fuera de alcance
- Auto-registro y datos reales.
## Reglas de negocio
- Código/matrícula ficticios; una o más especialidades; una primaria; una o ambas sedes.
## Dependencias
- [[EP-002-perfil-y-catalogos]]
## Historias de usuario
- [[HU-009-registrar-profesional]]
- [[HU-010-configurar-habilitaciones-profesional]]
## Criterio de completitud de la épica
- [ ] Todas las HU están `Completada`.
- [ ] Solo profesionales habilitados participan en disponibilidad.
## Riesgos e incógnitas
- Restricciones de cambios con agenda existente pendientes.
