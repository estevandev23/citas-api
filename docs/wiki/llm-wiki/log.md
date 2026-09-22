# Registro de la LLM Wiki

| Fecha | Operación | Alcance | Resultado |
| --- | --- | --- | --- |
| 2026-09-15 | INGEST | Fuentes iniciales aprobadas del workspace y repositorios | Se crearon RAW inmutables, esquema operativo, síntesis WIKI e índice inicial. Sin código ni contratos REST implementados. |
| 2026-09-15 | LEARN | Instrucción de orquestación aprobada | Se registró DEC-001: `citas-web` usará Next.js + TypeScript. Las ambigüedades funcionales y de contrato permanecen como preguntas abiertas. |
| 2026-09-15 | LINT | Estructura inicial | Se detectó y documentó la divergencia React/Angular versus Next.js; resuelta por DEC-001. No se ingirieron secretos, `.env` ni modelo de referencia del trainer. |
| 2026-09-15 | INGEST | Especificaciones WF-001, WF-002 y WF-003 | Se incorporaron como requisitos futuros de automatización; no se crearon ni modificaron workflows JSON. |
| 2026-09-15 | LINT | Enlaces Markdown de la wiki | Se corrigieron rutas relativas entre `wiki/`, `raw/` y `schema/`; el contenido de plantilla se dejó como texto no enlazable. |
| 2026-09-22 | LEARN | Registro, login y refresh JWT | Se documentó el contrato REST implementado y la rotación de refresh, con pruebas backend; la reconciliación frontend sigue pendiente. |
| 2026-09-22 | INGEST | Referencia visual Stitch de autenticación | Se sintetizaron paleta, tipografía, composición y discrepancias en `wiki/visual-identity.md`; se omitieron afirmaciones clínicas no respaldadas por el PRD. |
| 2026-09-22 | LINT | Incremento de identidad | `mvn test` pasó con 4 pruebas; Flyway V1 y registro/login/refresh se probaron además contra MySQL 8.4 aislado con datos sintéticos. La carpeta Stitch sigue presente porque la política del host rechazó su eliminación. |
| 2026-09-22 | LINT | Retiro de referencias Stitch | Tras verificar el destino literal dentro de `citas-web`, se retiró `stitch_citas_brayan/` (40 archivos). La identidad visual quedó sintetizada en `wiki/visual-identity.md`; la entrada anterior conserva el rechazo inicial como historial append-only. |
| 2026-09-22 | QUERY / LINT | `database/reference/db.sql` frente a Flyway V1 | La identidad implementada conserva 3FN y cubre el incremento actual. El esquema de referencia es objetivo para HU posteriores; no se adelantaron tablas, seeds ni datos de demostración sin alcance aprobado. |
| 2026-09-22 | LEARN / LINT | Reconciliación frontend de HU-001/HU-002 | Next.js implementa `/registro`, `/iniciar-sesion` y una vista protegida mínima; cliente REST directo, tokens solo en memoria y CORS local verificados. Typecheck, pruebas, lint y build pasaron; logout sigue abierto por falta de contrato backend. |
