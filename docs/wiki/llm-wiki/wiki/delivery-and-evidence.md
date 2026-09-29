# Entrega y evidencia

## Estado

HECHO — expectativas formativas del workspace.

## Síntesis

El progreso debe ser reconstruible por commits trazables en cada repositorio. La evidencia mínima evoluciona de baseline y documentación en S2, a pruebas y hooks en S3, Builder/Verifier y MVP en S4, y workflows n8n versionados y validaciones en S5/S6.

Los workflows se exportan como JSON sin credenciales. La historia Git no debe reescribirse para ocultar progreso.

El 2026-09-24, la suite backend ejecutada en Docker Temurin 21 validó 7 pruebas de identidad, incluyendo logout idempotente, invalidación de refresh y recuperación de contraseña de un uso. La suite frontend en Node 24 validó 5 pruebas; typecheck y build pasaron, y ESLint terminó sin errores con 16 advertencias preexistentes de fuentes/imágenes. El recorrido REST Docker con perfil `local` confirmó registro `201`, login `200`, logout `204`, refresh revocado `401`, recuperación `202`, reset `204`, reutilización `400`, nuevo login `200` y CORS `200`.

También el 2026-09-24, HU-006 añadió Flyway V3 y el contrato autenticado de catálogos fijos. Maven validó 8 pruebas y el frontend 6; typecheck y build pasaron en los contenedores Temurin 21 y Node 24. El recorrido real contra MySQL 8.4 confirmó registro `201`, catálogo `200`, petición sin JWT `401`, escritura `405` y CORS `200` para `http://localhost:5174`.

Como requisitos futuros se han ingestado las especificaciones WF-001 (recordatorios), WF-002 (notificación por cambio de estado) y WF-003 (resumen operativo). No son workflows implementados ni autorizan cambios en el núcleo funcional.

El 2026-09-29, Flyway V5 añadió especialidades, profesionales, bloques de disponibilidad, reservas de slots, estados, auditoría y solicitudes de reprogramación. Maven `test` validó las migraciones en H2 y el cliente Node 24 pasó 7 pruebas, typecheck, lint sin errores y build. En MySQL 8.4 se verificaron recorridos sintéticos de registro/perfil, alta de profesional, disponibilidad, reserva general `APPROVED`, solicitud especializada `REQUESTED` con rechazo y liberación, reprogramación aprobada con cambio de hora e historial. El navegador Docker validó autenticación y renderizado del portal de agenda.

El 2026-09-29, el perfil Docker `local` incorporó un cargador idempotente de datos sintéticos (`TEST_DATA_ENABLED=true`): paciente, profesional, administrador, afiliaciones, disponibilidad para el día siguiente y una cita aprobada. La validación E2E en `localhost:5174` confirmó el acceso del paciente con su cita y del administrador con bandeja y catálogos. El cargador se desactiva fuera de `local` y no se ejecuta contra H2.

## Evidencia

- [Evidencias y trazabilidad](../raw/2026-09-15-evidencias-y-trazabilidad.md)
- [Restricciones técnicas](../raw/2026-09-15-restricciones-tecnicas.md)
- [Especificación WF-001](../raw/2026-09-15-wf-001-appointment-reminders.md)
- [Especificación WF-002](../raw/2026-09-15-wf-002-status-notifications.md)
- [Especificación WF-003](../raw/2026-09-15-wf-003-daily-operational-summary.md)

## Enlaces relacionados

- [Arquitectura](architecture.md)
