# Convenciones de la LLM Wiki

## Propósito

La wiki concentra conocimiento durable y verificable del workspace. No es un diario de conversación, un archivo de secretos ni una sustitución de las fuentes aprobadas.

## Tipos de conocimiento

- **HECHO:** afirmación respaldada por una fuente RAW, especificación aprobada o código/prueba verificados.
- **DECISIÓN:** elección aprobada con estado, fecha, responsable o fuente de aprobación y consecuencias.
- **PREFERENCIA:** guía de trabajo no funcional que puede cambiar sin alterar el dominio.
- **PREGUNTA ABIERTA:** aspecto no decidido que bloquea o condiciona diseño/implementación.

## Reglas de contenido

- Toda página debe enlazar las fuentes que respaldan sus HECHOS.
- Señalar explícitamente inferencias como tales; no convertirlas en HECHOS.
- No almacenar secretos, tokens, passwords, PII o datos privados de FCV.
- Los nombres de archivos RAW son estables; su contenido no se edita. Una nueva versión se incorpora como nuevo archivo.
- `wiki/log.md` es append-only: nunca se reordena ni se reescribe el historial.

## Enlaces

Usar enlaces Markdown relativos. Las páginas WIKI deben poder encontrarse desde `wiki/index.md`; las fuentes RAW relevantes deben enlazarse desde cada síntesis.
