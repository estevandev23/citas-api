# Modelo de datos y 3FN

## Estado

HECHO — los incrementos se materializaron mediante Flyway `V1__identity.sql` a `V5__scheduling.sql`, incluyendo identidad, aseguramiento, catálogos, profesionales, disponibilidad, slots, citas, historial y reprogramaciones.

## Síntesis

El modelo contiene `app_user`, `role`, `user_role`, `refresh_session`, `password_reset_token`, catálogos fijos, EPS/planes/afiliaciones, `specialty`, `professional`, las relaciones `professional_specialty`/`professional_facility`, `availability_block`, `appointment`, `appointment_slot`, `appointment_status_history`, `reschedule_request` y `reschedule_slot`. Mantiene la identidad normalizada: roles y habilitaciones son relaciones N:M, y las reservas usan claves compuestas para impedir doble ocupación.

La referencia `database/reference/db.sql` fue comparada con el modelo Flyway; se conservaron las mismas cardinalidades esenciales sin copiar datos de demostración. Los usuarios de `local` se siembran aparte, son sintéticos e idempotentes.

El diseño debe cumplir 3FN: atributos atómicos, relaciones N:M resueltas, dependencias completas en claves compuestas y ausencia de dependencias transitivas. No se deben duplicar nombres de EPS, plan, régimen o especialidad cuando existen catálogos.

La reserva de slots se protege con transacción y clave primaria `(professional_id, slot_start)`; una especialidad de 60 minutos exige dos slots consecutivos. La cita original se conserva mientras una reprogramación está `PENDING`; la decisión mueve o libera las reservas correspondientes. Las citas almacenan FKs a catálogos y el historial registra actor, fuente, fecha y motivo.

## Evidencia

- [Requisitos de normalización 3FN](../raw/2026-09-15-requisitos-normalizacion-3fn.md)
- [PRD 1.0](../raw/2026-09-15-prd-v1.md)
- [Migración de identidad](../../../../src/main/resources/db/migration/V1__identity.sql)
- [Migración de recuperación](../../../../src/main/resources/db/migration/V2__password_reset_tokens.sql)
- [Migración de catálogos fijos](../../../../src/main/resources/db/migration/V3__fixed_catalogs.sql)
- [Migración de afiliaciones](../../../../src/main/resources/db/migration/V4__user_affiliations.sql)
- [Migración de agenda y citas](../../../../src/main/resources/db/migration/V5__scheduling.sql)

## Enlaces relacionados

- [Dominio](domain.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
- [Contratos REST](rest-contracts.md)
