package com.example.guesswine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.max

enum class AppPhase {
    START,
    VISUAL,
    OLFATIVA,
    GUSTATIVA,
    RESULTADOS,
}

data class WineTableRow(
    val tipo: String,
    val uva: String,
    val subtipoElaboracionPrincipal: String,
)

data class CandidateProfile(
    val tipo: String,
    val elaboracion: String,
    val uvaProbable: String,
    val doSugerida: String,
    val zona: String,
    val graduacionEstimada: String,
)

data class RankedWineResult(
    val tipo: String,
    val elaboracion: String,
    val uvaProbable: String,
    val doSugerida: String,
    val zona: String,
    val graduacionEstimada: String,
    val score: Int,
    val reasons: List<String>,
    val confianzaRelativa: Double,
)

data class ClassificationResult(
    val top3: List<RankedWineResult>,
    val vinoAtipico: Boolean,
)

data class Observations(
    val color: String,
    val densidad: String,
    val lagrima: String,
    val claridad: String,
    val burbujasVisual: Boolean,
    val intensidadOlfativa: String,
    val aromasPrimarios: Set<String>,
    val aromasSecundarios: Set<String>,
    val aromasTerciarios: Set<String>,
    val sabor: Set<String>,
    val cuerpo: String,
    val acidez: String,
    val alcohol: String,
    val persistencia: String,
    val efervescencia: Boolean,
    val salinidad: Boolean,
)

data class TastingPreset(
    val visualColor: String,
    val visualColorOtro: String = "",
    val visualDensidad: String,
    val visualLagrima: String,
    val visualClaridad: String,
    val visualBurbujas: Boolean,
    val olfIntensidad: String,
    val olfAromasPrimarios: Set<String>,
    val olfAromasSecundarios: Set<String>,
    val olfAromasTerciarios: Set<String>,
    val gusSabor: Set<String>,
    val gusCuerpo: String,
    val gusAcidez: String,
    val gusAlcohol: String,
    val gusPersistencia: String,
    val gusEfervescencia: Boolean,
    val gusSalinidad: Boolean,
    val gusNotas: String,
) {
    fun applyTo(state: WineUiState): WineUiState {
        return state.copy(
            phase = AppPhase.VISUAL,
            visualColor = visualColor,
            visualColorOtro = visualColorOtro,
            visualDensidad = visualDensidad,
            visualLagrima = visualLagrima,
            visualClaridad = visualClaridad,
            visualBurbujas = visualBurbujas,
            olfIntensidad = olfIntensidad,
            olfAromasPrimarios = olfAromasPrimarios,
            olfAromasSecundarios = olfAromasSecundarios,
            olfAromasTerciarios = olfAromasTerciarios,
            gusSabor = gusSabor,
            gusCuerpo = gusCuerpo,
            gusAcidez = gusAcidez,
            gusAlcohol = gusAlcohol,
            gusPersistencia = gusPersistencia,
            gusEfervescencia = gusEfervescencia,
            gusSalinidad = gusSalinidad,
            gusNotas = gusNotas,
            errors = emptyList(),
            resultados = emptyList(),
            vinoAtipico = false,
        )
    }

    fun toUiState(): WineUiState = applyTo(WineUiState())
}

data class WineUiState(
    val phase: AppPhase = AppPhase.START,
    val errors: List<String> = emptyList(),
    val resultados: List<RankedWineResult> = emptyList(),
    val vinoAtipico: Boolean = false,
    val visualColor: String = "Selecciona...",
    val visualColorOtro: String = "",
    val visualDensidad: String = "Selecciona...",
    val visualLagrima: String = "Selecciona...",
    val visualClaridad: String = "Selecciona...",
    val visualBurbujas: Boolean = false,
    val olfIntensidad: String = "Selecciona...",
    val olfAromasPrimarios: Set<String> = emptySet(),
    val olfAromasSecundarios: Set<String> = emptySet(),
    val olfAromasTerciarios: Set<String> = emptySet(),
    val gusSabor: Set<String> = emptySet(),
    val gusCuerpo: String = "Selecciona...",
    val gusAcidez: String = "Selecciona...",
    val gusAlcohol: String = "Selecciona...",
    val gusPersistencia: String = "Selecciona...",
    val gusEfervescencia: Boolean = false,
    val gusSalinidad: Boolean = false,
    val gusNotas: String = "",
)

