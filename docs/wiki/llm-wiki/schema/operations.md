# Operaciones de la LLM Wiki

## INGEST

1. Verificar que la fuente está aprobada, es segura y no contiene secretos.
2. Guardar una copia curada en `raw/` sin modificarla posteriormente.
3. Integrar únicamente conocimiento durable en páginas WIKI existentes o nuevas.
4. Añadir enlaces, actualizar `wiki/index.md` si cambia la estructura y registrar la operación en `log.md`.

## QUERY

1. Leer `wiki/index.md`.
2. Consultar páginas pertinentes.
3. Verificar contra RAW, especificación o código cuando aplique.
4. Responder separando evidencia de inferencia.

## LEARN

Después de una interacción sustancial, extraer solo información durable. Clasificarla como HECHO, DECISIÓN, PREFERENCIA o PREGUNTA ABIERTA. Verificar HECHOS antes de persistirlos y registrar el resultado en `log.md`.

## LINT

Revisar contradicciones, claims obsoletos, duplicados, páginas huérfanas, enlaces rotos, decisiones sin aprobación y contenido sensible. Registrar el alcance, hallazgos y correcciones aprobadas en `log.md`.
