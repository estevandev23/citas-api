---
id: EP-002
tipo: epica
titulo: "Perfil y catálogos"
estado: Borrador
historias: ["[[HU-005-gestionar-perfil-y-afiliacion]]", "[[HU-006-consultar-catalogos-fijos]]", "[[HU-007-administrar-eps-y-planes]]", "[[HU-008-administrar-especialidades]]"]
dependencias: ["[[EP-001-identidad-y-acceso]]"]
---
# EP-002 — Perfil y catálogos
## Objetivo
Administrar perfil y catálogos que soportan citas sin duplicar datos.
## Valor esperado
Datos consistentes para usuarios, profesionales y reservas.
## Actores
- USER; ADMIN.
## Alcance
- Perfil/afiliación, catálogos fijos y CRUD de EPS, planes y especialidades.
## Fuera de alcance
- Borrado físico de catálogos referenciados.
## Reglas de negocio
- 3FN; catálogos fijos solo lectura; catálogos referenciados se desactivan.
## Dependencias
- [[EP-001-identidad-y-acceso]]
## Historias de usuario
- [[HU-005-gestionar-perfil-y-afiliacion]]
- [[HU-006-consultar-catalogos-fijos]]
- [[HU-007-administrar-eps-y-planes]]
- [[HU-008-administrar-especialidades]]
## Criterio de completitud de la épica
- [ ] Todas las HU están `Completada`.
- [ ] Las restricciones 3FN aplicables tienen evidencia.
## Riesgos e incógnitas
- Política de snapshots versus FKs pendiente.
