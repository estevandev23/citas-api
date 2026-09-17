---
id: HU-004
tipo: historia-de-usuario
titulo: "Recuperar contraseña"
estado: Borrador
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
- [ ] **T-01 — Definir entrega segura de desarrollo y contrato.** Dificultad: Alto.
- [ ] **T-02 — Implementar solicitud/restablecimiento y pruebas de token.** Dificultad: Alto.
## Criterios de aceptación
### CA-01 — Solicitud segura
**Dado** un email registrado, **cuando** solicita recuperación, **entonces** se crea un token temporal sin exposición insegura.
### CA-02 — Uso único
**Dado** un token válido, **cuando** cambia la contraseña, **entonces** el token queda consumido y la nueva contraseña permite login.
### CA-03 — Rechazo
**Dado** token usado, vencido o inválido, **cuando** intenta usarlo, **entonces** el cambio se rechaza.
## Definition of Done
- [ ] CA-01 a CA-03 validados con evidencia.
- [ ] Passwords/tokens no aparecen en logs o UI.
- [ ] La decisión de exposición de desarrollo está aprobada.
## Evidencia de validación
| Elemento | Resultado | Evidencia |
|---|---|---|
| CA-01 a CA-03 y DoD | Pendiente | — |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
