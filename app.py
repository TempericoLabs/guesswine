from __future__ import annotations

from dataclasses import dataclass
from typing import Dict, List, Tuple

import pandas as pd
import streamlit as st


st.set_page_config(
    page_title="Identificador de Vino Espanol",
    page_icon="🍷",
    layout="wide",
)


# Tabla exacta solicitada para clasificacion base
WINE_TABLE_DATA = [
    {
        "TIPO": "BLANCO",
        "UVA": "monovarietal",
        "Subtipo/Elaboración principal": "joven, lías, fermentado en barrica, crianza",
    },
    {
        "TIPO": "TINTO",
        "UVA": "monovarietal",
        "Subtipo/Elaboración principal": "joven, roble, crianza, reserva",
    },
    {
        "TIPO": "ROSADO",
        "UVA": "monovarietal",
        "Subtipo/Elaboración principal": "joven, lías, crianza",
    },
    {
        "TIPO": "CLARETE",
        "UVA": "monovarietal",
        "Subtipo/Elaboración principal": "joven, lías, crianza",
    },
    {
        "TIPO": "ESPUMOSO",
        "UVA": "monovarietal",
        "Subtipo/Elaboración principal": "tradicional",
    },
    {
        "TIPO": "GENEROSO",
        "UVA": "monovarietal",
        "Subtipo/Elaboración principal": "biológica (fino o manzanilla), oxidativa (palo cortado, amontillado, oloroso)",
    },
    {
        "TIPO": "GENEROSO DE LICOR",
        "UVA": "monovarietal",
        "Subtipo/Elaboración principal": "pale cream, medium, cream",
    },
    {
        "TIPO": "DULCE NATURAL",
        "UVA": "monovarietal",
        "Subtipo/Elaboración principal": "-",
    },
    {
        "TIPO": "VERMUT",
        "UVA": "monovarietal",
        "Subtipo/Elaboración principal": "-",
    },
]
WINE_TABLE_DF = pd.DataFrame(WINE_TABLE_DATA)


@dataclass
class CandidateProfile:
    tipo: str
    elaboracion: str
    uva_probable: str
    do_sugerida: str
    zona: str
    graduacion_estimada: str


