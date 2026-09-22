# Contratos REST

## Estado

IMPLEMENTADO Y RECONCILIADO PARA HU-001/HU-002 — las rutas Next.js `/registro` y `/iniciar-sesion` consumen este contrato directamente, 2026-09-22. Logout y recuperación continúan pendientes de contrato.

## Síntesis

La integración es REST/JSON directa entre `citas-web` y `citas-api`. Todo cambio futuro de contrato requiere plan cross-repo, actualización de esta página y evidencia verificable en backend y frontend.

### Identidad v1

| Operación | Solicitud JSON | Éxito | Errores esperados |
|---|---|---|---|
| `POST /api/v1/auth/register` | `firstName`, `lastName`, `documentType`, `documentNumber`, `email`, `phone`, `password` | `201`: `id`, `firstName`, `lastName`, `email`, `roles` | `400 VALIDATION_ERROR` con `fields`; `409 EMAIL_EXISTS` / `DOCUMENT_EXISTS` / `IDENTITY_EXISTS` |
| `POST /api/v1/auth/login` | `email`, `password` | `200`: `accessToken`, `refreshToken`, `tokenType: Bearer` | `400 VALIDATION_ERROR`; `401 INVALID_CREDENTIALS` |
| `POST /api/v1/auth/refresh` | `refreshToken` | `200`: nuevo access y refresh con el mismo formato del login | `400 VALIDATION_ERROR`; `401 INVALID_REFRESH` |
| `GET /api/v1/session/me` | `Authorization: Bearer <accessToken>` | `200`: `userId`, `roles` | `401` sin access válido; autorización por roles |

El email se guarda en minúsculas. La unicidad de documento se aplica a la pareja `documentType` + `documentNumber`. El refresh válido rota los dos tokens y consume el refresh anterior. El access token no sirve para refresh ni el refresh para recursos protegidos. La respuesta de registro no incluye contraseña ni hash.

Los errores de aplicación son `{"code":"...","message":"...","fields":[]}`. El manejo de error de Spring Security para acceso no autorizado sigue su respuesta estándar. URL de API y origen CORS son configurables por ambiente. El cliente conserva la sesión solo en memoria: no usa `localStorage`, `sessionStorage` ni cookies JavaScript para tokens. No hay contrato de logout ni recuperación en este incremento.

## Evidencia

- [PRD 1.0](../raw/2026-09-15-prd-v1.md)
- [Restricciones técnicas](../raw/2026-09-15-restricciones-tecnicas.md)

## Enlaces relacionados

- [Arquitectura](architecture.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
