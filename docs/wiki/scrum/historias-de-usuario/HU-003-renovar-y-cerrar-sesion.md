---
id: HU-003
tipo: historia-de-usuario
titulo: "Renovar y cerrar sesión"
estado: En validación
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
- [ ] **T-01 — Definir contrato de refresh/logout y manejo de sesión expirada.** Dificultad: Medio. Refresh documentado; logout aún no tiene contrato.
- [ ] **T-02 — Implementar flujo de renovación/cierre en API y cliente.** Dificultad: Alto. Rotación refresh implementada en API; logout y cliente pendientes.
- [x] **T-03 — Probar refresh vencido, consumido y renovación válida.** Dificultad: Medio.
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
| CA-01 — Renovación válida | Cumple | `IdentityService#refresh`; `AuthFlowTest.loginRefreshAndProtectedResourceAreSeparated` | Rota access/refresh sin contraseña. |
| CA-02 — Cierre | No cumple | No existe `POST /logout` ni cliente que limpie estado | Contrato y revocación explícita pendientes. |
| CA-03 — Refresh inválido | Cumple | `AuthFlowTest` y `expiredRefreshIsRejected` | Refresh usado, access presentado como refresh y refresh vencido retornan `401`. |
| DoD — Cliente limpia estado | No cumple | No hay aplicación `citas-web` | Pendiente de HU-002/HU-003 frontend. |
| DoD — Pruebas y contrato | No cumple | Pruebas refresh disponibles; falta contrato logout | Falta evidencia del ciclo completo. |
| DoD — Trazabilidad | Cumple | Esta matriz e historial | Evidencia vinculada. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
- 2026-09-22 — Pasó a `En validación`. Se verificó refresh; cierre/logout y cliente mantienen la HU abierta.
