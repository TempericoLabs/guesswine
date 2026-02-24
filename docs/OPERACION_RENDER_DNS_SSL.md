# Operación diaria: Render + DonDominio + SSL (runbook)

Guía corta para operar proyectos web desplegados en Render con dominio en DonDominio.

Pensada para casos como:

- `guesswine.caplatemp.xyz`
- `polymeteo.caplatemp.xyz` (futuro)

## 1. Checklist de despliegue normal (cambios de app)

## En local

- [ ] Probar app localmente
- [ ] `git status` limpio (o cambios esperados)
- [ ] `git add ...`
- [ ] `git commit -m "..."`
- [ ] `git push`

## En Render

- [ ] Ver que se dispara deploy automático (si `autoDeploy: true`)
- [ ] Revisar logs de build y start
- [ ] Verificar URL Render (`*.onrender.com`)
- [ ] Verificar dominio custom (`https://subdominio.caplatemp.xyz`)

## Verificación rápida

```bash
curl -I https://guesswine.caplatemp.xyz
```

Debe devolver `HTTP/2 200` (o `302` si hay redirección intencionada).

## 2. Checklist de DNS (DonDominio)

Usar **Zona DNS** (no "Sitios web") cuando la app corre en Render.

## Patrón recomendado para subdominios

- Tipo: `CNAME`
- Host: `subdominio` (ej. `guesswine`, `polymeteo`)
- Destino: `tu-servicio.onrender.com.`
- TTL: `Por defecto`

## Verificación DNS desde terminal

```bash
dig guesswine.caplatemp.xyz CNAME +short
```

Esperado:

```bash
guesswine.onrender.com.
```

## Si no resuelve bien

- [ ] Revisar que no exista otro registro exacto con el mismo host (`A`/`CNAME`)
- [ ] Confirmar que el `CNAME` apunta al hostname correcto de Render
- [ ] Esperar propagación DNS (minutos, a veces más)

Nota:

- Un registro específico (`guesswine`) prevalece sobre el comodín `*.caplatemp.xyz`.

## 3. Checklist SSL (Render)

## Regla práctica

- SSL del subdominio lo gestiona **Render**
- No instalar certificado manual en DonDominio para subdominios que apuntan a Render

## Verificación funcional

- [ ] `https://subdominio.caplatemp.xyz` abre en navegador
- [ ] Candado activo (sin aviso de certificado)
- [ ] En Render, `Custom Domains` aparece en estado activo/verificado

## Si SSL no activa

- [ ] Confirmar DNS correcto (`CNAME`)
- [ ] Confirmar dominio añadido en `Custom Domains` de Render
- [ ] Esperar propagación/verificación
- [ ] Evitar certificados manuales en DonDominio para ese subdominio

## 4. Rollback (si un deploy rompe producción)

## Estrategia recomendada (segura y simple)

Usar Git como mecanismo principal de rollback.

### Opción A: revertir último commit

```bash
git revert HEAD
git push
```

Render redeployará automáticamente la versión revertida.

### Opción B: volver a un commit anterior (sin reescribir historia)

```bash
git revert <commit_malo>
git push
```

### Opción C: redeploy de un commit anterior desde Render

Si Render muestra historial de deploys/redeploy del commit anterior, puedes usarlo para recuperación rápida.  
Si no, vuelve a `A` o `B`.

## Evitar (salvo que sepas por qué)

- `git reset --hard` en ramas compartidas/publicadas
- cambios manuales en Render que contradigan `render.yaml` (si el servicio ya está asociado a Blueprint)

## 5. Servicios con Blueprint (`render.yaml`)

Si el servicio está asociado a Blueprint:

- `render.yaml` es la fuente de verdad de configuración
- cambios en `render.yaml` pueden modificar producción en el siguiente sync/deploy

### Buenas prácticas

- [ ] Cambiar `render.yaml` solo cuando cambie infraestructura/config
- [ ] Documentar cambios de `render.yaml` en el commit
- [ ] Probar primero si el cambio es sensible (plan, comandos, variables, paths)

## 6. Render Free (operación diaria)

## Comportamiento normal

- El servicio puede "dormirse" por inactividad
- La primera petición puede tardar 30–50s (o más)

## Qué hacer

- [ ] Esperar el primer arranque si estuvo inactivo
- [ ] No confundir cold start con caída real
- [ ] Revisar logs si tarda demasiado o falla

## Señales de problema real (no solo cold start)

- Error 502/503 persistente
- logs con fallo en `startCommand`
- app no responde también en `*.onrender.com`

## 7. Checklist de seguridad mínima (MVP)

- [ ] No subir `.env` ni secretos al repo
- [ ] No pegar tokens en chats/documentación
- [ ] Variables sensibles en Render (`Environment`)
- [ ] Revisar `.gitignore` antes del primer commit

## 8. Plantilla de decisión rápida (para nuevos proyectos)

## Si tienes...

- app Streamlit lista -> `Render Web Service`
- web estática (HTML/CSS/JS) -> `Render Static Site`
- app Android nativa -> no se despliega “tal cual” en web; crear versión web/landing aparte

## Patrón estándar recomendado

1. Local
2. GitHub
3. Render (`*.onrender.com`)
4. Custom Domain en Render
5. `CNAME` en DonDominio
6. SSL automático en Render

