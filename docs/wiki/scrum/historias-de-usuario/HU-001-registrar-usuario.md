---
id: HU-001
tipo: historia-de-usuario
titulo: "Registrar usuario"
estado: Borrador
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
- [ ] **T-01 — Definir contrato y validaciones de registro.** Dificultad: Medio.
- [ ] **T-02 — Implementar registro seguro y su pantalla/formulario.** Dificultad: Alto.
- [ ] **T-03 — Crear pruebas relevantes de unicidad y validación.** Dificultad: Medio.
## Criterios de aceptación
### CA-01 — Registro válido
**Dado** un visitante con datos mínimos válidos, **cuando** confirma el registro, **entonces** obtiene una cuenta USER sin exponer su contraseña.
### CA-02 — Unicidad
**Dado** email o documento existente, **cuando** intenta registrarse, **entonces** recibe un error verificable y no se crea otra cuenta.
### CA-03 — Datos inválidos
**Dado** datos obligatorios faltantes o inválidos, **cuando** envía el formulario, **entonces** se señalan y no se persiste la cuenta.
## Definition of Done
- [ ] CA-01 a CA-03 validados con evidencia.
- [ ] Password tratado con hash adaptativo y sin registro en logs/UI.
- [ ] Contrato REST y pruebas aplicables documentados.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01, CA-02, CA-03, DoD | Pendiente | — | HU en Borrador. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Borrador`.