CANDIDATES: List[CandidateProfile] = [
    CandidateProfile(
        "BLANCO",
        "joven",
        "Albariño / Verdejo",
        "Rías Baixas / Rueda",
        "Galicia / Castilla y León",
        "11-13% vol",
    ),
    CandidateProfile(
        "BLANCO",
        "lías",
        "Godello / Albariño",
        "Valdeorras / Rías Baixas",
        "Galicia",
        "12-13.5% vol",
    ),
    CandidateProfile(
        "BLANCO",
        "fermentado en barrica",
        "Chardonnay / Viura",
        "Rioja / Penedès",
        "La Rioja / Cataluña",
        "12.5-14% vol",
    ),
    CandidateProfile(
        "BLANCO",
        "crianza",
        "Viura / Godello",
        "Rioja / Bierzo",
        "La Rioja / Castilla y León",
        "13-14.5% vol",
    ),
    CandidateProfile(
        "TINTO",
        "joven",
        "Tempranillo / Garnacha",
        "Rioja / Ribera del Duero",
        "La Rioja / Castilla y León",
        "13-14% vol",
    ),
    CandidateProfile(
        "TINTO",
        "roble",
        "Tempranillo / Monastrell",
        "Ribera del Duero / Jumilla",
        "Castilla y León / Murcia",
        "13.5-14.5% vol",
    ),
    CandidateProfile(
        "TINTO",
        "crianza",
        "Tempranillo / Mazuelo",
        "Rioja / Ribera del Duero",
        "La Rioja / Castilla y León",
        "13.5-15% vol",
    ),
    CandidateProfile(
        "TINTO",
        "reserva",
        "Tempranillo / Graciano",
        "Rioja / Toro",
        "La Rioja / Castilla y León",
        "14-15.5% vol",
    ),
    CandidateProfile(
        "ROSADO",
        "joven",
        "Garnacha / Bobal",
        "Navarra / Cigales",
        "Navarra / Castilla y León",
        "12-13.5% vol",
    ),
    CandidateProfile(
        "ROSADO",
        "lías",
        "Garnacha / Tempranillo",
        "Navarra / Rioja",
        "Navarra / La Rioja",
        "12.5-13.5% vol",
    ),
    CandidateProfile(
        "ROSADO",
        "crianza",
        "Garnacha / Tempranillo",
        "Rioja / Navarra",
        "La Rioja / Navarra",
        "13-14% vol",
    ),
    CandidateProfile(
        "CLARETE",
        "joven",
        "Tempranillo / Garnacha",
        "Cigales",
        "Castilla y León",
        "12.5-14% vol",
    ),
    CandidateProfile(
        "CLARETE",
        "lías",
        "Tempranillo / Albillo",
        "Cigales",
        "Castilla y León",
        "13-14% vol",
    ),
    CandidateProfile(
        "CLARETE",
        "crianza",
        "Tempranillo / Garnacha",
        "Cigales / Rioja",
        "Castilla y León / La Rioja",
        "13.5-14.5% vol",
    ),
    CandidateProfile(
        "ESPUMOSO",
        "tradicional",
        "Macabeo / Xarel·lo / Parellada",
        "Cava",
        "Cataluña",
        "11.5-12.5% vol",
    ),
    CandidateProfile(
        "GENEROSO",
        "biológica (fino o manzanilla)",
        "Palomino Fino",
        "Jerez-Xérès-Sherry / Manzanilla-Sanlúcar",
        "Andalucía",
        "15-15.5% vol",
    ),
    CandidateProfile(
        "GENEROSO",
        "oxidativa (palo cortado, amontillado, oloroso)",
        "Palomino Fino",
        "Jerez-Xérès-Sherry",
        "Andalucía",
        "17-22% vol",
    ),
    CandidateProfile(
        "GENEROSO DE LICOR",
        "pale cream",
        "Palomino Fino",
        "Jerez-Xérès-Sherry",
        "Andalucía",
        "15.5-17% vol",
    ),
    CandidateProfile(
        "GENEROSO DE LICOR",
        "medium",
        "Palomino + PX",
        "Jerez-Xérès-Sherry",
        "Andalucía",
        "16-19% vol",
    ),
    CandidateProfile(
        "GENEROSO DE LICOR",
        "cream",
        "Oloroso + PX",
        "Jerez-Xérès-Sherry",
        "Andalucía",
        "17-20% vol",
    ),
    CandidateProfile(
        "DULCE NATURAL",
        "-",
        "Pedro Ximénez / Moscatel",
        "Málaga / Montilla-Moriles / Jerez",
        "Andalucía",
        "15-22% vol",
    ),
    CandidateProfile(
        "VERMUT",
        "-",
        "Macabeo / Airén (base aromatizada)",
        "Reus / Jerez / Madrid",
        "Cataluña / Andalucía / Madrid",
        "15-18% vol",
    ),
]


DEFAULT_STATE: Dict[str, object] = {
    "phase": 0,
    "resultados": None,
    "visual_color": "Selecciona...",
    "visual_color_otro": "",
    "visual_densidad": "Selecciona...",
    "visual_lagrima": "Selecciona...",
    "visual_claridad": "Selecciona...",
    "visual_burbujas": False,
    "olf_intensidad": "Selecciona...",
    "olf_aromas_primarios": [],
    "olf_aromas_secundarios": [],
    "olf_aromas_terciarios": [],
    "gus_sabor": [],
    "gus_cuerpo": "Selecciona...",
    "gus_acidez": "Selecciona...",
    "gus_alcohol": "Selecciona...",
    "gus_persistencia": "Selecciona...",
    "gus_efervescencia": False,
    "gus_salinidad": False,
    "gus_notas": "",
}


