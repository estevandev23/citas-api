# Dominio de citas

## Estado

HECHO — sintetizado del PRD 1.0 el 2026-09-15.

## Síntesis

El laboratorio implementa un sistema ficticio de agendamiento con tres actores: `USER`, `PROFESSIONAL` y `ADMIN`. Las sedes son un catálogo fijo; los datos personales, profesionales y transaccionales deben ser sintéticos.

El núcleo consiste en disponibilidad por bloques de un profesional habilitado en una sede, discretizada en slots de 30 minutos. Las especialidades duran 30 o 60 minutos y una reserva de 60 minutos requiere dos slots consecutivos. Una cita general se aprueba automáticamente; una especializada inicia como solicitud y requiere decisión administrativa. La cancelación, el rechazo y ciertas decisiones de reprogramación liberan las reservas pertinentes.

Toda transición de cita debe ser explícita y auditable, incluyendo estado nuevo, actor cuando exista, fuente, fecha/hora y motivo opcional.

## Evidencia

- [PRD 1.0](../raw/2026-09-15-prd-v1.md)

## Enlaces relacionados

- [Modelo de datos](data-model.md)
- [Contratos REST](rest-contracts.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