object WineCatalog {
    val wineTableRows: List<WineTableRow> = listOf(
        WineTableRow("BLANCO", "monovarietal", "joven, lías, fermentado en barrica, crianza"),
        WineTableRow("TINTO", "monovarietal", "joven, roble, crianza, reserva"),
        WineTableRow("ROSADO", "monovarietal", "joven, lías, crianza"),
        WineTableRow("CLARETE", "monovarietal", "joven, lías, crianza"),
        WineTableRow("ESPUMOSO", "monovarietal", "tradicional"),
        WineTableRow(
            "GENEROSO",
            "monovarietal",
            "biológica (fino o manzanilla), oxidativa (palo cortado, amontillado, oloroso)",
        ),
        WineTableRow("GENEROSO DE LICOR", "monovarietal", "pale cream, medium, cream"),
        WineTableRow("DULCE NATURAL", "monovarietal", "-"),
        WineTableRow("VERMUT", "monovarietal", "-"),
    )

    val candidates: List<CandidateProfile> = listOf(
        CandidateProfile("BLANCO", "joven", "Albariño / Verdejo", "Rías Baixas / Rueda", "Galicia / Castilla y León", "11-13% vol"),
        CandidateProfile("BLANCO", "lías", "Godello / Albariño", "Valdeorras / Rías Baixas", "Galicia", "12-13.5% vol"),
        CandidateProfile("BLANCO", "fermentado en barrica", "Chardonnay / Viura", "Rioja / Penedès", "La Rioja / Cataluña", "12.5-14% vol"),
        CandidateProfile("BLANCO", "crianza", "Viura / Godello", "Rioja / Bierzo", "La Rioja / Castilla y León", "13-14.5% vol"),
        CandidateProfile("TINTO", "joven", "Tempranillo / Garnacha", "Rioja / Ribera del Duero", "La Rioja / Castilla y León", "13-14% vol"),
        CandidateProfile("TINTO", "roble", "Tempranillo / Monastrell", "Ribera del Duero / Jumilla", "Castilla y León / Murcia", "13.5-14.5% vol"),
        CandidateProfile("TINTO", "crianza", "Tempranillo / Mazuelo", "Rioja / Ribera del Duero", "La Rioja / Castilla y León", "13.5-15% vol"),
        CandidateProfile("TINTO", "reserva", "Tempranillo / Graciano", "Rioja / Toro", "La Rioja / Castilla y León", "14-15.5% vol"),
        CandidateProfile("ROSADO", "joven", "Garnacha / Bobal", "Navarra / Cigales", "Navarra / Castilla y León", "12-13.5% vol"),
        CandidateProfile("ROSADO", "lías", "Garnacha / Tempranillo", "Navarra / Rioja", "Navarra / La Rioja", "12.5-13.5% vol"),
        CandidateProfile("ROSADO", "crianza", "Garnacha / Tempranillo", "Rioja / Navarra", "La Rioja / Navarra", "13-14% vol"),
        CandidateProfile("CLARETE", "joven", "Tempranillo / Garnacha", "Cigales", "Castilla y León", "12.5-14% vol"),
        CandidateProfile("CLARETE", "lías", "Tempranillo / Albillo", "Cigales", "Castilla y León", "13-14% vol"),
        CandidateProfile("CLARETE", "crianza", "Tempranillo / Garnacha", "Cigales / Rioja", "Castilla y León / La Rioja", "13.5-14.5% vol"),
        CandidateProfile("ESPUMOSO", "tradicional", "Macabeo / Xarel·lo / Parellada", "Cava", "Cataluña", "11.5-12.5% vol"),
        CandidateProfile("GENEROSO", "biológica (fino o manzanilla)", "Palomino Fino", "Jerez-Xérès-Sherry / Manzanilla-Sanlúcar", "Andalucía", "15-15.5% vol"),
        CandidateProfile("GENEROSO", "oxidativa (palo cortado, amontillado, oloroso)", "Palomino Fino", "Jerez-Xérès-Sherry", "Andalucía", "17-22% vol"),
        CandidateProfile("GENEROSO DE LICOR", "pale cream", "Palomino Fino", "Jerez-Xérès-Sherry", "Andalucía", "15.5-17% vol"),
        CandidateProfile("GENEROSO DE LICOR", "medium", "Palomino + PX", "Jerez-Xérès-Sherry", "Andalucía", "16-19% vol"),
        CandidateProfile("GENEROSO DE LICOR", "cream", "Oloroso + PX", "Jerez-Xérès-Sherry", "Andalucía", "17-20% vol"),
        CandidateProfile("DULCE NATURAL", "-", "Pedro Ximénez / Moscatel", "Málaga / Montilla-Moriles / Jerez", "Andalucía", "15-22% vol"),
        CandidateProfile("VERMUT", "-", "Macabeo / Airén (base aromatizada)", "Reus / Jerez / Madrid", "Cataluña / Andalucía / Madrid", "15-18% vol"),
    )

