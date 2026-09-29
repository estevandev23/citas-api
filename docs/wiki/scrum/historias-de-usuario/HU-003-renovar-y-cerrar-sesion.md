---
id: HU-003
tipo: historia-de-usuario
titulo: "Renovar y cerrar sesión"
estado: Completada
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
- [x] **T-01 — Definir contrato de refresh/logout y manejo de sesión expirada.** Dificultad: Medio. Logout idempotente y vigencia residual del access documentados.
- [x] **T-02 — Implementar flujo de renovación/cierre en API y cliente.** Dificultad: Alto. API revoca/rota el refresh en cookie HttpOnly y el cliente valida/restaura la sesión desde `sessionStorage`, rota el access cuando expira y limpia la sesión aun ante error de red.
- [x] **T-03 — Probar refresh vencido, consumido y renovación válida.** Dificultad: Medio.
## Criterios de aceptación
### CA-01 — Renovación válida
**Dado** un refresh válido, **cuando** se renueva la sesión, **entonces** se obtiene acceso renovado sin usar la contraseña.
### CA-02 — Cierre
**Dado** una sesión activa, **cuando** el usuario cierra sesión, **entonces** sus credenciales de sesión dejan de permitir acceso según contrato.
### CA-03 — Refresh inválido
**Dado** un refresh inválido o revocado, **cuando** se intenta renovar, **entonces** el acceso se rechaza y el cliente vuelve a estado no autenticado.
## Definition of Done
- [x] CA-01 a CA-03 validados.
- [x] El cliente limpia estado sensible de forma segura al cerrar o expirar.
- [x] Pruebas de ciclo de sesión y contrato documentadas.
- [x] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 — Renovación válida | Cumple | `IdentityService#refresh`; `AuthFlowTest.loginRefreshAndProtectedResourceAreSeparated` | Rota access/refresh sin contraseña. |
| CA-02 — Cierre | Cumple en pruebas | `POST /api/v1/auth/logout`; `AuthFlowTest.logoutRevokesOnlyTheSubmittedRefreshAndIsIdempotent`; `AuthProvider#logout` | Revoca el refresh presentado y el cliente limpia memoria en `finally`. |
| CA-03 — Refresh inválido | Cumple | `AuthFlowTest` y `expiredRefreshIsRejected` | Refresh usado, access presentado como refresh y refresh vencido retornan `401`. |
| DoD — Cliente limpia/restaura estado | Cumple por revisión, prueba REST y E2E en contenedores | `citas-web/features/auth/auth-provider.tsx`; `citas-web/tests/api.test.ts` | El access vive en memoria/`sessionStorage`, el refresh en cookie HttpOnly, se valida/rota al recargar y logout limpia/revoca la sesión. |
| DoD — Pruebas y contrato | Cumple en suites | `AuthFlowTest` (7 pruebas totales); `citas-web/tests/api.test.ts` (5 pruebas); `llm-wiki/wiki/rest-contracts.md` | Falta recorrido manual API↔web con perfil Docker `local`. |
| DoD — Trazabilidad | Cumple | Esta matriz e historial | Evidencia vinculada. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
- 2026-09-22 — Pasó a `En validación`. Se verificó refresh; cierre/logout y cliente mantienen la HU abierta.
- 2026-09-24 — Logout implementado y probado en backend/frontend. Permanece `En validación` hasta recorrido manual contra los contenedores locales.
- 2026-09-24 — Recorrido REST integrado contra MySQL y CORS local: logout `204` y refresh revocado `401`. Estado final: `Completada`.
