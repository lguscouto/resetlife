package br.com.resetlife.presentation.components

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Teste unitário (sem Android) que garante que as strings de estado vazio
 * da versão 0.19.0 existem em strings.xml e não estão vazias.
 *
 * Lê o arquivo de recursos diretamente para validar a presença das chaves,
 * cumprindo o requisito de TDD sem depender de emulador/contexto Android.
 */
class EmptyStateStringsTest {

    private val requiredKeys = listOf(
        "empty_organize",
        "empty_organize_hint",
        "empty_environment",
        "empty_environment_hint",
        "empty_customlists",
        "empty_customlists_hint",
        "empty_wellbeing",
        "empty_wellbeing_hint",
        "empty_weekly",
        "empty_weekly_hint",
        "a11y_nav_hoje",
        "a11y_nav_organizar",
        "a11y_nav_vida",
        "a11y_nav_perfil",
    )

    private fun stringsXml(): File {
        // O diretório de trabalho dos testes unitários do Gradle é o módulo (app).
        val candidates = listOf(
            File("src/main/res/values/strings.xml"),
            File("app/src/main/res/values/strings.xml"),
            File("../../../src/main/res/values/strings.xml"),
        )
        return candidates.firstOrNull { it.exists() }
            ?: error("strings.xml não encontrado nos caminhos esperados: $candidates")
    }

    private fun values(): Map<String, String> {
        val factory = DocumentBuilderFactory.newInstance()
        val doc = factory.newDocumentBuilder().parse(stringsXml())
        doc.documentElement.normalize()
        val nodes = doc.getElementsByTagName("string")
        val map = mutableMapOf<String, String>()
        for (i in 0 until nodes.length) {
            val node = nodes.item(i) as Element
            val name = node.getAttribute("name")
            val text = node.textContent.trim()
            if (name.isNotEmpty()) map[name] = text
        }
        return map
    }

    @Test
    fun emptyStateStrings_arePresentAndNonEmpty() {
        val values = values()
        assertTrue("strings.xml deve conter entradas", values.isNotEmpty())

        for (key in requiredKeys) {
            val value = values[key]
            assertNotNull("String ausente: $key", value)
            assertFalse("String vazia: $key", value!!.isEmpty())
        }
    }

    @Test
    fun navContentDescriptionStrings_alreadyExist() {
        // A barra de navegação usa as strings *_nav_description existentes.
        val values = values()
        for (key in listOf(
            "today_nav_description",
            "organize_nav_description",
            "life_nav_description",
            "profile_nav_description",
        )) {
            assertNotNull("String de a11y de navegação ausente: $key", values[key])
            assertFalse("String de a11y de navegação vazia: $key", values[key]!!.isEmpty())
        }
    }
}
