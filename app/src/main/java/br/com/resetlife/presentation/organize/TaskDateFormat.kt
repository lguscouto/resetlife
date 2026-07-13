package br.com.resetlife.presentation.organize

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

/**
 * Converts the Brazilian date entered in the UI to the ISO value kept by the
 * existing local persistence layer, and formats stored values back for display.
 */
object TaskDateFormat {
    private val brazilianFormatter = DateTimeFormatter
        .ofPattern("dd/MM/uuuu")
        .withResolverStyle(ResolverStyle.STRICT)
    private val storageFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun formatDigitsForDisplay(value: String): String {
        val digits = value.filter(Char::isDigit).take(8)
        return buildString {
            digits.forEachIndexed { index, digit ->
                if (index == 2 || index == 4) append('/')
                append(digit)
            }
        }
    }

    fun normalizeInput(value: String): String? {
        val rawValue = value.trim()
        if (rawValue.isEmpty()) {
            return null
        }
        val brazilianValue = when {
            rawValue.matches(Regex("\\d{2}/\\d{2}/\\d{4}")) -> rawValue
            rawValue.matches(Regex("\\d{8}")) -> formatDigitsForDisplay(rawValue)
            else -> return null
        }
        return runCatching {
            LocalDate.parse(brazilianValue, brazilianFormatter).format(storageFormatter)
        }.getOrNull()
    }

    fun formatStored(value: String): String? {
        return runCatching {
            LocalDate.parse(value, storageFormatter).format(brazilianFormatter)
        }.getOrNull()
    }
}