EXAMPLE_TASTINGS: Dict[str, Dict[str, object]] = {
    "Blanco joven (ejemplo)": {
        "visual_color": "Amarillo pajizo",
        "visual_densidad": "Media",
        "visual_lagrima": "Rápida",
        "visual_claridad": "Limpio",
        "visual_burbujas": False,
        "olf_intensidad": "Media",
        "olf_aromas_primarios": ["Cítricos", "Fruta blanca"],
        "olf_aromas_secundarios": [],
        "olf_aromas_terciarios": [],
        "gus_sabor": ["Seco", "Ácido"],
        "gus_cuerpo": "Ligero",
        "gus_acidez": "Alta",
        "gus_alcohol": "Medio (12-14%)",
        "gus_persistencia": "Media",
        "gus_efervescencia": False,
        "gus_salinidad": False,
        "gus_notas": "Ejemplo de blanco fresco y frutal.",
    },
    "Tinto crianza (ejemplo)": {
        "visual_color": "Rojo rubí",
        "visual_densidad": "Alta",
        "visual_lagrima": "Lenta",
        "visual_claridad": "Limpio",
        "visual_burbujas": False,
        "olf_intensidad": "Alta",
        "olf_aromas_primarios": ["Fruta roja", "Fruta negra"],
        "olf_aromas_secundarios": [],
        "olf_aromas_terciarios": ["Vainilla", "Tostado"],
        "gus_sabor": ["Seco", "Tánico"],
        "gus_cuerpo": "Pleno",
        "gus_acidez": "Media",
        "gus_alcohol": "Alto (>14%)",
        "gus_persistencia": "Larga",
        "gus_efervescencia": False,
        "gus_salinidad": False,
        "gus_notas": "Ejemplo de tinto estructurado con madera.",
    },
    "Espumoso tradicional (ejemplo)": {
        "visual_color": "Amarillo limón",
        "visual_densidad": "Media",
        "visual_lagrima": "Rápida",
        "visual_claridad": "Limpio",
        "visual_burbujas": True,
        "olf_intensidad": "Media",
        "olf_aromas_primarios": ["Cítricos", "Floral"],
        "olf_aromas_secundarios": ["Levadura", "Panadería"],
        "olf_aromas_terciarios": [],
        "gus_sabor": ["Seco", "Ácido"],
        "gus_cuerpo": "Ligero",
        "gus_acidez": "Alta",
        "gus_alcohol": "Bajo (<12%)",
        "gus_persistencia": "Media",
        "gus_efervescencia": True,
        "gus_salinidad": False,
        "gus_notas": "Ejemplo de espumoso con burbuja fina.",
    },
}


def _copy_value(value: object) -> object:
    if isinstance(value, list):
        return value.copy()
    return value


def init_state() -> None:
    for key, value in DEFAULT_STATE.items():
        if key not in st.session_state:
            st.session_state[key] = _copy_value(value)


def reset_state() -> None:
    for key, value in DEFAULT_STATE.items():
        st.session_state[key] = _copy_value(value)


def load_example(name: str) -> None:
    reset_state()
    for key, value in EXAMPLE_TASTINGS[name].items():
        st.session_state[key] = _copy_value(value)
    st.session_state["phase"] = 1


def get_visual_color() -> str:
    if st.session_state["visual_color"] == "Otro (escribir abajo)":
        return str(st.session_state["visual_color_otro"]).strip()
    return str(st.session_state["visual_color"]).strip()


def validate_visual() -> List[str]:
    errors: List[str] = []
    if st.session_state["visual_color"] == "Selecciona...":
        errors.append("Selecciona un color en la fase visual.")
    if st.session_state["visual_color"] == "Otro (escribir abajo)" and not str(
        st.session_state["visual_color_otro"]
    ).strip():
        errors.append("Escribe el color en el campo 'Otro'.")
    if st.session_state["visual_densidad"] == "Selecciona...":
        errors.append("Selecciona la densidad.")
    if st.session_state["visual_lagrima"] == "Selecciona...":
        errors.append("Selecciona el comportamiento de la lágrima.")
    if st.session_state["visual_claridad"] == "Selecciona...":
        errors.append("Selecciona la claridad.")
    return errors


def validate_olfativa() -> List[str]:
    errors: List[str] = []
    if st.session_state["olf_intensidad"] == "Selecciona...":
        errors.append("Selecciona la intensidad olfativa.")
    if len(st.session_state["olf_aromas_primarios"]) == 0:
        errors.append("Elige al menos un aroma primario.")
    return errors


def validate_gustativa() -> List[str]:
    errors: List[str] = []
    if len(st.session_state["gus_sabor"]) == 0:
        errors.append("Selecciona al menos una sensación de sabor.")
    if st.session_state["gus_cuerpo"] == "Selecciona...":
        errors.append("Selecciona el cuerpo.")
    if st.session_state["gus_acidez"] == "Selecciona...":
        errors.append("Selecciona la acidez.")
    if st.session_state["gus_alcohol"] == "Selecciona...":
        errors.append("Selecciona la sensación de alcohol.")
    if st.session_state["gus_persistencia"] == "Selecciona...":
        errors.append("Selecciona la persistencia.")
    return errors


def to_lower_set(values: List[str]) -> set[str]:
    return {v.lower() for v in values}


def contains_any(values: set[str], options: set[str]) -> bool:
    return len(values.intersection(options)) > 0


def color_contains(color: str, options: Tuple[str, ...]) -> bool:
    color_l = color.lower()
    return any(option in color_l for option in options)


