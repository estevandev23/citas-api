---
id: HU-007
tipo: historia-de-usuario
titulo: "Administrar EPS y planes"
estado: Borrador
epica: "[[EP-002-perfil-y-catalogos]]"
esfuerzo: Medio
sprint_sugerido: "Sprint 2"
dependencias: ["[[HU-002-iniciar-sesion]]"]
---
# HU-007 — Administrar EPS y planes
## Historia de usuario
**COMO** ADMIN **QUIERO** gestionar EPS y sus planes **PARA** mantener afiliaciones configurables.
## Alcance y reglas
- RF-06; no borrar físicamente catálogos referenciados, desactivar cuando aplique.
## Dependencias y relaciones
- Épica: [[EP-002-perfil-y-catalogos]]; depende de [[HU-002-iniciar-sesion]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** relación EPS-plan, autorización e integridad referencial.
## Tareas de desarrollo
- [ ] **T-01 — Definir contrato, restricciones de referencia y UI CRUD.** Dificultad: Medio.
- [ ] **T-02 — Implementar gestión, estados y pruebas.** Dificultad: Alto.
## Criterios de aceptación
### CA-01 — CRUD autorizado
**Dado** ADMIN autenticado, **cuando** gestiona EPS/planes válidos, **entonces** los cambios quedan disponibles para afiliación.
### CA-02 — Protección de referencias
**Dado** un catálogo referenciado, **cuando** se intenta eliminar, **entonces** no se borra físicamente y se aplica la alternativa aprobada.
## Definition of Done
- [ ] CA-01 y CA-02 validados; autorización ADMIN y migración aplicable verificadas.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia |
|---|---|---|
| CA-01, CA-02 y DoD | Pendiente | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
