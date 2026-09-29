# Riesgos y preguntas abiertas

## Estado

PREGUNTAS ABIERTAS — únicamente quedan dependencias del entorno externo n8n; las reglas del núcleo funcional ya están implementadas y probadas.

1. Configurar y probar credenciales Gmail/MCP individuales del trainer para activar WF-001/WF-002.
2. El resumen WF-003 usa un endpoint operativo opcional; permanece desactivado como bonus.
3. SMTP para recuperación sigue fuera de alcance; la exposición controlada del token queda limitada al perfil `local` del laboratorio.
4. Los commits S3–S6 deben conservarse separados en `develop` antes del merge final a `main`.

## Evidencia

- [PRD 1.0](../raw/2026-09-15-prd-v1.md)
- [Restricciones técnicas](../raw/2026-09-15-restricciones-tecnicas.md)
- [Requisitos de normalización 3FN](../raw/2026-09-15-requisitos-normalizacion-3fn.md)
- [Auditoría de entrega S2–S6](../../../evidence/GUIA_SESIONES_AUDITORIA.md)

## Enlaces relacionados

- [Contratos REST](rest-contracts.md)
- [Modelo de datos](data-model.md)
- [Seguridad](security.md)
