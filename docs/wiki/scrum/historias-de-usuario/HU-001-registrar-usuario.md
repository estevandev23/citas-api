---
id: HU-001
tipo: historia-de-usuario
titulo: "Registrar usuario"
estado: Completada
epica: "[[EP-001-identidad-y-acceso]]"
esfuerzo: Medio
sprint_sugerido: "Sprint 1"
dependencias: []
relacionadas: ["[[HU-002-iniciar-sesion]]"]
---
# HU-001 — Registrar usuario
## Historia de usuario
**COMO** visitante **QUIERO** crear una cuenta USER con mis datos mínimos **PARA** acceder al agendamiento.
## Alcance
- Registro con nombres, apellidos, documento, email, teléfono y contraseña.
## Fuera de alcance
- Creación de PROFESSIONAL o ADMIN.
## Reglas de negocio
- RF-01; email y documento únicos; password nunca en texto plano.
## Dependencias y relaciones
- Épica: [[EP-001-identidad-y-acceso]]; relacionada: [[HU-002-iniciar-sesion]].
## Esfuerzo
**Nivel:** Medio. **Justificación:** datos sensibles, validaciones y persistencia segura.
## Tareas de desarrollo
- [x] **T-01 — Definir contrato y validaciones de registro.** Dificultad: Medio.
- [x] **T-02 — Implementar registro seguro y pantalla/formulario.** Dificultad: Alto. API y ruta Next.js `/registro` integradas con el contrato REST.
- [x] **T-03 — Crear pruebas relevantes de unicidad y validación.** Dificultad: Medio.
## Criterios de aceptación
### CA-01 — Registro válido
**Dado** un visitante con datos mínimos válidos, **cuando** confirma el registro, **entonces** obtiene una cuenta USER sin exponer su contraseña.
### CA-02 — Unicidad
**Dado** email o documento existente, **cuando** intenta registrarse, **entonces** recibe un error verificable y no se crea otra cuenta.
### CA-03 — Datos inválidos
**Dado** datos obligatorios faltantes o inválidos, **cuando** envía el formulario, **entonces** se señalan y no se persiste la cuenta.
## Definition of Done
- [x] CA-01 a CA-03 validados con evidencia.
- [x] Password tratado con hash adaptativo y sin registro en logs/UI.
- [x] Contrato REST y pruebas aplicables documentados.
- [x] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 — Registro válido | Cumple | `AuthController#register`, `IdentityService#register`; `AuthFlowTest.registrationIsUniqueAndDoesNotExposePassword` | Devuelve `201`, rol USER y no incluye contraseña/hash. |
| CA-02 — Unicidad | Cumple | Restricciones `uq_app_user_email` y `uq_app_user_document`; misma prueba | Rechaza email/documento duplicado con `409`. |
| CA-03 — Datos inválidos | Cumple | Bean Validation en `RegisterRequest`; `AuthFlowTest.invalidRegistrationDoesNotPersist` | Respuesta `400` con campos inválidos. |
| DoD — Hash y no exposición | Cumple | `BcryptPasswordAdapter`; `AuthFlowTest.registrationIsUniqueAndDoesNotExposePassword` | La contraseña se almacena como hash BCrypt y no forma parte del DTO. |
| DoD — Contrato y pruebas | Cumple | `llm-wiki/wiki/rest-contracts.md`; `AuthFlowTest` | Contrato documentado y `mvn test` aislado: 4 pruebas correctas. |
| DoD — Trazabilidad | Cumple | Esta matriz e historial | Evidencia vinculada a HU y épica. |
| UI de registro | Cumple | `citas-web/app/registro/page.tsx`, `features/auth/registration-form.tsx`; revisión visual local | Formulario accesible con estados cliente/API, submit deshabilitado y éxito sin mostrar ni persistir contraseña. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
- 2026-09-22 — Pasó a `En validación`; todos los CA y DoD aplicables cuentan con evidencia. Estado final: `Completada`.
- 2026-09-22 — Se incorporó y verificó visualmente la ruta `/registro`; se mantiene el cierre con evidencia adicional frontend.
