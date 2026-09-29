# Evidencia del gate de secretos

La prueba sintética se ejecuta con:

```powershell
.\scripts\secret-hook-smoke-test.ps1
```

Resultado esperado y validado el 2026-09-29:

```text
PASS: secreto sintético bloqueado; placeholder documentado permitido.
```

El hook real además bloquea archivos `.env`, `.pem` y `.key`, y ejecuta las suites del repositorio. Los placeholders de `.env.example` se permiten porque no son secretos operativos.
