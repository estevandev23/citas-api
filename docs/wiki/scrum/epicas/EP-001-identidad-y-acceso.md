---
id: EP-001
tipo: epica
titulo: "Identidad y acceso"
estado: En desarrollo
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
- [ ] Todas las HU están `Completada` con evidencia.
- [ ] No hay secretos ni tokens expuestos.
## Riesgos e incógnitas
- Recuperación y logout siguen sin contrato REST. La custodia del refresh token en el navegador requiere decisión cross-repo antes de implementar el cliente.
- Estado al 2026-09-22: [[HU-001-registrar-usuario]] está completada; [[HU-002-iniciar-sesion]] y [[HU-003-renovar-y-cerrar-sesion]] permanecen en validación por trabajo frontend/logout pendiente.
