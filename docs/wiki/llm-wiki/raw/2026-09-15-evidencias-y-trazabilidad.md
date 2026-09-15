# Evidencias y trazabilidad para evaluación final

La calificación ocurre al finalizar S6 y el historial debe reconstruir el progreso.

| Sesión | Evidencia mínima |
| --- | --- |
| S2 | Commit backend y frontend; AGENTS; Scrum specs; baseline ejecutable. |
| S3 | Commits; tests; hook FAIL/PASS; secreto ficticio bloqueado. |
| S4 | Commits; logs Builder/Verifier; goal/loop; MVP. |
| S5 | Commit; WF-001 JSON; evidencia MCP; riesgos residuales. |
| S6 | Commit final; WF-002 JSON; validaciones; merge/main estable; sustentación. |

Desarrollo en `develop`; `main` solo contiene puntos estables; no realizar squash/rebase destructivo que borre progreso. Por evidencia se registra sesión, repo, rama, commit, HU, criterios, pruebas, pendientes y evidencia adicional.

Los JSON n8n deben importarse sin secretos embebidos; las credenciales se configuran en n8n, fuera del repositorio.
