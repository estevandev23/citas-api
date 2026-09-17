---
id: HU-005
tipo: historia-de-usuario
titulo: "Gestionar perfil y afiliación"
estado: Borrador
epica: "[[EP-002-perfil-y-catalogos]]"
esfuerzo: Medio
sprint_sugerido: "Sprint 2"
dependencias: ["[[HU-002-iniciar-sesion]]", "[[HU-006-consultar-catalogos-fijos]]"]
---
# HU-005 — Gestionar perfil y afiliación
## Historia de usuario
**COMO** USER autenticado **QUIERO** consultar/actualizar mi perfil y afiliación **PARA** mantener mis datos permitidos vigentes.
## Alcance y reglas
- RF-04; no duplicar EPS, régimen o plan en datos del usuario.
## Dependencias y relaciones
- Épica: [[EP-002-perfil-y-catalogos]]; depende de [[HU-002-iniciar-sesion]] y [[HU-006-consultar-catalogos-fijos]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** ownership, afiliación normalizada y validaciones.
## Tareas de desarrollo
- [ ] **T-01 — Definir datos editables y contrato de perfil/afiliación.** Dificultad: Medio.
- [ ] **T-02 — Implementar UI/API y pruebas de ownership.** Dificultad: Alto.
## Criterios de aceptación
### CA-01 — Consulta propia
**Dado** USER autenticado, **cuando** consulta perfil, **entonces** ve únicamente sus datos permitidos y afiliación actual.
### CA-02 — Actualización válida
**Dado** datos permitidos válidos, **cuando** guarda cambios, **entonces** el perfil/afiliación queda actualizado.
### CA-03 — Integridad de catálogo
**Dado** una afiliación, **cuando** se guarda, **entonces** referencia EPS/plan/régimen sin duplicar sus nombres en el usuario.
## Definition of Done
- [ ] CA-01 a CA-03 validados.
- [ ] Ownership y validación server-side comprobados.
- [ ] Evidencia de 3FN y trazabilidad Scrum actualizadas.
## Evidencia de validación
| Elemento | Resultado | Evidencia |
|---|---|---|
| CA-01 a CA-03 y DoD | Pendiente | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
