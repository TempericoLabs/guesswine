# Chuleta rápida: Render + DonDominio + SSL

Uso rápido para desplegar una app web y publicarla en `*.caplatemp.xyz`.

## Patrón estándar (siempre)

1. Local
2. GitHub
3. Render (`*.onrender.com`)
4. Custom Domain en Render
5. `CNAME` en DonDominio
6. SSL automático en Render

## Deploy normal (cambio de app)

```bash
git add .
git commit -m "..."
git push
```

Luego:

- Revisar deploy en Render
- Probar `*.onrender.com`
- Probar dominio custom

## Render (Streamlit) - comandos clave

### Build command

```bash
pip install -r requirements.txt
```

### Start command

```bash
streamlit run app.py --server.port $PORT --server.address 0.0.0.0 --server.headless true
```

## DNS en DonDominio (si la app corre en Render)

No usar "Sitios web" para el deploy. Usar **Zona DNS**.

- Tipo: `CNAME`
- Host: `subdominio` (ej. `guesswine`, `polymeteo`)
- Destino: `tu-servicio.onrender.com.`
- TTL: `Por defecto`

## Comprobar DNS

```bash
dig guesswine.caplatemp.xyz CNAME +short
```

Esperado:

```bash
guesswine.onrender.com.
```

## SSL (regla práctica)

- SSL del subdominio lo gestiona **Render**
- No instalar certificado manual en DonDominio para ese subdominio
- No pegar `Certificado / Clave / CA` manualmente

## Comprobar SSL rápido

- Abrir `https://subdominio.caplatemp.xyz`
- Ver candado activo
- En Render -> `Custom Domains` debe salir activo/verificado

## Rollback (si se rompe producción)

### Opción segura

```bash
git revert HEAD
git push
```

Render redeploya la versión revertida.

## Render Free (normal)

- Puede dormir por inactividad
- Primera carga puede tardar 30-50s
- Si falla de verdad, revisar logs de Render

## Errores comunes

- Usar `A` en vez de `CNAME` para subdominio Render
- Configurar SSL manual en DonDominio para un dominio servido por Render
- Olvidar añadir el custom domain en Render antes de tocar DNS
- Cambiar `render.yaml` sin revisar impacto (si el servicio usa Blueprint)
