package com.example.guesswine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WineClassifierTest {

    @Test
    fun blancoJovenExampleRanksBlancoTop1() {
        val preset = WineCatalog.exampleTastings.getValue("Blanco joven (ejemplo)")
        val result = WineClassifier.rank(preset.toUiState())

        assertTrue(result.top3.isNotEmpty())
        assertEquals("BLANCO", result.top3.first().tipo)
    }

    @Test
    fun tintoCrianzaExampleRanksTintoOnTop() {
        val preset = WineCatalog.exampleTastings.getValue("Tinto crianza (ejemplo)")
        val result = WineClassifier.rank(preset.toUiState())

        assertTrue(result.top3.isNotEmpty())
        assertEquals("TINTO", result.top3.first().tipo)
    }

    @Test
    fun espumosoExampleIncludesEspumosoTop3() {
        val preset = WineCatalog.exampleTastings.getValue("Espumoso tradicional (ejemplo)")
        val result = WineClassifier.rank(preset.toUiState())

        assertTrue(result.top3.any { it.tipo == "ESPUMOSO" })
    }
}
