# Decisiones

## DEC-001 — Frontend en Next.js + TypeScript

**Estado:** aprobada el 2026-09-15.

**Contexto:** Las fuentes iniciales permiten React o Angular después de Stitch/Google AI Studio. La instrucción de orquestación del workspace establece que `citas-web` será Next.js.

**Decisión:** Implementar el frontend en Next.js con TypeScript, manteniendo consumo REST directo de `citas-api` y sin BFF/Express.

**Consecuencia:** Las instrucciones locales de `citas-web` se generarán/depurarán después de importar el frontend real, sin volver a abrir la elección de framework salvo nueva aprobación explícita.

## Enlaces relacionados

- [Arquitectura](architecture.md)
- [Preferencias](preferences.md)