    val exampleTastings: Map<String, TastingPreset> = linkedMapOf(
        "Blanco joven (ejemplo)" to TastingPreset(
            visualColor = "Amarillo pajizo",
            visualDensidad = "Media",
            visualLagrima = "Rápida",
            visualClaridad = "Limpio",
            visualBurbujas = false,
            olfIntensidad = "Media",
            olfAromasPrimarios = setOf("Cítricos", "Fruta blanca"),
            olfAromasSecundarios = emptySet(),
            olfAromasTerciarios = emptySet(),
            gusSabor = setOf("Seco", "Ácido"),
            gusCuerpo = "Ligero",
            gusAcidez = "Alta",
            gusAlcohol = "Medio (12-14%)",
            gusPersistencia = "Media",
            gusEfervescencia = false,
            gusSalinidad = false,
            gusNotas = "Ejemplo de blanco fresco y frutal.",
        ),
        "Tinto crianza (ejemplo)" to TastingPreset(
            visualColor = "Rojo rubí",
            visualDensidad = "Alta",
            visualLagrima = "Lenta",
            visualClaridad = "Limpio",
            visualBurbujas = false,
            olfIntensidad = "Alta",
            olfAromasPrimarios = setOf("Fruta roja", "Fruta negra"),
            olfAromasSecundarios = emptySet(),
            olfAromasTerciarios = setOf("Vainilla", "Tostado"),
            gusSabor = setOf("Seco", "Tánico"),
            gusCuerpo = "Pleno",
            gusAcidez = "Media",
            gusAlcohol = "Alto (>14%)",
            gusPersistencia = "Larga",
            gusEfervescencia = false,
            gusSalinidad = false,
            gusNotas = "Ejemplo de tinto estructurado con madera.",
        ),
        "Espumoso tradicional (ejemplo)" to TastingPreset(
            visualColor = "Amarillo limón",
            visualDensidad = "Media",
            visualLagrima = "Rápida",
            visualClaridad = "Limpio",
            visualBurbujas = true,
            olfIntensidad = "Media",
            olfAromasPrimarios = setOf("Cítricos", "Floral"),
            olfAromasSecundarios = setOf("Levadura", "Panadería"),
            olfAromasTerciarios = emptySet(),
            gusSabor = setOf("Seco", "Ácido"),
            gusCuerpo = "Ligero",
            gusAcidez = "Alta",
            gusAlcohol = "Bajo (<12%)",
            gusPersistencia = "Media",
            gusEfervescencia = true,
            gusSalinidad = false,
            gusNotas = "Ejemplo de espumoso con burbuja fina.",
        ),
    )
}

