# Contratos REST

## Estado

IMPLEMENTADO Y RECONCILIADO PARA HU-001 A HU-020 — identidad, afiliación, catálogos, agenda, citas, reprogramaciones y decisiones administrativas integrados con el cliente Next.js y validados en Docker el 2026-09-29.

El perfil y la afiliación cuentan con integración REST verificable en contenedores: lectura/actualización del perfil propio y opciones de afiliación dinámicas. La agenda cubre búsqueda, reserva, cancelación, reprogramación, decisiones administrativas y cierre profesional. No se muestran opciones EPS, planes, especialidades ni profesionales codificados en el cliente.

## Síntesis

La integración es REST/JSON directa entre `citas-web` y `citas-api`. Todo cambio futuro de contrato requiere plan cross-repo, actualización de esta página y evidencia verificable en backend y frontend.

### Identidad v1

| Operación | Solicitud JSON | Éxito | Errores esperados |
|---|---|---|---|
| `POST /api/v1/auth/register` | `firstName`, `lastName`, `documentType`, `documentNumber`, `email`, `phone`, `password` | `201`: `id`, `firstName`, `lastName`, `email`, `roles` | `400 VALIDATION_ERROR` con `fields`; `409 EMAIL_EXISTS` / `DOCUMENT_EXISTS` / `IDENTITY_EXISTS` |
| `POST /api/v1/auth/login` | `email`, `password` | `200`: `accessToken`, `refreshToken`, `tokenType: Bearer` | `400 VALIDATION_ERROR`; `401 INVALID_CREDENTIALS` |
| `POST /api/v1/auth/refresh` | `refreshToken` | `200`: nuevo access y refresh con el mismo formato del login | `400 VALIDATION_ERROR`; `401 INVALID_REFRESH` |
| `POST /api/v1/auth/logout` | `refreshToken` | `204` sin cuerpo | `400 VALIDATION_ERROR` |
| `POST /api/v1/auth/password-recovery` | `email` | `202` sin cuerpo; en perfil `local` configurado, el email existente recibe `resetToken` solo para el laboratorio | `400 VALIDATION_ERROR` |
| `POST /api/v1/auth/password-reset` | `token`, `newPassword` | `204` sin cuerpo | `400 VALIDATION_ERROR` / `INVALID_RESET_TOKEN` |
| `GET /api/v1/session/me` | `Authorization: Bearer <accessToken>` | `200`: `userId`, `roles` | `401` sin access válido; autorización por roles |
| `GET /api/v1/catalogs/fixed` | `Authorization: Bearer <accessToken>` | `200`: `roles`, `appointmentStatuses`, `reschedulingStatuses`, `regimes`, `facilities`; cada valor tiene `code`, `label` | `401` sin access válido; no existen operaciones de escritura (`POST` devuelve `405`) |
| `GET /api/v1/catalogs/affiliation-options` | `Authorization: Bearer <accessToken>` | `200`: `insurers` y `plans`; cada plan incluye `insurerCode`, `code`, `label` | `401` sin access válido |
| `GET /api/v1/profile/me` | `Authorization: Bearer <accessToken>` con rol `USER` | `200`: datos propios permitidos y afiliación actual, si existe | `401`/`403` |
| `PUT /api/v1/profile/me` | `firstName`, `lastName`, `phone`, `insurerCode`, `planCode`, `regimeCode` | `200`: perfil actualizado | `400 INVALID_AFFILIATION` o `VALIDATION_ERROR`; `401`/`403` |

El email se guarda en minúsculas. La unicidad de documento se aplica a la pareja `documentType` + `documentNumber`. El refresh válido rota los dos tokens y consume el refresh anterior. El access token no sirve para refresh ni el refresh para recursos protegidos. La respuesta de registro no incluye contraseña ni hash.

Logout es idempotente: consume el refresh presentado si sigue vigente y responde `204` incluso si ya fue usado o no es válido. El access JWT ya emitido no se revoca y conserva su vigencia corta configurada. El cliente mantiene access/refresh solo en memoria; al salir intenta revocar el refresh y limpia ambos valores aunque la red falle.

La recuperación devuelve `202` para no exponer la existencia de una cuenta. El token tiene vigencia de 15 minutos, se usa una sola vez y, tras el cambio de contraseña, todas las sesiones refresh vigentes del usuario quedan revocadas. Solo el perfil `local`, con exposición habilitada explícitamente, lo retorna en la respuesta controlada; el cliente lo conserva en memoria, nunca lo muestra ni lo persiste. SMTP queda fuera del alcance actual.

El catálogo fijo se consulta bajo autenticación y sus seeds son parte de Flyway V3. No se exponen endpoints CRUD: roles, estados, regímenes y sedes solo pueden leerse. El cliente usa el access token que ya mantiene en memoria y no persiste los resultados.

Los catálogos administrables se exponen bajo `/api/v1/admin/catalogs/{specialties|insurers|plans}` para `ADMIN`, con `GET`, `POST` y `PATCH /{code}/active`; sus relaciones se validan en el backend y los datos se almacenan normalizados.

Los errores de aplicación son `{"code":"...","message":"...","fields":[]}`. El manejo de error de Spring Security para acceso no autorizado sigue su respuesta estándar. URL de API y origen CORS son configurables por ambiente.

La afiliación almacena solamente claves de EPS, plan y régimen; los nombres proceden de sus catálogos normalizados. El `PUT` requiere una EPS y plan activos y que el plan pertenezca a la EPS indicada. CORS permite `PUT` además de los métodos de identidad.

### Agenda, citas y decisiones

| Operación | Resumen |
|---|---|
| `GET /api/v1/specialties`, `GET /api/v1/professionals` | Catálogos activos para búsqueda de disponibilidad. |
| `GET /api/v1/availability?facilityCode&specialtyCode&date&professionalCode` | Devuelve slots consecutivos de 30/60 minutos sin reservas. |
| `POST /api/v1/professional/availability` / `PATCH .../{blockId}` / `DELETE .../{blockId}` | PROFESSIONAL crea, edita y elimina bloques futuros en sedes asignadas; solapes y bloques comprometidos se rechazan. |
| `POST /api/v1/appointments` | USER crea cita general `APPROVED` o especializada `REQUESTED`; la reserva de slots es transaccional. |
| `GET /api/v1/appointments/me`, `POST .../{id}/cancel`, `GET .../{id}/history` | USER consulta, cancela una cita futura y visualiza auditoría. |
| `POST /api/v1/admin/appointments/{id}/decision` | ADMIN aprueba/rechaza solicitudes; el rechazo exige `reason` y libera slots. |
| `POST /api/v1/appointments/{id}/reschedules` | USER retiene una nueva franja sin destruir la original. |
| `POST /api/v1/admin/reschedules/{id}/decision` | ADMIN aprueba/rechaza; al aprobar mueve slots y al rechazar libera la reserva provisional. |
| `POST /api/v1/professional/appointments/{id}/close?status=COMPLETED\|NO_SHOW` | PROFESSIONAL cierra una cita pasada y registra auditoría. |

El bootstrap administrativo usa `POST /api/v1/auth/bootstrap-admin` con `X-Bootstrap-Token`, habilitado únicamente cuando `ADMIN_BOOTSTRAP_TOKEN` está configurado fuera del código. No se almacenan tokens de bootstrap ni credenciales en el repositorio.

## Evidencia

- [PRD 1.0](../raw/2026-09-15-prd-v1.md)
- [Restricciones técnicas](../raw/2026-09-15-restricciones-tecnicas.md)

## Enlaces relacionados

- [Arquitectura](architecture.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
