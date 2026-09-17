---
id: HU-008
tipo: historia-de-usuario
titulo: "Administrar especialidades"
estado: Borrador
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
- [ ] **T-01 — Definir validaciones/contrato de especialidad.** Dificultad: Medio.
- [ ] **T-02 — Implementar CRUD, activación y pruebas.** Dificultad: Alto.
## Criterios de aceptación
### CA-01 — Duración válida
**Dado** ADMIN, **cuando** crea/edita especialidad, **entonces** solo puede definir 30 o 60 minutos.
### CA-02 — Activación segura
**Dado** especialidad referenciada, **cuando** deja de ofrecerse, **entonces** se desactiva sin destruir transacciones.
## Definition of Done
- [ ] CA-01 y CA-02 validados; migración/pruebas aplicables verificadas.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia |
|---|---|---|
| CA-01, CA-02 y DoD | Pendiente | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
