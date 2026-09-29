# Seguridad y datos

## Estado

HECHO — requisitos mínimos del PRD y restricciones técnicas.

## Síntesis

Las contraseñas usan hash adaptativo compatible con Spring Security. Los secretos solo viven en variables de entorno o `.env`, que nunca se versionan. Access y refresh JWT son distintos; debe haber autorización por rol y ownership, CORS explícito y validación server-side.

No se deben registrar passwords o tokens. Los datos de pacientes, profesionales, credenciales, EPS, planes, horarios y citas son sintéticos, salvo referencias públicas incluidas expresamente en requisitos.

DECISIÓN — el frontend conserva el access solo en memoria/`sessionStorage` durante la vida de la pestaña y el API entrega el refresh en una cookie `HttpOnly`, `SameSite=Lax`, limitada a `/api/v1/auth`. Al iniciar la aplicación valida el access contra `/session/me`; si expiró o no hay almacenamiento disponible, rota el refresh mediante `/auth/refresh` usando credenciales CORS. El cierre explícito revoca el refresh y expira la cookie. En producción se debe habilitar `Secure` sobre HTTPS.

DECISIÓN — para el laboratorio, la recuperación de contraseña puede devolver el token solo si el perfil activo es `local` y se habilita expresamente la exposición. El token es aleatorio, temporal (15 minutos), de uso único y se persiste únicamente como hash. El cliente no lo muestra, registra ni persiste; en perfiles distintos la respuesta es `202` genérica. Un restablecimiento revoca todas las sesiones refresh vigentes.

## Evidencia

- [PRD 1.0](../raw/2026-09-15-prd-v1.md)
- [Restricciones técnicas](../raw/2026-09-15-restricciones-tecnicas.md)

## Enlaces relacionados

- [Arquitectura](architecture.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
