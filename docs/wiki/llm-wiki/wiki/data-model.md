# Modelo de datos y 3FN

## Estado

HECHO — el incremento de identidad se materializó mediante Flyway `V1__identity.sql`. El modelo completo de agenda permanece pendiente de sus HU.

## Síntesis

El incremento actual contiene `app_user`, `role`, `user_role` y `refresh_session`. Mantiene la identidad normalizada: los roles se resuelven mediante una relación N:M y las sesiones refresh se separan de los datos de usuario. El email y el documento compuesto son únicos; la persistencia guarda solamente un hash del identificador del refresh y la rotación marca la sesión anterior como consumida.

La referencia `database/reference/db.sql` confirma el modelo objetivo más amplio: identidad, aseguramiento, catálogos, profesionales, disponibilidad/slots, citas, historial y reprogramaciones. Sus entidades aún no están implementadas porque dependen de HU posteriores; no se copian sus datos de demostración ni se adelantan tablas sin alcance aprobado.

El diseño debe cumplir 3FN: atributos atómicos, relaciones N:M resueltas, dependencias completas en claves compuestas y ausencia de dependencias transitivas. No se deben duplicar nombres de EPS, plan, régimen o especialidad cuando existen catálogos.

Para los próximos incrementos siguen sin aprobación la estrategia de concurrencia/reserva de slots, la política de snapshots y los contratos de catálogos, citas y reprogramación.

## Evidencia

- [Requisitos de normalización 3FN](../raw/2026-09-15-requisitos-normalizacion-3fn.md)
- [PRD 1.0](../raw/2026-09-15-prd-v1.md)
- [Migración de identidad](../../../../src/main/resources/db/migration/V1__identity.sql)

## Enlaces relacionados

- [Dominio](domain.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
- [Contratos REST](rest-contracts.md)
