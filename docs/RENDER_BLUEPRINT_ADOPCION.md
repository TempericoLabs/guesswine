# Adopción de Render Blueprint (servicio existente)

Esta guía explica cómo pasar de un servicio creado manualmente en Render (como `guesswine`) a un flujo gestionado por `render.yaml` (Blueprint) sin perder el patrón de despliegue.

## Estado actual (ya funcionando)

- App publicada en Render: `guesswine`
- URL Render: `https://guesswine.onrender.com`
- Dominio custom: `https://guesswine.caplatemp.xyz`
- DNS en DonDominio: `CNAME guesswine -> guesswine.onrender.com`
- SSL: gestionado automáticamente por Render

## Qué significa "gestionar por Blueprint"

Significa que la configuración del servicio (runtime, comandos, plan, variables, etc.) vive en el repositorio en `render.yaml`, para poder:

- recrear el servicio fácilmente
- repetir el patrón en otros proyectos
- reducir errores manuales al configurar Render

## Importante: limitación práctica

Render no siempre "convierte" automáticamente un servicio ya existente en uno gestionado por Blueprint con un clic.

La forma segura y simple es:

1. Mantener el servicio actual (`guesswine`) funcionando.
2. Usar `render.yaml` como fuente de verdad para próximos servicios.
3. Si quieres migración completa a Blueprint, crear un servicio nuevo desde Blueprint y luego mover dominio/tráfico.

## Opción A (recomendada ahora): mantener servicio actual + usar Blueprint en adelante

No cambias nada de producción ahora mismo. Solo conservas en el repo:

- `render.yaml`
- `README.md`

Ventajas:

- cero riesgo para el dominio actual
- mismo patrón listo para repetir en `meteoTrader`
- más rápido

## Opción B (migración real a Blueprint) - cuando quieras

### 1. Verificar `render.yaml`

En este repo ya existe:

- `/Users/emiair/Documents/CodexAPP/GuessWine/render.yaml`

### 2. Crear nuevo servicio desde Blueprint

En Render:

1. `New +`
2. `Blueprint`
3. Selecciona el repo `TempericoLabs/guesswine`
4. Render leerá `render.yaml`
5. Crea un servicio nuevo (temporal), por ejemplo:
   - `guesswine-blueprint`

### 3. Probar el nuevo servicio

Valida que funcione en su URL `*.onrender.com` antes de tocar DNS.

### 4. Mover dominio custom (cuando esté validado)

En Render:

1. Quitar `guesswine.caplatemp.xyz` del servicio antiguo
2. Añadir `guesswine.caplatemp.xyz` al servicio nuevo

En DonDominio:

- Normalmente no tendrás que cambiar el `CNAME` si el destino cambia a otro `*.onrender.com`, pero sí debes actualizarlo si Render te da otro subdominio.

### 5. Verificar SSL

Render volverá a emitir o revalidar el certificado SSL automáticamente.

## Checklist de migración segura

- [ ] Nuevo servicio funcionando en URL temporal
- [ ] Mismo comportamiento que producción
- [ ] Dominio custom añadido en nuevo servicio
- [ ] DNS apunta al destino correcto
- [ ] SSL activo en Render
- [ ] Navegación en `https://guesswine.caplatemp.xyz` OK

## Recomendación para ti (ahora mismo)

Quédate en Opción A de momento:

- mantén `guesswine` como está
- usa `render.yaml` como plantilla base
- aplica el flujo de Blueprint en el próximo proyecto (`meteoTrader`)

Así aprendes el patrón sin tocar una app ya publicada y funcionando.
