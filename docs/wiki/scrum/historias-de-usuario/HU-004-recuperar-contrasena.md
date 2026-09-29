---
id: HU-004
tipo: historia-de-usuario
titulo: "Recuperar contraseña"
estado: Completada
epica: "[[EP-001-identidad-y-acceso]]"
esfuerzo: Medio
sprint_sugerido: "Sprint 1"
dependencias: ["[[HU-001-registrar-usuario]]"]
---
# HU-004 — Recuperar contraseña
## Historia de usuario
**COMO** usuario registrado **QUIERO** recuperar mi contraseña con un token temporal **PARA** restablecer el acceso.
## Alcance y reglas
- RF-03: solicitud, token de un uso y cambio de contraseña; SMTP es opcional.
## Dependencias y relaciones
- Épica: [[EP-001-identidad-y-acceso]]; depende de [[HU-001-registrar-usuario]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** ciclo sensible de token y seguridad.
## Tareas de desarrollo
- [x] **T-01 — Definir entrega segura de desarrollo y contrato.** Dificultad: Alto. Perfil `local` y exposición explícita; perfiles restantes responden genéricamente.
- [x] **T-02 — Implementar solicitud/restablecimiento y pruebas de token.** Dificultad: Alto.
## Criterios de aceptación
### CA-01 — Solicitud segura
**Dado** un email registrado, **cuando** solicita recuperación, **entonces** se crea un token temporal sin exposición insegura.
### CA-02 — Uso único
**Dado** un token válido, **cuando** cambia la contraseña, **entonces** el token queda consumido y la nueva contraseña permite login.
### CA-03 — Rechazo
**Dado** token usado, vencido o inválido, **cuando** intenta usarlo, **entonces** el cambio se rechaza.
## Definition of Done
- [x] CA-01 a CA-03 validados con evidencia.
- [x] Passwords/tokens no aparecen en logs o UI.
- [x] La decisión de exposición de desarrollo está aprobada.
## Evidencia de validación
| Elemento | Resultado | Evidencia |
|---|---|---|
| CA-01 — Solicitud segura | Cumple en prueba backend | `V2__password_reset_tokens.sql`; `AuthFlowTest.passwordResetIsSingleUseAndRevokesExistingRefreshSessions` | Token aleatorio hasheado, 15 minutos; exposición solo local configurada. |
| CA-02 — Uso único | Cumple en prueba backend | `AuthFlowTest.passwordResetIsSingleUseAndRevokesExistingRefreshSessions` | Consume token, cambia hash BCrypt y revoca refresh sessions. |
| CA-03 — Rechazo | Cumple en prueba backend | `AuthFlowTest.expiredOrUnknownPasswordRecoveryTokensAreRejectedWithoutCreatingAUser` | Token vencido o reutilizado retorna `400 INVALID_RESET_TOKEN`. |
| DoD — No exposición insegura | Cumple por revisión y prueba de cliente | `citas-web/components/auth/PasswordRecoveryView.tsx`; `citas-web/tests/api.test.ts` | No hay logs ni persistencia frontend del token. |
| DoD — Evidencia | Cumple | suites backend/frontend, contrato REST y recorrido Docker local | Recuperación `202`, reset `204`, reutilización `400` y nuevo login `200`. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
- 2026-09-24 — Usuario aprobó el alcance S4; se implementó y pasó a `En validación` con suites correctas. Pendiente validación manual Docker local.
- 2026-09-24 — Recorrido REST integrado con perfil `local`, datos sintéticos y CORS confirmado. Estado final: `Completada`.