def collect_observations() -> Dict[str, object]:
    return {
        "color": get_visual_color(),
        "densidad": str(st.session_state["visual_densidad"]).lower(),
        "lagrima": str(st.session_state["visual_lagrima"]).lower(),
        "claridad": str(st.session_state["visual_claridad"]).lower(),
        "burbujas_visual": bool(st.session_state["visual_burbujas"]),
        "intensidad_olfativa": str(st.session_state["olf_intensidad"]).lower(),
        "aromas_primarios": to_lower_set(st.session_state["olf_aromas_primarios"]),
        "aromas_secundarios": to_lower_set(st.session_state["olf_aromas_secundarios"]),
        "aromas_terciarios": to_lower_set(st.session_state["olf_aromas_terciarios"]),
        "sabor": to_lower_set(st.session_state["gus_sabor"]),
        "cuerpo": str(st.session_state["gus_cuerpo"]).lower(),
        "acidez": str(st.session_state["gus_acidez"]).lower(),
        "alcohol": str(st.session_state["gus_alcohol"]).lower(),
        "persistencia": str(st.session_state["gus_persistencia"]).lower(),
        "efervescencia": bool(st.session_state["gus_efervescencia"]),
        "salinidad": bool(st.session_state["gus_salinidad"]),
    }


def score_candidate(
    candidate: CandidateProfile, obs: Dict[str, object]
) -> Tuple[int, List[str]]:
    score = 0
    reasons: List[str] = []

    def add(points: int, condition: bool, reason: str) -> None:
        nonlocal score
        if condition:
            score += points
            reasons.append(reason)

    color = str(obs["color"]).lower()
    prim = obs["aromas_primarios"]
    sec = obs["aromas_secundarios"]
    ter = obs["aromas_terciarios"]
    sabor = obs["sabor"]
    cuerpo = str(obs["cuerpo"])
    acidez = str(obs["acidez"])
    alcohol = str(obs["alcohol"])
    persist = str(obs["persistencia"])
    has_bubbles = bool(obs["burbujas_visual"]) or bool(obs["efervescencia"])
    salinidad = bool(obs["salinidad"])

    blancos = ("amarillo", "dorado", "verdoso", "pajizo", "limón", "limon")
    tintos = ("rojo", "rubí", "rubi", "granate", "picota", "violáceo", "violaceo")
    rosados = ("rosado", "salmón", "salmon", "frambuesa")
    claretes = ("clarete", "cereza", "rubí claro", "rubi claro")
    ambar = ("ámbar", "ambar", "caoba", "anaranjado", "miel")

    if candidate.tipo == "BLANCO":
        add(3, color_contains(color, blancos), "color dentro de blancos")
        add(
            2,
            contains_any(prim, {"cítricos", "fruta blanca", "fruta tropical"}),
            "aroma primario frutal/fresco",
        )
        add(2, acidez in {"media", "alta"}, "acidez compatible con blanco")
        add(1, cuerpo in {"ligero", "medio"}, "cuerpo ligero o medio")
        add(1, "seco" in sabor, "perfil de boca seco")

        if candidate.elaboracion == "joven":
            add(2, len(ter) == 0, "pocos aromas terciarios (estilo joven)")
        elif candidate.elaboracion == "lías":
            add(
                3,
                contains_any(sec, {"levadura", "panadería", "panaderia"}),
                "notas de lías/levadura",
            )
        elif candidate.elaboracion == "fermentado en barrica":
            add(
                3,
                contains_any(ter, {"vainilla", "tostado"}),
                "notas de barrica (vainilla/tostado)",
            )
        elif candidate.elaboracion == "crianza":
            add(
                3,
                contains_any(ter, {"vainilla", "tostado", "frutos secos"}),
                "terciarios de crianza",
            )
            add(1, persist in {"media", "larga"}, "persistencia de vino evolucionado")

        if has_bubbles:
            score -= 2

    elif candidate.tipo == "TINTO":
        add(3, color_contains(color, tintos), "color dentro de tintos")
        add(2, "tánico" in sabor or "tanico" in sabor, "presencia de taninos")
        add(2, cuerpo in {"medio", "pleno"}, "cuerpo medio/pleno")
        add(
            2,
            contains_any(prim, {"fruta roja", "fruta negra", "fruta madura"}),
            "aroma de fruta roja/negra",
        )
        add(1, alcohol in {"medio (12-14%)", "alto (>14%)"}, "alcohol medio o alto")

        if candidate.elaboracion == "joven":
            add(2, len(ter) == 0, "sin madera marcada (joven)")
        elif candidate.elaboracion == "roble":
            add(
                2,
                contains_any(ter, {"vainilla", "tostado"}),
                "toque de roble",
            )
        elif candidate.elaboracion == "crianza":
            add(
                3,
                contains_any(ter, {"vainilla", "tostado", "cuero", "tabaco"}),
                "terciarios de crianza",
            )
        elif candidate.elaboracion == "reserva":
            add(
                4,
                contains_any(ter, {"vainilla", "tostado", "cuero", "tabaco"})
                and persist == "larga",
                "estructura y evolución de reserva",
            )

        if has_bubbles:
            score -= 3

    elif candidate.tipo == "ROSADO":
        add(4, color_contains(color, rosados), "color rosado/salmón")
        add(
            2,
            contains_any(prim, {"fruta roja", "floral", "cítricos"}),
            "aroma primario típico de rosado",
        )
        add(1, cuerpo in {"ligero", "medio"}, "cuerpo de rosado")
        add(1, acidez in {"media", "alta"}, "acidez fresca")
        add(1, ("tánico" not in sabor and "tanico" not in sabor), "tanino bajo")

        if candidate.elaboracion == "joven":
            add(2, len(ter) == 0, "perfil joven sin terciarios")
        elif candidate.elaboracion == "lías":
            add(
                2,
                contains_any(sec, {"levadura", "panadería", "panaderia"}) or cuerpo == "medio",
                "volumen por trabajo sobre lías",
            )
        elif candidate.elaboracion == "crianza":
            add(
                2,
                contains_any(ter, {"vainilla", "tostado"}),
                "toque de crianza",
            )

        if has_bubbles:
            score -= 1

    elif candidate.tipo == "CLARETE":
        add(3, color_contains(color, claretes) or color_contains(color, rosados), "tono de clarete")
        add(
            2,
            contains_any(prim, {"fruta roja", "floral"}),
            "aroma de fruta roja/floral",
        )
        add(2, cuerpo in {"medio", "pleno"}, "cuerpo con algo de estructura")
        add(1, "tánico" in sabor or "tanico" in sabor, "ligera trama tánica")
        add(1, acidez in {"media", "alta"}, "acidez equilibrada")

        if candidate.elaboracion == "joven":
            add(2, len(ter) == 0, "perfil joven")
        elif candidate.elaboracion == "lías":
            add(
                2,
                contains_any(sec, {"levadura", "panadería", "panaderia"}),
                "notas de lías",
            )
        elif candidate.elaboracion == "crianza":
            add(
                2,
                contains_any(ter, {"vainilla", "tostado"}),
                "terciarios de crianza",
            )

        if has_bubbles:
            score -= 1

    elif candidate.tipo == "ESPUMOSO":
        add(6, has_bubbles, "presencia de burbujas/efervescencia")
        add(
            2,
            contains_any(sec, {"levadura", "panadería", "panaderia"}),
            "aromas de autólisis (levadura/pan)",
        )
        add(1, acidez == "alta", "acidez alta típica de espumoso")
        add(1, persist in {"media", "larga"}, "persistencia de burbuja")
        add(1, color_contains(color, blancos) or color_contains(color, rosados), "gama de color compatible")
        add(1, cuerpo in {"ligero", "medio"}, "cuerpo típico de espumoso")
        if not has_bubbles:
            score -= 5

    elif candidate.tipo == "GENEROSO":
        if "biológica" in candidate.elaboracion:
            add(2, color_contains(color, blancos), "color pálido-dorado")
            add(
                2,
                contains_any(sec, {"levadura"}) or salinidad,
                "pista biológica (levadura/salinidad)",
            )
            add(2, "seco" in sabor, "perfil seco")
            add(2, alcohol in {"medio (12-14%)", "alto (>14%)"}, "sensación alcohólica elevada")
            add(1, persist in {"media", "larga"}, "persistencia media-larga")
        else:
            add(3, color_contains(color, ambar), "color ámbar/caoba")
            add(
                2,
                contains_any(ter, {"frutos secos", "caramelo", "tabaco", "oxidativo"}),
                "notas oxidativas",
            )
            add(2, ("seco" in sabor or "amargo" in sabor), "boca seca o amargosa")
            add(2, alcohol == "alto (>14%)", "alcohol alto")
            add(1, persist == "larga", "larga persistencia")

    elif candidate.tipo == "GENEROSO DE LICOR":
        add(2, color_contains(color, ambar), "color ámbar")
        add(2, alcohol in {"medio (12-14%)", "alto (>14%)"}, "grado alcohólico medio-alto")
        add(1, persist in {"media", "larga"}, "persistencia media-larga")
        if candidate.elaboracion == "pale cream":
            add(3, "dulce" in sabor and acidez in {"media", "alta"}, "dulzor moderado de pale cream")
        elif candidate.elaboracion == "medium":
            add(
                3,
                "dulce" in sabor and ("seco" in sabor or "amargo" in sabor),
                "equilibrio dulce-seco de medium",
            )
        elif candidate.elaboracion == "cream":
            add(4, "dulce" in sabor and cuerpo == "pleno", "dulzor y cuerpo de cream")

    elif candidate.tipo == "DULCE NATURAL":
        add(5, "dulce" in sabor, "dominante dulce")
        add(2, cuerpo in {"medio", "pleno"}, "cuerpo medio/pleno")
        add(
            2,
            contains_any(prim, {"fruta madura", "fruta pasificada"})
            or contains_any(ter, {"caramelo", "frutos secos"}),
            "aromas de fruta madura/pasificada",
        )
        add(1, persist == "larga", "persistencia larga")
        add(1, alcohol in {"medio (12-14%)", "alto (>14%)"}, "alcohol notable")

    elif candidate.tipo == "VERMUT":
        add(
            3,
            contains_any(prim, {"herbal", "especiado", "botánico", "botanico"}),
            "perfil botánico/especiado",
        )
        add(2, "amargo" in sabor, "amargor característico")
        add(2, ("dulce" in sabor or "seco" in sabor), "base vínica con equilibrio de dulzor")
        add(1, color_contains(color, ambar), "color típico de vermut")
        add(1, contains_any(ter, {"caramelo"}), "toque caramelizado")
        add(1, persist in {"media", "larga"}, "persistencia")

    return score, reasons


