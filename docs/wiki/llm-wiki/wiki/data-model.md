# Modelo de datos y 3FN

## Estado

HECHO — diseño conceptual pendiente de concretarse por HU aprobadas.

## Síntesis

El modelo debe soportar usuarios y roles múltiples; profesionales como usuarios especializados; relaciones N:M de profesional-especialidad y profesional-sede; EPS, plan, régimen y afiliación; bloques de disponibilidad; citas de 30/60 minutos; estados e historial; reprogramaciones; y artefactos compatibles con autenticación y recuperación de contraseña.

El diseño debe cumplir 3FN: atributos atómicos, relaciones N:M resueltas, dependencias completas en claves compuestas y ausencia de dependencias transitivas. No se deben duplicar nombres de EPS, plan, régimen o especialidad cuando existen catálogos.

No se ha aprobado aún una estructura de tablas, una estrategia concreta de concurrencia o una política de snapshots.

## Evidencia

- [Requisitos de normalización 3FN](../raw/2026-09-15-requisitos-normalizacion-3fn.md)
- [PRD 1.0](../raw/2026-09-15-prd-v1.md)

## Enlaces relacionados

- [Dominio](domain.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
