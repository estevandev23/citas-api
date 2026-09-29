---
id: HU-008
tipo: historia-de-usuario
titulo: "Administrar especialidades"
estado: Completada
epica: "[[EP-002-perfil-y-catalogos]]"
esfuerzo: Medio
sprint_sugerido: "Sprint 2"
dependencias: ["[[HU-002-iniciar-sesion]]"]
---
# HU-008 — Administrar especialidades
## Historia de usuario
**COMO** ADMIN **QUIERO** gestionar especialidades activas y su duración **PARA** habilitar reservas coherentes.
## Alcance y reglas
- RF-06 y RF-09; duración solo 30/60 minutos; catálogo referenciado no se borra físicamente.
## Dependencias y relaciones
- Épica: [[EP-002-perfil-y-catalogos]]; relacionada: [[HU-010-configurar-habilitaciones-profesional]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** catálogo, duración y dependencias de reserva.
## Tareas de desarrollo
- [x] **T-01 — Definir validaciones/contrato de especialidad.** Dificultad: Medio.
- [x] **T-02 — Implementar CRUD, activación y pruebas.** Dificultad: Alto.
## Criterios de aceptación
### CA-01 — Duración válida
**Dado** ADMIN, **cuando** crea/edita especialidad, **entonces** solo puede definir 30 o 60 minutos.
### CA-02 — Activación segura
**Dado** especialidad referenciada, **cuando** deja de ofrecerse, **entonces** se desactiva sin destruir transacciones.
## Definition of Done
- [x] CA-01 y CA-02 validados; migración/pruebas aplicables verificadas.
- [x] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia |
|---|---|---|
| CA-01, CA-02 y DoD | Cumple | `AdminCatalogController`, restricción Flyway V5 de duración 30/60, `StaffSchedulingView`, recorrido E2E administrador |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
- 2026-09-29 — CRUD, activación/desactivación y duración validada en Docker. Estado final: `Completada`.