def rank_wines(obs: Dict[str, object]) -> Tuple[List[Dict[str, object]], bool]:
    ranked: List[Dict[str, object]] = []
    for candidate in CANDIDATES:
        score, reasons = score_candidate(candidate, obs)
        ranked.append(
            {
                "tipo": candidate.tipo,
                "elaboracion": candidate.elaboracion,
                "uva_probable": candidate.uva_probable,
                "do_sugerida": candidate.do_sugerida,
                "zona": candidate.zona,
                "graduacion_estimada": candidate.graduacion_estimada,
                "score": score,
                "reasons": reasons,
            }
        )

    ranked.sort(key=lambda x: x["score"], reverse=True)
    top_score = ranked[0]["score"] if ranked else 0
    atipico = top_score < 6

    top3 = ranked[:3]
    safe_scores = [max(int(item["score"]), 0) + 1 for item in top3]
    total_safe = sum(safe_scores) if safe_scores else 1

    for item, safe_score in zip(top3, safe_scores):
        item["confianza_relativa"] = round((safe_score / total_safe) * 100, 1)

    return top3, atipico


def reason_text(reasons: List[str]) -> str:
    if not reasons:
        return "coincidencia global de perfil"
    return ", ".join(reasons[:3])


def render_sidebar() -> None:
    st.sidebar.title("Opciones")
    st.sidebar.markdown("Carga una cata de prueba para validar la app rapidamente.")
    example = st.sidebar.selectbox(
        "Catas predefinidas",
        ["Selecciona..."] + list(EXAMPLE_TASTINGS.keys()),
    )
    if st.sidebar.button("Cargar ejemplo", use_container_width=True):
        if example == "Selecciona...":
            st.sidebar.warning("Selecciona una cata de ejemplo.")
        else:
            load_example(example)
            st.rerun()

    if st.sidebar.button("Reiniciar cata", use_container_width=True):
        reset_state()
        st.rerun()

    with st.sidebar.expander("Ver tabla base de vinos"):
        st.dataframe(WINE_TABLE_DF, use_container_width=True, hide_index=True)


