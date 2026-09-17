---
id: HU-003
tipo: historia-de-usuario
titulo: "Renovar y cerrar sesión"
estado: Borrador
epica: "[[EP-001-identidad-y-acceso]]"
esfuerzo: Medio
sprint_sugerido: "Sprint 1"
dependencias: ["[[HU-002-iniciar-sesion]]"]
relacionadas: []
---
# HU-003 — Renovar y cerrar sesión
## Historia de usuario
**COMO** usuario autenticado **QUIERO** renovar una sesión válida y cerrarla **PARA** mantener y terminar mi acceso de forma segura.
## Alcance
- Refresh y revocación/logout.
## Fuera de alcance
- Cambio de contraseña.
## Reglas de negocio
- RF-02; access y refresh separados; logout revoca la sesión según contrato aprobado.
## Dependencias y relaciones
- Épica: [[EP-001-identidad-y-acceso]]; depende de [[HU-002-iniciar-sesion]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** ciclo de tokens y coherencia cliente-servidor.
## Tareas de desarrollo
- [ ] **T-01 — Definir contrato de refresh/logout y manejo de sesión expirada.** Dificultad: Medio.
- [ ] **T-02 — Implementar flujo de renovación/cierre en API y cliente.** Dificultad: Alto.
- [ ] **T-03 — Probar token revocado, vencido y renovación válida.** Dificultad: Medio.
## Criterios de aceptación
### CA-01 — Renovación válida
**Dado** un refresh válido, **cuando** se renueva la sesión, **entonces** se obtiene acceso renovado sin usar la contraseña.
### CA-02 — Cierre
**Dado** una sesión activa, **cuando** el usuario cierra sesión, **entonces** sus credenciales de sesión dejan de permitir acceso según contrato.
### CA-03 — Refresh inválido
**Dado** un refresh inválido o revocado, **cuando** se intenta renovar, **entonces** el acceso se rechaza y el cliente vuelve a estado no autenticado.
## Definition of Done
- [ ] CA-01 a CA-03 validados.
- [ ] El cliente limpia estado sensible de forma segura al cerrar o expirar.
- [ ] Pruebas de ciclo de sesión y contrato documentadas.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01, CA-02, CA-03, DoD | Pendiente | — | HU en Borrador. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
