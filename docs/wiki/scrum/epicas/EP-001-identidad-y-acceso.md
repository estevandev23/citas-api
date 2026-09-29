---
id: EP-001
tipo: epica
titulo: "Identidad y acceso"
estado: Completada
historias: ["[[HU-001-registrar-usuario]]", "[[HU-002-iniciar-sesion]]", "[[HU-003-renovar-y-cerrar-sesion]]", "[[HU-004-recuperar-contrasena]]"]
dependencias: []
---
# EP-001 — Identidad y acceso
## Objetivo
Permitir a usuarios ficticios crear y recuperar acceso seguro, con contexto de rol.
## Valor esperado
Base de acceso para las capacidades protegidas.
## Actores
- USER; PROFESSIONAL; ADMIN.
## Alcance
- Registro USER, login, access/refresh JWT, logout y recuperación.
## Fuera de alcance
- Proveedores externos de identidad y SMTP obligatorio.
## Reglas de negocio
- Email/documento únicos; password con hash adaptativo; tokens separados; autorización por rol/ownership.
## Dependencias
- Ninguna para HU-001; las demás siguen sus enlaces.
## Historias de usuario
- [[HU-001-registrar-usuario]]
- [[HU-002-iniciar-sesion]]
- [[HU-003-renovar-y-cerrar-sesion]]
- [[HU-004-recuperar-contrasena]]
## Criterio de completitud de la épica
- [x] Todas las HU están `Completada` con evidencia.
- [x] No hay secretos ni tokens expuestos.
## Riesgos e incógnitas
- La custodia actual de access/refresh es solo en memoria; una persistencia mediante cookie HttpOnly requeriría contrato y CORS distintos.
- SMTP queda fuera de alcance. La exposición del token de recuperación queda limitada al perfil `local` configurado.
- Estado al 2026-09-24: las cuatro HU de la épica están completadas; las suites y el recorrido REST Docker pasan con datos sintéticos.