def render_header() -> None:
    st.title("Identificador de vino español por cata")
    st.caption(
        "Esta app te guía en una cata para identificar el vino. "
        "Introduce observaciones en cada fase."
    )

    phase = int(st.session_state["phase"])
    if phase > 0:
        progress = min(phase / 4, 1.0)
        st.progress(progress)
        labels = {
            1: "Fase actual: Visual",
            2: "Fase actual: Olfativa",
            3: "Fase actual: Gustativa",
            4: "Fase actual: Resultados",
        }
        st.write(labels.get(phase, ""))


def render_inicio() -> None:
    st.info(
        "Tip general: prepara una copa limpia, buena luz y una hoja para anotar."
    )
    st.markdown(
        """
### ¿Como funciona?
1. Fase visual: color, densidad, lagrima y claridad.
2. Fase olfativa: intensidad y familias aromaticas.
3. Fase gustativa: sabor, cuerpo, acidez y alcohol.
4. Resultado: top 3 vinos mas probables segun reglas de matching.
"""
    )

    if st.button("Comenzar cata", type="primary"):
        st.session_state["phase"] = 1
        st.rerun()


def render_visual() -> None:
    st.subheader("Fase 1: Visual")
    st.info(
        "Tip visual: inclina la copa sobre fondo blanco para apreciar tono y brillo."
    )

    color_options = [
        "Selecciona...",
        "Amarillo pajizo",
        "Amarillo limón",
        "Dorado",
        "Rojo rubí",
        "Rojo granate",
        "Rojo picota",
        "Rosado salmón",
        "Rosado frambuesa",
        "Clarete/cereza claro",
        "Ámbar",
        "Caoba",
        "Otro (escribir abajo)",
    ]

    with st.form("visual_form"):
        st.selectbox(
            "Color",
            options=color_options,
            key="visual_color",
            help="Ejemplos: amarillo pajizo, rojo rubí, rosado salmón.",
        )
        st.text_input(
            "Otro color (si aplica)",
            key="visual_color_otro",
            help="Solo usar si elegiste 'Otro'.",
        )
        c1, c2 = st.columns(2)
        with c1:
            st.selectbox(
                "Densidad",
                options=["Selecciona...", "Baja", "Media", "Alta"],
                key="visual_densidad",
                help="Mayor densidad suele indicar mas cuerpo/alcohol.",
            )
            st.selectbox(
                "Lágrima / glicerina",
                options=["Selecciona...", "Lenta", "Rápida", "Ausente"],
                key="visual_lagrima",
                help="Lágrima lenta puede relacionarse con mayor alcohol.",
            )
        with c2:
            st.selectbox(
                "Claridad",
                options=["Selecciona...", "Limpio", "Turbio"],
                key="visual_claridad",
                help="Un vino limpio no presenta velos ni partículas visibles.",
            )
            st.checkbox(
                "¿Observas burbujas?",
                key="visual_burbujas",
                help="Importante para detectar espumosos.",
            )

        next_btn = st.form_submit_button("Siguiente: Fase olfativa", type="primary")

    if next_btn:
        errors = validate_visual()
        if errors:
            for err in errors:
                st.error(err)
        else:
            st.session_state["phase"] = 2
            st.rerun()