object WineFormOptions {
    val visualColors = listOf(
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
    )
    val density = listOf("Selecciona...", "Baja", "Media", "Alta")
    val tears = listOf("Selecciona...", "Lenta", "Rápida", "Ausente")
    val clarity = listOf("Selecciona...", "Limpio", "Turbio")
    val smellIntensity = listOf("Selecciona...", "Débil", "Media", "Alta")
    val primaryAromas = listOf(
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
    )
    val secondaryAromas = listOf("Levadura", "Panadería", "Láctico", "Fermentativo")
    val tertiaryAromas = listOf("Vainilla", "Tostado", "Cuero", "Tabaco", "Frutos secos", "Caramelo", "Oxidativo")
    val flavors = listOf("Dulce", "Seco", "Ácido", "Amargo", "Tánico")
    val body = listOf("Selecciona...", "Ligero", "Medio", "Pleno")
    val acidity = listOf("Selecciona...", "Baja", "Media", "Alta")
    val alcohol = listOf("Selecciona...", "Bajo (<12%)", "Medio (12-14%)", "Alto (>14%)")
    val persistence = listOf("Selecciona...", "Corta", "Media", "Larga")
}

class WineViewModel : ViewModel() {
    var uiState by mutableStateOf(WineUiState())
        private set

    fun update(transform: (WineUiState) -> WineUiState) {
        uiState = transform(uiState).copy(errors = emptyList())
    }

    fun start() {
        uiState = uiState.copy(phase = AppPhase.VISUAL, errors = emptyList())
    }

    fun reset() {
        uiState = WineUiState()
    }

    fun loadExample(name: String) {
        val preset = WineCatalog.exampleTastings[name] ?: return
        uiState = preset.toUiState()
    }

    fun goToPhase(phase: AppPhase) {
        uiState = uiState.copy(phase = phase, errors = emptyList())
    }

    fun submitVisual() {
        val errors = validateVisual(uiState)
        uiState = if (errors.isEmpty()) {
            uiState.copy(phase = AppPhase.OLFATIVA, errors = emptyList())
        } else {
            uiState.copy(errors = errors)
        }
    }

    fun submitOlfativa() {
        val errors = validateOlfativa(uiState)
        uiState = if (errors.isEmpty()) {
            uiState.copy(phase = AppPhase.GUSTATIVA, errors = emptyList())
        } else {
            uiState.copy(errors = errors)
        }
    }

    fun submitGustativaAndCalculate() {
        val errors = validateGustativa(uiState)
        if (errors.isNotEmpty()) {
            uiState = uiState.copy(errors = errors)
            return
        }
        val result = WineClassifier.rank(uiState)
        uiState = uiState.copy(
            errors = emptyList(),
            resultados = result.top3,
            vinoAtipico = result.vinoAtipico,
            phase = AppPhase.RESULTADOS,
        )
    }

    private fun validateVisual(state: WineUiState): List<String> {
        val errors = mutableListOf<String>()
        if (state.visualColor == "Selecciona...") errors += "Selecciona un color en la fase visual."
        if (state.visualColor == "Otro (escribir abajo)" && state.visualColorOtro.isBlank()) {
            errors += "Escribe el color en el campo 'Otro'."
        }
        if (state.visualDensidad == "Selecciona...") errors += "Selecciona la densidad."
        if (state.visualLagrima == "Selecciona...") errors += "Selecciona el comportamiento de la lágrima."
        if (state.visualClaridad == "Selecciona...") errors += "Selecciona la claridad."
        return errors
    }

    private fun validateOlfativa(state: WineUiState): List<String> {
        val errors = mutableListOf<String>()
        if (state.olfIntensidad == "Selecciona...") errors += "Selecciona la intensidad olfativa."
        if (state.olfAromasPrimarios.isEmpty()) errors += "Elige al menos un aroma primario."
        return errors
    }

    private fun validateGustativa(state: WineUiState): List<String> {
        val errors = mutableListOf<String>()
        if (state.gusSabor.isEmpty()) errors += "Selecciona al menos una sensación de sabor."
        if (state.gusCuerpo == "Selecciona...") errors += "Selecciona el cuerpo."
        if (state.gusAcidez == "Selecciona...") errors += "Selecciona la acidez."
        if (state.gusAlcohol == "Selecciona...") errors += "Selecciona la sensación de alcohol."
        if (state.gusPersistencia == "Selecciona...") errors += "Selecciona la persistencia."
        return errors
    }
}

