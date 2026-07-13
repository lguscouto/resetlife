package br.com.resetlife.presentation.organize

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/** Displays numeric date input as DD/MM/AAAA without changing the underlying value. */
class BrazilianDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text.filter(Char::isDigit).take(8)
        val transformed = TaskDateFormat.formatDigitsForDisplay(original)
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val safeOffset = offset.coerceIn(0, original.length)
                return when {
                    safeOffset <= 2 -> safeOffset
                    safeOffset <= 4 -> safeOffset + 1
                    else -> (safeOffset + 2).coerceAtMost(transformed.length)
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                val safeOffset = offset.coerceIn(0, transformed.length)
                return when {
                    safeOffset <= 2 -> safeOffset
                    safeOffset <= 5 -> (safeOffset - 1).coerceAtLeast(0)
                    else -> (safeOffset - 2).coerceAtMost(original.length)
                }
            }
        }
        return TransformedText(AnnotatedString(transformed), offsetMapping)
    }
}
