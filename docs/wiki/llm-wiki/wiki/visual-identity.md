# Identidad visual de autenticación

## Estado

HECHO sobre los artefactos Stitch inspeccionados el 2026-09-22. Su aprobación visual final no consta en el repositorio.

## Paleta y composición observadas

- Nombre del sistema de diseño: **Clinical Trust**.
- Tipografía: **Plus Jakarta Sans**, con jerarquía de titulares semibold y texto de cuerpo legible.
- Fondo de las pantallas de registro/login: `#f8f9ff`; tarjetas: `#ffffff`; texto principal: `#0b1c30`.
- Azul institucional oscuro: `#001428`, contenedor `#0f2942`.
- Azul de acción: `#0051d5`, con variante `#316bf3`.
- Campos y superficies suaves: `#eff4ff` y `#e5eeff`; contornos `#c3c6ce`.
- Estado de error del sistema de tokens: `#ba1a1a` sobre `#ffdad6`.
- Esquinas: controles de aproximadamente 8 px, paneles de 12 px y secciones grandes de 16 px.
- Composición: formulario principal y panel lateral informativo en escritorio; una columna en móvil. Campos con etiquetas visibles, estados de foco y mensajes de error.

## Discrepancias y límites

El texto narrativo de `DESIGN.md` menciona además azul institucional `#0f2942` y acción `#2563eb`, mientras el bloque de tokens y las pantallas HTML usan `#001428` y `#0051d5`. Para los estilos de autenticación se usan los tokens del HTML observado; la diferencia queda abierta hasta aprobación visual.

Las capturas incluyen nombres comerciales, declaraciones de seguridad, soporte, interoperabilidad y funciones clínicas que no están aprobadas por el PRD. Se conserva su lenguaje visual, no esas afirmaciones ni datos de ejemplo.

## Procedencia

Observación de `citas-web/stitch_citas_brayan/clinical_trust/DESIGN.md` y de las capturas/HTML de registro e inicio de sesión. La carpeta de referencias se retiró del workspace el 2026-09-22 por instrucción del usuario. No se guardaron capturas ni HTML en RAW.

## Enlaces relacionados

- [Arquitectura](architecture.md)
- [Riesgos y preguntas abiertas](risks-and-open-questions.md)
