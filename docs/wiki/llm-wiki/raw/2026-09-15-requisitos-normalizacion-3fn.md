# Actividad — Diseñar y normalizar la base de datos hasta 3FN

## Objetivo

Diseñar el modelo relacional del PRD y justificar su normalización hasta 3FN antes de comparar con la referencia del trainer.

## Capacidades requeridas

Usuarios y múltiples roles; profesionales como usuarios especializados; especialidades N:M; sedes N:M; EPS/régimen/plan/afiliación; bloques de disponibilidad por profesional/sede/fecha; citas de 30/60 minutos; estados e historial; reprogramaciones y estado; access/refresh/password reset compatibles con PRD.

## Forma normal

- **1FN:** atributos atómicos, sin listas en columnas y grupos repetidos separados.
- **2FN:** en PK compuesta, atributos no clave dependen de la clave completa; relaciones N:M resueltas mediante puentes.
- **3FN:** ningún atributo no clave depende transitivamente de otro no clave; no duplicar nombres de EPS/régimen/plan/especialidad desde catálogos; modelar estados coherentemente.

## Decisiones a justificar

Claves y únicos; cardinalidades; catálogos fijos/configurables; prevención de doble reserva; citas de 60 min; preservación de cita original con reprogramación pendiente; auditoría; snapshots/FK; índices de agenda.

## Entregables

Diagrama ER, dependencias funcionales, explicación 1FN→2FN→3FN, SQL inicial propio y comparación posterior con referencia.
