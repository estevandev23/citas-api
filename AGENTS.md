# AGENTS.md — `citas-api`

## Estado verificado

- El repositorio está en fase inicial: no hay `pom.xml`, fuentes Java, pruebas ni migraciones.
- La rama de trabajo actual es `develop`; `main` se reserva para puntos estables.
- `docs/wiki/scrum/epicas/` y `docs/wiki/scrum/historias-de-usuario/` no contienen todavía una HU aprobada ni un DoD.
- La única wiki global es `docs/wiki/llm-wiki/` y la mantiene el orquestador. No crear ni actualizar una wiki paralela desde este repositorio.

Antes de inicializar Spring Boot o implementar una funcionalidad, leer:

- `docs/wiki/llm-wiki/raw/2026-09-15-prd-v1.md`
- `docs/wiki/llm-wiki/raw/2026-09-15-restricciones-tecnicas.md`
- La HU aprobada y su DoD cuando estén disponibles en `docs/wiki/scrum/historias-de-usuario/`.

## Alcance técnico confirmado

- Java 21 LTS, Spring Boot 3.5.x y Maven.
- REST/JSON, Spring Security, JWT separados de acceso y actualización, Spring Data JPA, MySQL 8.4 y Flyway.
- El frontend consume REST directamente. Este agente no modifica `citas-web` ni asume detalles de React, Angular o rutas HTTP que no estén aprobados por una HU.

## Arquitectura

- Mantener dominio y aplicación independientes de Spring, JPA y HTTP.
- Colocar las reglas y los casos de uso en aplicación; expresar las dependencias mediante puertos de entrada/salida.
- Tratar REST y persistencia como adaptadores: los controladores traducen HTTP, validan la entrada y delegan, sin concentrar reglas de negocio.
- No fijar nombres de paquetes ni estructura de módulos hasta que exista el proyecto Spring Boot; seguir la estructura real que se introduzca al inicializarlo sin romper estas dependencias.

## Reglas funcionales que deben preservarse

- Diseñar cada cambio contra la HU y el DoD aprobados, identificando RF, RN y contrato REST afectados antes de editar.
- Las reglas críticas del PRD incluyen ausencia de doble reserva, duración de 60 minutos con dos slots consecutivos, prohibición de bloques/citas pasados, autorización por rol/ownership, transiciones de estado explícitas e historial de auditoría.
- Las citas generales se aprueban automáticamente; las especializadas requieren decisión ADMIN y motivo al rechazar. Cancelaciones y rechazos liberan reservas; una reprogramación pendiente conserva la cita original hasta aprobarse.
- Los endpoints, payloads, códigos HTTP y formato de errores siguen abiertos hasta que una HU los apruebe. No inventarlos ni acoplarlos a una pantalla.

## Persistencia y seguridad

- Todo cambio de esquema requiere una migración Flyway y una justificación en la HU o evidencia de entrega.
- Mantener los datos normalizados al menos hasta 3FN; no duplicar atributos de catálogos en entidades dependientes.
- Usar solo variables de entorno para credenciales y secretos. `.env.example` documenta los nombres permitidos y no contiene valores reales.
- Usar hash adaptativo para contraseñas, validar en servidor y configurar CORS explícitamente. Nunca registrar passwords, JWT ni refresh tokens.
- Usar exclusivamente datos sintéticos; no incorporar datos clínicos ni personales reales.

## Forma de trabajo y verificación

1. Localizar HU aprobada y DoD; si faltan, informar el bloqueo y no implementar funcionalidad no especificada.
2. Enumerar reglas, contratos, adaptadores y migraciones afectados, y proponer el plan antes de editar.
3. Implementar el cambio mínimo coherente y pruebas de dominio, aplicación e integración que correspondan.
4. Ejecutar los comandos de Maven realmente disponibles en el proyecto y reportar resultados, junto con lo no verificado.
5. Mantener cambios trazables en `develop`, sin reescritura destructiva de historial ni secretos en Git.

## Evidencia de este archivo

`README.md`; `.env.example`; `docs/wiki/llm-wiki/raw/2026-09-15-prd-v1.md`; `docs/wiki/llm-wiki/raw/2026-09-15-restricciones-tecnicas.md`; `docs/wiki/llm-wiki/raw/2026-09-15-requisitos-normalizacion-3fn.md`; `docs/wiki/llm-wiki/wiki/{architecture,security,rest-contracts,preferences}.md`.
