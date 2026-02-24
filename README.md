# GuessWine

Aplicación para reconocimiento de vino español basada en una cata guiada (fases visual, olfativa y gustativa).

Este repositorio contiene:

- `app.py`: versión web (Streamlit)
- `android/`: versión Android (Kotlin + Jetpack Compose)

## Demo web

- Producción: [https://guesswine.caplatemp.xyz](https://guesswine.caplatemp.xyz)
- Render: [https://guesswine.onrender.com](https://guesswine.onrender.com)

## Requisitos (web local)

- Python 3.10+ (recomendado 3.11)

## Ejecutar en local (web/Streamlit)

```bash
cd /Users/emiair/Documents/CodexAPP/GuessWine
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
streamlit run app.py
```

Después abre la URL local que muestre Streamlit (normalmente `http://localhost:8501`).

## Ejecutar Android (Android Studio)

1. Abrir la carpeta `android/` como proyecto en Android Studio.
2. Esperar `Gradle Sync`.
3. Ejecutar con un emulador o dispositivo Android.

## Despliegue en Render (web)

La app web está preparada para desplegarse como **Web Service** en Render.

### Configuración usada

- Runtime: `Python 3`
- Build command:

```bash
pip install -r requirements.txt
```

- Start command:

```bash
streamlit run app.py --server.port $PORT --server.address 0.0.0.0 --server.headless true
```

## Despliegue reproducible con `render.yaml`

Este repo incluye `render.yaml` para crear el servicio desde Render Blueprint.

## Guías y plantillas reutilizables

- Guía de adopción a Blueprint para el servicio actual: `docs/RENDER_BLUEPRINT_ADOPCION.md`
- Guía específica para futuro thread `meteoTrader` (web + Render + DonDominio + SSL): `docs/METEOTRADER_WEB_MVP_GUIA.md`
- Plantilla reusable para futuros proyectos Streamlit + Render + DonDominio:
  - `templates/render-streamlit/README.template.md`
  - `templates/render-streamlit/render.template.yaml`
  - `templates/render-streamlit/CHECKLIST.md`

Estas plantillas están pensadas para replicar el mismo flujo en proyectos como `meteoTrader`.

## Dominio personalizado (DonDominio + Render)

Para exponer la app en `guesswine.caplatemp.xyz`:

1. Añadir dominio personalizado en Render (`guesswine.caplatemp.xyz`).
2. Crear en DonDominio una entrada DNS:
   - Tipo: `CNAME`
   - Host: `guesswine`
   - Destino: `guesswine.onrender.com.`
3. Esperar propagación DNS.
4. Render emite SSL automáticamente (Let's Encrypt).

### SSL (importante)

- El SSL de `guesswine.caplatemp.xyz` lo gestiona **Render**.
- No es necesario instalar certificado manual en DonDominio para este subdominio.

## Notas

- En el plan Free de Render, la app puede tardar en responder tras inactividad (spin down).
- La versión Android y la versión web comparten la misma lógica funcional de clasificación, pero están implementadas en tecnologías distintas.
