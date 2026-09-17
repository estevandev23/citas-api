---
id: HU-006
tipo: historia-de-usuario
titulo: "Consultar catálogos fijos"
estado: Borrador
epica: "[[EP-002-perfil-y-catalogos]]"
esfuerzo: Bajo
sprint_sugerido: "Sprint 2"
dependencias: []
---
# HU-006 — Consultar catálogos fijos
## Historia de usuario
**COMO** usuario de la aplicación **QUIERO** consultar catálogos fijos **PARA** seleccionar valores consistentes.
## Alcance y reglas
- RF-05: roles, estados de cita/reprogramación, regímenes y sedes precargados y solo lectura.
## Dependencias y relaciones
- Épica: [[EP-002-perfil-y-catalogos]]; relacionada: [[HU-005-gestionar-perfil-y-afiliacion]].
## Esfuerzo
**Nivel:** Bajo. **Justificación:** catálogo de lectura, seed y consumo controlado.
## Tareas de desarrollo
- [ ] **T-01 — Definir fuentes/contrato de consulta y seed.** Dificultad: Medio.
- [ ] **T-02 — Implementar consumo de solo lectura y pruebas.** Dificultad: Bajo.
## Criterios de aceptación
### CA-01 — Disponibilidad
**Dado** una pantalla que requiere un catálogo fijo, **cuando** lo consulta, **entonces** obtiene valores precargados consistentes.
### CA-02 — Solo lectura
**Dado** un actor ADMIN, **cuando** intenta gestionar un catálogo fijo, **entonces** no dispone de CRUD que lo altere.
## Definition of Done
- [ ] CA-01 y CA-02 validados.
- [ ] Seeds/migración aplicable y pruebas documentadas.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia |
|---|---|---|
| CA-01, CA-02 y DoD | Pendiente | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
