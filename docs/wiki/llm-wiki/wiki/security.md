# Seguridad y datos

## Estado

HECHO — requisitos mínimos del PRD y restricciones técnicas.

## Síntesis

Las contraseñas usan hash adaptativo compatible con Spring Security. Los secretos solo viven en variables de entorno o `.env`, que nunca se versionan. Access y refresh JWT son distintos; debe haber autorización por rol y ownership, CORS explícito y validación server-side.

No se deben registrar passwords o tokens. Los datos de pacientes, profesionales, credenciales, EPS, planes, horarios y citas son sintéticos, salvo referencias públicas incluidas expresamente en requisitos.

## Evidencia

- [PRD 1.0](../raw/2026-09-15-prd-v1.md)
- [Restricciones técnicas](../raw/2026-09-15-restricciones-tecnicas.md)

## Enlaces relacionados

- [Arquitectura](architecture.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