def render_olfativa() -> None:
    st.subheader("Fase 2: Olfativa")
    st.info(
        "Tip olfativo: da dos o tres giros suaves a la copa y huele en intervalos cortos."
    )

    with st.form("olfativa_form"):
        st.selectbox(
            "Intensidad olfativa",
            options=["Selecciona...", "Débil", "Media", "Alta"],
            key="olf_intensidad",
            help="Valora cuanto aroma percibes al acercar la copa.",
        )
        st.multiselect(
            "Aromas primarios",
            options=[
                "Cítricos",
                "Fruta blanca",
                "Fruta tropical",
                "Fruta roja",
                "Fruta negra",
                "Fruta madura",
                "Fruta pasificada",
                "Floral",
                "Herbal",
                "Especiado",
                "Botánico",
            ],
            key="olf_aromas_primarios",
            help="Ejemplo: cítricos en blancos jóvenes; fruta roja en rosados/tintos.",
        )
        c1, c2 = st.columns(2)
        with c1:
            st.multiselect(
                "Aromas secundarios (fermentación)",
                options=["Levadura", "Panadería", "Láctico", "Fermentativo"],
                key="olf_aromas_secundarios",
                help="Levadura/panadería son comunes en espumosos o vinos sobre lías.",
            )
        with c2:
            st.multiselect(
                "Aromas terciarios (crianza)",
                options=["Vainilla", "Tostado", "Cuero", "Tabaco", "Frutos secos", "Caramelo", "Oxidativo"],
                key="olf_aromas_terciarios",
                help="Vainilla y tostado suelen indicar paso por barrica.",
            )

        back_col, next_col = st.columns(2)
        back_btn = back_col.form_submit_button("Volver a visual")
        next_btn = next_col.form_submit_button("Siguiente: Fase gustativa", type="primary")

    if back_btn:
        st.session_state["phase"] = 1
        st.rerun()
    if next_btn:
        errors = validate_olfativa()
        if errors:
            for err in errors:
                st.error(err)
        else:
            st.session_state["phase"] = 3
            st.rerun()


