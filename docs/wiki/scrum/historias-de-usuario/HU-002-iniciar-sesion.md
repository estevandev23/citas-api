---
id: HU-002
tipo: historia-de-usuario
titulo: "Iniciar sesión"
estado: Completada
epica: "[[EP-001-identidad-y-acceso]]"
esfuerzo: Medio
sprint_sugerido: "Sprint 1"
dependencias: ["[[HU-001-registrar-usuario]]"]
relacionadas: ["[[HU-003-renovar-y-cerrar-sesion]]"]
---
# HU-002 — Iniciar sesión
## Historia de usuario
**COMO** usuario registrado **QUIERO** iniciar sesión con email y contraseña **PARA** acceder a las funciones de mi rol.
## Alcance
- Validación de credenciales y emisión de tokens separados.
## Fuera de alcance
- Renovación, revocación y recuperación.
## Reglas de negocio
- RF-02; roles en contexto de autorización; no exponer password.
## Dependencias y relaciones
- Épica: [[EP-001-identidad-y-acceso]]; depende de [[HU-001-registrar-usuario]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** autenticación y propagación segura de contexto.
## Tareas de desarrollo
- [x] **T-01 — Acordar contrato de login y errores de credenciales.** Dificultad: Medio.
- [x] **T-02 — Implementar autenticación y pantalla de login.** Dificultad: Alto. Ruta Next.js `/iniciar-sesion`, cliente REST y sesión en memoria integrados.
- [x] **T-03 — Probar credenciales válidas e inválidas en API.** Dificultad: Medio.
## Criterios de aceptación
### CA-01 — Credenciales válidas
**Dado** un usuario activo con credenciales válidas, **cuando** inicia sesión, **entonces** recibe contexto autenticado y tokens access/refresh separados.
### CA-02 — Credenciales inválidas
**Dado** credenciales inválidas, **cuando** intenta ingresar, **entonces** recibe un error sin revelar cuál dato falló.
### CA-03 — Rol aplicado
**Dado** una sesión iniciada, **cuando** accede a una capacidad protegida, **entonces** el rol participa en la autorización.
## Definition of Done
- [x] CA-01 a CA-03 validados con evidencia.
- [x] Tokens no quedan hardcodeados ni expuestos en logs.
- [x] Manejo UI de loading/error y pruebas aplicables disponibles.
- [x] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 — Credenciales válidas | Cumple | `AuthController#login`; `AuthFlowTest.loginRefreshAndProtectedResourceAreSeparated` | Emite access/refresh diferenciados. |
| CA-02 — Credenciales inválidas | Cumple | `IdentityService#login`; prueba de flujo de autenticación | Devuelve `401 INVALID_CREDENTIALS` sin distinguir el campo incorrecto. |
| CA-03 — Rol aplicado | Cumple | `SecurityConfiguration`; `AuthController#me`; `AuthFlowTest` | El access con rol USER accede a `/api/v1/session/me`; refresh no puede hacerlo. |
| DoD — Tokens sin hardcode/log | Cumple | Configuración por variables de entorno; DTO de respuesta | No hay secretos/token estáticos en código ni logging de tokens. |
| DoD — UI loading/error y pruebas aplicables | Cumple | `citas-web/features/auth/login-form.tsx`; revisión visual de `/iniciar-sesion`; `npm test` | Campos inválidos, error genérico de credenciales, estado enviando/deshabilitado y redirección tras sesión válida. |
| DoD — Trazabilidad | Cumple | Esta matriz e historial | Evidencia vinculada. |
| Integración CORS/UI | Cumple | API local: `cors_origin=http://localhost:15174`, registro `201`, login `200`, sesión `200` | CORS y URL de API verificadas contra el grupo Docker `citas-brayan`. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
- 2026-09-22 — Pasó a `En validación`. Backend y pruebas cumplen; no se cierra hasta completar la pantalla y estados UI.
- 2026-09-22 — Se verificaron la pantalla, errores accesibles, build/typecheck/lint, pruebas y recorrido REST/CORS local. Estado final: `Completada`.