private data class ScoredCandidate(
    val candidate: CandidateProfile,
    val score: Int,
    val reasons: List<String>,
)

object WineClassifier {
    fun rank(state: WineUiState): ClassificationResult {
        return rank(collectObservations(state))
    }

    fun rank(observations: Observations): ClassificationResult {
        val scored = WineCatalog.candidates.map { candidate ->
            val (score, reasons) = scoreCandidate(candidate, observations)
            ScoredCandidate(candidate, score, reasons)
        }.sortedByDescending { it.score }

        val top3Raw = scored.take(3)
        val topScore = top3Raw.firstOrNull()?.score ?: 0
        val isAtipico = topScore < 6

        val safeScores = top3Raw.map { max(it.score, 0) + 1 }
        val totalSafe = safeScores.sum().coerceAtLeast(1)

        val top3 = top3Raw.mapIndexed { index, item ->
            val confidence = (safeScores[index].toDouble() / totalSafe.toDouble()) * 100.0
            RankedWineResult(
                tipo = item.candidate.tipo,
                elaboracion = item.candidate.elaboracion,
                uvaProbable = item.candidate.uvaProbable,
                doSugerida = item.candidate.doSugerida,
                zona = item.candidate.zona,
                graduacionEstimada = item.candidate.graduacionEstimada,
                score = item.score,
                reasons = item.reasons,
                confianzaRelativa = (confidence * 10.0).toInt() / 10.0,
            )
        }

        return ClassificationResult(top3 = top3, vinoAtipico = isAtipico)
    }

    fun collectObservations(state: WineUiState): Observations {
        val color = if (state.visualColor == "Otro (escribir abajo)") {
            state.visualColorOtro.trim()
        } else {
            state.visualColor.trim()
        }
        return Observations(
            color = color,
            densidad = state.visualDensidad.lowercase(),
            lagrima = state.visualLagrima.lowercase(),
            claridad = state.visualClaridad.lowercase(),
            burbujasVisual = state.visualBurbujas,
            intensidadOlfativa = state.olfIntensidad.lowercase(),
            aromasPrimarios = state.olfAromasPrimarios.map { it.lowercase() }.toSet(),
            aromasSecundarios = state.olfAromasSecundarios.map { it.lowercase() }.toSet(),
            aromasTerciarios = state.olfAromasTerciarios.map { it.lowercase() }.toSet(),
            sabor = state.gusSabor.map { it.lowercase() }.toSet(),
            cuerpo = state.gusCuerpo.lowercase(),
            acidez = state.gusAcidez.lowercase(),
            alcohol = state.gusAlcohol.lowercase(),
            persistencia = state.gusPersistencia.lowercase(),
            efervescencia = state.gusEfervescencia,
            salinidad = state.gusSalinidad,
        )
    }

