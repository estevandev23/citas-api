# Usuarios sintéticos de prueba

Al iniciar `citas-api` con el perfil `local` (configuración por defecto de `docker compose`), se crean de forma idempotente usuarios y una cita futura para validar los recorridos completos.

| Rol | Correo | Contraseña |
|---|---|---|
| Paciente | `demo.patient@example.test` | `CitasDemo123!` |
| Profesional | `demo.professional@example.test` | `CitasDemo123!` |
| Administrador | `demo.admin@example.test` | `CitasDemo123!` |

Los registros son ficticios y solo se generan en el perfil `local`. El cargador no reemplaza usuarios existentes; puede ejecutarse varias veces sin duplicar profesionales, bloques ni citas.
