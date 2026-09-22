# Seguridad y datos

## Estado

HECHO — requisitos mínimos del PRD y restricciones técnicas.

## Síntesis

Las contraseñas usan hash adaptativo compatible con Spring Security. Los secretos solo viven en variables de entorno o `.env`, que nunca se versionan. Access y refresh JWT son distintos; debe haber autorización por rol y ownership, CORS explícito y validación server-side.

No se deben registrar passwords o tokens. Los datos de pacientes, profesionales, credenciales, EPS, planes, horarios y citas son sintéticos, salvo referencias públicas incluidas expresamente en requisitos.

DECISIÓN — el frontend de autenticación conserva los tokens solo en memoria de la pestaña. Esto evita persistirlos en almacenamiento JavaScript con el contrato actual; recargar vuelve el cliente a estado anónimo. La persistencia segura del refresh requiere una decisión cross-repo y un contrato distinto (por ejemplo, cookie HttpOnly con CORS de credenciales).

## Evidencia

- [PRD 1.0](../raw/2026-09-15-prd-v1.md)
- [Restricciones técnicas](../raw/2026-09-15-restricciones-tecnicas.md)

## Enlaces relacionados

- [Arquitectura](architecture.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