def render_gustativa() -> None:
    st.subheader("Fase 3: Gustativa")
    st.info(
        "Tip gustativo: toma un sorbo pequeño, reparte por toda la boca y evalúa final."
    )

    with st.form("gustativa_form"):
        st.multiselect(
            "Sensaciones de sabor",
            options=["Dulce", "Seco", "Ácido", "Amargo", "Tánico"],
            key="gus_sabor",
            help="Puedes elegir varias si el vino combina sensaciones.",
        )
        c1, c2 = st.columns(2)
        with c1:
            st.selectbox(
                "Cuerpo",
                options=["Selecciona...", "Ligero", "Medio", "Pleno"],
                key="gus_cuerpo",
                help="Ligero: paso fácil. Pleno: sensación más densa.",
            )
            st.selectbox(
                "Acidez",
                options=["Selecciona...", "Baja", "Media", "Alta"],
                key="gus_acidez",
                help="Acidez alta aporta frescura y salivación.",
            )
            st.selectbox(
                "Alcohol (sensación de calor)",
                options=["Selecciona...", "Bajo (<12%)", "Medio (12-14%)", "Alto (>14%)"],
                key="gus_alcohol",
                help="Calor más marcado suele indicar mayor graduación.",
            )
        with c2:
            st.selectbox(
                "Persistencia",
                options=["Selecciona...", "Corta", "Media", "Larga"],
                key="gus_persistencia",
                help="Tiempo que dura el sabor tras tragar o escupir.",
            )
            st.checkbox(
                "Efervescencia en boca",
                key="gus_efervescencia",
                help="Señal de espumoso o aguja.",
            )
            st.checkbox(
                "Sensación salina",
                key="gus_salinidad",
                help="Puede aparecer en perfiles de generoso biológico.",
            )
        st.text_area(
            "Notas libres (opcional)",
            key="gus_notas",
            help="Anota matices no cubiertos en los campos anteriores.",
        )

        back_col, result_col = st.columns(2)
        back_btn = back_col.form_submit_button("Volver a olfativa")
        result_btn = result_col.form_submit_button("Calcular resultados", type="primary")

    if back_btn:
        st.session_state["phase"] = 2
        st.rerun()
    if result_btn:
        errors = validate_gustativa()
        if errors:
            for err in errors:
                st.error(err)
        else:
            observations = collect_observations()
            top3, atipico = rank_wines(observations)
            st.session_state["resultados"] = {"top3": top3, "atipico": atipico}
            st.session_state["phase"] = 4
            st.rerun()


def render_resultados() -> None:
    st.subheader("Resultados de clasificación")
    data = st.session_state.get("resultados")
    if not data:
        st.warning("No hay resultados calculados todavía.")
        if st.button("Ir a fase gustativa"):
            st.session_state["phase"] = 3
            st.rerun()
        return

    top3 = data["top3"]
    atipico = data["atipico"]

    if atipico:
        st.warning(
            "No hay una coincidencia clara. Posible vino atípico. "
            "Puedes volver atrás y añadir más detalles."
        )

    for i, item in enumerate(top3, start=1):
        st.markdown(
            f"### {i}. {item['tipo']} - {item['elaboracion']} "
            f"({item['confianza_relativa']}%)"
        )
        st.write(f"**Uva probable:** {item['uva_probable']}")
        st.write(f"**DO sugerida:** {item['do_sugerida']}")
        st.write(f"**Zona sugerida:** {item['zona']}")
        st.write(f"**Graduación estimada:** {item['graduacion_estimada']}")
        st.write(f"**Explicación:** Basado en {reason_text(item['reasons'])}.")
        st.divider()

    st.markdown("#### Resumen comparativo")
    summary_df = pd.DataFrame(
        [
            {
                "Tipo": item["tipo"],
                "Elaboración": item["elaboracion"],
                "Confianza relativa (%)": item["confianza_relativa"],
                "DO sugerida": item["do_sugerida"],
                "Graduación": item["graduacion_estimada"],
            }
            for item in top3
        ]
    )
    st.dataframe(summary_df, use_container_width=True, hide_index=True)

    c1, c2 = st.columns(2)
    if c1.button("Volver a fase gustativa"):
        st.session_state["phase"] = 3
        st.rerun()
    if c2.button("Reiniciar cata completa", type="primary"):
        reset_state()
        st.rerun()


def main() -> None:
    init_state()
    render_sidebar()
    render_header()

    phase = int(st.session_state["phase"])
    if phase == 0:
        render_inicio()
    elif phase == 1:
        render_visual()
    elif phase == 2:
        render_olfativa()
    elif phase == 3:
        render_gustativa()
    else:
        render_resultados()


if __name__ == "__main__":
    main()
