# {{APP_NAME}}

Aplicación web desplegada con **Streamlit + Render** y dominio propio en **DonDominio**.

## URLs

- Producción: `https://{{CUSTOM_DOMAIN}}`
- Render: `https://{{RENDER_SUBDOMAIN}}.onrender.com`

## Requisitos

- Python 3.10+ (recomendado 3.11)

## Ejecutar en local

```bash
cd {{LOCAL_PROJECT_PATH}}
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
streamlit run app.py
```

## Despliegue en Render

### Build command

```bash
pip install -r requirements.txt
```

### Start command

```bash
streamlit run app.py --server.port $PORT --server.address 0.0.0.0 --server.headless true
```

## DNS (DonDominio)

Crear entrada DNS:

- Tipo: `CNAME`
- Host: `{{SUBDOMAIN_HOST}}`
- Destino: `{{RENDER_SUBDOMAIN}}.onrender.com.`
- TTL: `Por defecto`

## SSL

- SSL gestionado por **Render** (automático)
- No instalar certificado manual en DonDominio para este subdominio

## Notas

- En Render Free la app puede tardar en arrancar tras inactividad.
