---
id: HU-006
tipo: historia-de-usuario
titulo: "Consultar catálogos fijos"
estado: Completada
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
- [x] **T-01 — Definir fuentes/contrato de consulta y seed.** Dificultad: Medio. `GET /api/v1/catalogs/fixed` y Flyway V3.
- [x] **T-02 — Implementar consumo de solo lectura y pruebas.** Dificultad: Bajo. Selector de sedes consume REST autenticado.
## Criterios de aceptación
### CA-01 — Disponibilidad
**Dado** una pantalla que requiere un catálogo fijo, **cuando** lo consulta, **entonces** obtiene valores precargados consistentes.
### CA-02 — Solo lectura
**Dado** un actor ADMIN, **cuando** intenta gestionar un catálogo fijo, **entonces** no dispone de CRUD que lo altere.
## Definition of Done
- [x] CA-01 y CA-02 validados.
- [x] Seeds/migración aplicable y pruebas documentadas.
- [x] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia |
|---|---|---|
| CA-01 — Disponibilidad | Cumple | `GET /api/v1/catalogs/fixed` devuelve los cinco grupos con JWT; frontend consume `facilities` en el selector de sede. |
| CA-02 — Solo lectura | Cumple | No hay controlador de escritura; `POST /api/v1/catalogs/fixed` autenticado devuelve `405`. |
| Flyway y Docker | Cumple | V3 aplicada sobre MySQL 8.4; recorrido real: `201` registro, `200` catálogo, `401` sin JWT, `405` escritura y CORS para `http://localhost:5174`. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
- 2026-09-24 — Aprobada por solicitud explícita para iniciar la implementación de RF-05.
- 2026-09-24 — Completada: Maven, frontend y recorrido Docker/MySQL validados con datos sintéticos.