    private fun scoreCandidate(candidate: CandidateProfile, obs: Observations): Pair<Int, List<String>> {
        var score = 0
        val reasons = mutableListOf<String>()

        fun add(points: Int, condition: Boolean, reason: String) {
            if (condition) {
                score += points
                reasons += reason
            }
        }

        val color = obs.color.lowercase()
        val prim = obs.aromasPrimarios
        val sec = obs.aromasSecundarios
        val ter = obs.aromasTerciarios
        val sabor = obs.sabor
        val cuerpo = obs.cuerpo
        val acidez = obs.acidez
        val alcohol = obs.alcohol
        val persist = obs.persistencia
        val hasBubbles = obs.burbujasVisual || obs.efervescencia
        val salinidad = obs.salinidad

        val blancos = arrayOf("amarillo", "dorado", "verdoso", "pajizo", "limón", "limon")
        val tintos = arrayOf("rojo", "rubí", "rubi", "granate", "picota", "violáceo", "violaceo")
        val rosados = arrayOf("rosado", "salmón", "salmon", "frambuesa")
        val claretes = arrayOf("clarete", "cereza", "rubí claro", "rubi claro")
        val ambar = arrayOf("ámbar", "ambar", "caoba", "anaranjado", "miel")

        when (candidate.tipo) {
            "BLANCO" -> {
                add(3, colorContains(color, *blancos), "color dentro de blancos")
                add(2, containsAny(prim, "cítricos", "fruta blanca", "fruta tropical"), "aroma primario frutal/fresco")
                add(2, acidez in setOf("media", "alta"), "acidez compatible con blanco")
                add(1, cuerpo in setOf("ligero", "medio"), "cuerpo ligero o medio")
                add(1, hasAny(sabor, "seco"), "perfil de boca seco")

                when (candidate.elaboracion) {
                    "joven" -> add(2, ter.isEmpty(), "pocos aromas terciarios (estilo joven)")
                    "lías" -> add(3, containsAny(sec, "levadura", "panadería", "panaderia"), "notas de lías/levadura")
                    "fermentado en barrica" -> add(3, containsAny(ter, "vainilla", "tostado"), "notas de barrica (vainilla/tostado)")
                    "crianza" -> {
                        add(3, containsAny(ter, "vainilla", "tostado", "frutos secos"), "terciarios de crianza")
                        add(1, persist in setOf("media", "larga"), "persistencia de vino evolucionado")
                    }
                }
                if (hasBubbles) score -= 2
            }

            "TINTO" -> {
                add(3, colorContains(color, *tintos), "color dentro de tintos")
                add(2, hasAny(sabor, "tánico", "tanico"), "presencia de taninos")
                add(2, cuerpo in setOf("medio", "pleno"), "cuerpo medio/pleno")
                add(2, containsAny(prim, "fruta roja", "fruta negra", "fruta madura"), "aroma de fruta roja/negra")
                add(1, alcohol in setOf("medio (12-14%)", "alto (>14%)"), "alcohol medio o alto")

                when (candidate.elaboracion) {
                    "joven" -> add(2, ter.isEmpty(), "sin madera marcada (joven)")
                    "roble" -> add(2, containsAny(ter, "vainilla", "tostado"), "toque de roble")
                    "crianza" -> add(3, containsAny(ter, "vainilla", "tostado", "cuero", "tabaco"), "terciarios de crianza")
                    "reserva" -> add(
                        4,
                        containsAny(ter, "vainilla", "tostado", "cuero", "tabaco") && persist == "larga",
                        "estructura y evolución de reserva",
                    )
                }
                if (hasBubbles) score -= 3
            }

            "ROSADO" -> {
                add(4, colorContains(color, *rosados), "color rosado/salmón")
                add(2, containsAny(prim, "fruta roja", "floral", "cítricos"), "aroma primario típico de rosado")
                add(1, cuerpo in setOf("ligero", "medio"), "cuerpo de rosado")
                add(1, acidez in setOf("media", "alta"), "acidez fresca")
                add(1, !hasAny(sabor, "tánico", "tanico"), "tanino bajo")

                when (candidate.elaboracion) {
                    "joven" -> add(2, ter.isEmpty(), "perfil joven sin terciarios")
                    "lías" -> add(2, containsAny(sec, "levadura", "panadería", "panaderia") || cuerpo == "medio", "volumen por trabajo sobre lías")
                    "crianza" -> add(2, containsAny(ter, "vainilla", "tostado"), "toque de crianza")
                }
                if (hasBubbles) score -= 1
            }

            "CLARETE" -> {
                add(3, colorContains(color, *claretes) || colorContains(color, *rosados), "tono de clarete")
                add(2, containsAny(prim, "fruta roja", "floral"), "aroma de fruta roja/floral")
                add(2, cuerpo in setOf("medio", "pleno"), "cuerpo con algo de estructura")
                add(1, hasAny(sabor, "tánico", "tanico"), "ligera trama tánica")
                add(1, acidez in setOf("media", "alta"), "acidez equilibrada")

                when (candidate.elaboracion) {
                    "joven" -> add(2, ter.isEmpty(), "perfil joven")
                    "lías" -> add(2, containsAny(sec, "levadura", "panadería", "panaderia"), "notas de lías")
                    "crianza" -> add(2, containsAny(ter, "vainilla", "tostado"), "terciarios de crianza")
                }
                if (hasBubbles) score -= 1
            }

            "ESPUMOSO" -> {
                add(6, hasBubbles, "presencia de burbujas/efervescencia")
                add(2, containsAny(sec, "levadura", "panadería", "panaderia"), "aromas de autólisis (levadura/pan)")
                add(1, acidez == "alta", "acidez alta típica de espumoso")
                add(1, persist in setOf("media", "larga"), "persistencia de burbuja")
                add(1, colorContains(color, *blancos) || colorContains(color, *rosados), "gama de color compatible")
                add(1, cuerpo in setOf("ligero", "medio"), "cuerpo típico de espumoso")
                if (!hasBubbles) score -= 5
            }

            "GENEROSO" -> {
                if (candidate.elaboracion.contains("biológica")) {
                    add(2, colorContains(color, *blancos), "color pálido-dorado")
                    add(2, containsAny(sec, "levadura") || salinidad, "pista biológica (levadura/salinidad)")
                    add(2, hasAny(sabor, "seco"), "perfil seco")
                    add(2, alcohol in setOf("medio (12-14%)", "alto (>14%)"), "sensación alcohólica elevada")
                    add(1, persist in setOf("media", "larga"), "persistencia media-larga")
                } else {
                    add(3, colorContains(color, *ambar), "color ámbar/caoba")
                    add(2, containsAny(ter, "frutos secos", "caramelo", "tabaco", "oxidativo"), "notas oxidativas")
                    add(2, hasAny(sabor, "seco", "amargo"), "boca seca o amargosa")
                    add(2, alcohol == "alto (>14%)", "alcohol alto")
                    add(1, persist == "larga", "larga persistencia")
                }
            }

            "GENEROSO DE LICOR" -> {
                add(2, colorContains(color, *ambar), "color ámbar")
                add(2, alcohol in setOf("medio (12-14%)", "alto (>14%)"), "grado alcohólico medio-alto")
                add(1, persist in setOf("media", "larga"), "persistencia media-larga")
                when (candidate.elaboracion) {
                    "pale cream" -> add(3, hasAny(sabor, "dulce") && acidez in setOf("media", "alta"), "dulzor moderado de pale cream")
                    "medium" -> add(3, hasAny(sabor, "dulce") && hasAny(sabor, "seco", "amargo"), "equilibrio dulce-seco de medium")
                    "cream" -> add(4, hasAny(sabor, "dulce") && cuerpo == "pleno", "dulzor y cuerpo de cream")
                }
            }

            "DULCE NATURAL" -> {
                add(5, hasAny(sabor, "dulce"), "dominante dulce")
                add(2, cuerpo in setOf("medio", "pleno"), "cuerpo medio/pleno")
                add(
                    2,
                    containsAny(prim, "fruta madura", "fruta pasificada") ||
                        containsAny(ter, "caramelo", "frutos secos"),
                    "aromas de fruta madura/pasificada",
                )
                add(1, persist == "larga", "persistencia larga")
                add(1, alcohol in setOf("medio (12-14%)", "alto (>14%)"), "alcohol notable")
            }

            "VERMUT" -> {
                add(3, containsAny(prim, "herbal", "especiado", "botánico", "botanico"), "perfil botánico/especiado")
                add(2, hasAny(sabor, "amargo"), "amargor característico")
                add(2, hasAny(sabor, "dulce", "seco"), "base vínica con equilibrio de dulzor")
                add(1, colorContains(color, *ambar), "color típico de vermut")
                add(1, containsAny(ter, "caramelo"), "toque caramelizado")
                add(1, persist in setOf("media", "larga"), "persistencia")
            }
        }

        return score to reasons
    }

    private fun colorContains(color: String, vararg options: String): Boolean {
        return options.any { color.contains(it, ignoreCase = true) }
    }

    private fun containsAny(values: Set<String>, vararg options: String): Boolean {
        return options.any { option -> values.any { it == option } }
    }

    private fun hasAny(values: Set<String>, vararg options: String): Boolean {
        return containsAny(values, *options)
    }
}

fun reasonText(reasons: List<String>): String {
    if (reasons.isEmpty()) return "coincidencia global de perfil"
    return reasons.take(3).joinToString(", ")
}

