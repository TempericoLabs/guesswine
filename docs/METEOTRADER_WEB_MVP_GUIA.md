# Guía para el otro thread: `meteoTrader` (web + dominio + SSL)

Esta guía está preparada para usarla en el thread/proyecto `meteoTrader`, que **no** está en este workspace y **no** parte de una app Python como `guesswine`.

## Estado comprobado de `meteoTrader` (resumen)

Proyecto localizado en:

- `/Users/emiair/Documents/CodexAPP/meteoTrader`

Situación actual:

- App Android compleja (Kotlin + Jetpack Compose)
- Muchas fuentes/metodología de datos meteo + trading
- No hay una app web lista para desplegar
- No hay un backend Python listo para Render (el directorio `backend/ecmwf_bridge` está vacío a efectos prácticos)
- Sí existe documentación web utilizable:
  - `/Users/emiair/Documents/CodexAPP/meteoTrader/docs/MANUAL_USO_POLYMETEO.html`

## Objetivo de dominio (confirmado)

Para `meteoTrader`, el subdominio será:

- `polymeteo.caplatemp.xyz`

## Diferencia clave respecto a `guesswine`

En `guesswine` tenías una app Streamlit (`app.py`) lista para ejecutar en Render.

En `meteoTrader` **no** puedes desplegar "la app Android" directamente como web. Necesitas una de estas rutas:

1. **MVP web estático (más simple y rápido)**
2. **Web app nueva (frontend web) conectada a APIs**
3. **Paridad Android + Web** con arquitectura compartida (más compleja)

## Recomendación práctica (para aprender y publicar rápido)

### Fase A (MVP web inmediato) - recomendada

Publicar una **web estática** en Render con:

- manual de uso
- presentación del proyecto
- capturas
- enlaces a APK / GitHub / roadmap

Esto te permite aprender el flujo completo:

- GitHub -> Render -> DonDominio DNS -> SSL

sin meterte todavía en el coste técnico de portar la app Android a web.

### Fase B (web funcional, lectura/consulta)

Crear una web app nueva (por ejemplo `React/Vite`) que muestre:

- datos procesados
- paneles
- visualizaciones
- señales / backtest (si expones una API)

### Fase C (paridad real Android + Web)

Requiere rediseñar arquitectura:

- backend de datos/negocio compartido
- auth (si hay usuarios)
- persistencia
- frontend web separado
- Android como cliente adicional

## Ruta MVP recomendada para `meteoTrader` (publicación web ya)

## Opción 1: Render Static Site (más simple)

### Qué publicar

Usar como punto de partida:

- `docs/MANUAL_USO_POLYMETEO.html`
- `docs/assets/`

Crear una carpeta web estática (en `meteoTrader`, no aquí), por ejemplo:

- `site/`

Con contenido como:

- `index.html` (landing)
- `manual/` (manual actual)
- `assets/` (capturas/recursos)

### Por qué esta opción

- No necesitas Python
- No necesitas servidor backend
- Render Static Site es muy simple
- SSL automático igual que en `guesswine`

## Opción 2: Render Web Service (cuando exista backend o app web)

Si en otro thread creas:

- frontend Node/Vite/Next
- o backend Python/Node

entonces usarás `Render Web Service` (o `Static Site + Web Service`).

## DNS + SSL para `polymeteo.caplatemp.xyz` (DonDominio + Render)

El patrón es el mismo que `guesswine`.

### En Render

1. Desplegar el servicio (Static Site o Web Service)
2. Añadir custom domain:
   - `polymeteo.caplatemp.xyz`

### En DonDominio (Zona DNS)

Crear entrada DNS:

- Tipo: `CNAME`
- Host: `polymeteo`
- Destino: el subdominio `*.onrender.com` que te dé Render
- TTL: `Por defecto`

Ejemplo (solo ejemplo):

- `polymeteo` -> `polymeteo-site.onrender.com.`

### SSL

- Lo gestiona **Render** automáticamente (Let's Encrypt)
- No instalar certificado manual en DonDominio para este subdominio
- No rellenar `Certificado / Clave / CA` manualmente

## Impacto del comodín DNS actual (`*.caplatemp.xyz`)

Si ya existe un comodín CNAME en DonDominio (como en `guesswine`), no pasa nada:

- un registro específico `polymeteo` prevalece sobre el comodín

## Qué se hará en el otro thread (plan sugerido)

## Escenario A: publicar rápido (estático)

1. Revisar `meteoTrader/docs/MANUAL_USO_POLYMETEO.html`
2. Crear estructura `site/` limpia (landing + manual)
3. Crear `render.yaml` para Render Static Site
4. Subir a GitHub
5. Deploy en Render
6. Añadir `polymeteo.caplatemp.xyz`
7. Crear `CNAME polymeteo` en DonDominio
8. Verificar SSL

## Escenario B: web funcional posterior

1. Definir qué parte de la app Android va a web (lectura, señales, backtest, etc.)
2. Decidir arquitectura:
   - Frontend web (`React/Vite`) + backend API
   - o solo frontend con datos cacheados/estáticos (si MVP)
3. Definir secretos y proveedores (API keys)
4. Desplegar en Render con `render.yaml`
5. Reusar `polymeteo.caplatemp.xyz`

## Qué NO hacer al principio (recomendación)

- No intentar portar toda la app Android a web en una sola iteración
- No empezar por auth + backend + DB si el objetivo inicial es aprender despliegue
- No comprar SSL en DonDominio para este subdominio si Render ya lo gestiona

## Checklist para arrancar el otro thread con buen enfoque

- [ ] Objetivo claro: `landing/manual` o `web funcional`
- [ ] Confirmar repositorio GitHub de `meteoTrader`
- [ ] Elegir Render Static Site (MVP) o Web Service
- [ ] Preparar `render.yaml`
- [ ] Desplegar en `*.onrender.com`
- [ ] Añadir custom domain `polymeteo.caplatemp.xyz`
- [ ] Crear `CNAME polymeteo` en DonDominio
- [ ] Esperar propagación + SSL automático

## Nota de trabajo en Codex (importante)

Como `meteoTrader` está en otro workspace:

- `/Users/emiair/Documents/CodexAPP/meteoTrader`

lo ideal es abrir ese proyecto en su propio thread cuando vayamos a ejecutar cambios reales (crear `site/`, `render.yaml`, etc.).

La guía de este archivo sirve como referencia técnica y checklist para ese momento.
