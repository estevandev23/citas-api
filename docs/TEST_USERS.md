# Usuarios sintéticos de prueba

Al iniciar `citas-api` con el perfil `local` (configuración por defecto de `docker compose`), se crean de forma idempotente usuarios y una cita futura para validar los recorridos completos.

| Rol | Correo | Contraseña |
|---|---|---|
| Paciente | `demo.patient@example.test` | `CitasDemo123!` |
| Profesional | `demo.professional@example.test` | `CitasDemo123!` |
| Profesional 2 | `demo.professional2@example.test` | `CitasDemo123!` |
| Administrador | `demo.admin@example.test` | `CitasDemo123!` |

El profesional 1 (`DEMO-PRO`) trabaja en HIC/ICV y tiene una cita de Medicina General; el profesional 2 (`DEMO-PRO-002`) trabaja en ICV y tiene una cita diferente de Medicina Interna. Los registros son ficticios y solo se generan en el perfil `local`. El cargador no reemplaza usuarios existentes; puede ejecutarse varias veces sin duplicar profesionales, bloques ni citas.
