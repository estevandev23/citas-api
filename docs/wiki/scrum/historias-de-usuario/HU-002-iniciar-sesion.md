---
id: HU-002
tipo: historia-de-usuario
titulo: "Iniciar sesión"
estado: Borrador
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
- [ ] **T-01 — Acordar contrato de login y errores de credenciales.** Dificultad: Medio.
- [ ] **T-02 — Implementar autenticación y pantalla de login.** Dificultad: Alto.
- [ ] **T-03 — Probar credenciales válidas e inválidas.** Dificultad: Medio.
## Criterios de aceptación
### CA-01 — Credenciales válidas
**Dado** un usuario activo con credenciales válidas, **cuando** inicia sesión, **entonces** recibe contexto autenticado y tokens access/refresh separados.
### CA-02 — Credenciales inválidas
**Dado** credenciales inválidas, **cuando** intenta ingresar, **entonces** recibe un error sin revelar cuál dato falló.
### CA-03 — Rol aplicado
**Dado** una sesión iniciada, **cuando** accede a una capacidad protegida, **entonces** el rol participa en la autorización.
## Definition of Done
- [ ] CA-01 a CA-03 validados con evidencia.
- [ ] Tokens no quedan hardcodeados ni expuestos en logs.
- [ ] Manejo UI de loading/error y pruebas aplicables disponibles.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01, CA-02, CA-03, DoD | Pendiente | — | HU en Borrador. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
