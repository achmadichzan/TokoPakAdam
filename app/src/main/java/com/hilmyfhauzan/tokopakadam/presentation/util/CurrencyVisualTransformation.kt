package com.hilmyfhauzan.tokopakadam.presentation.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val digits = originalText.filter { it.isDigit() }
        val formattedText = formatRupiah(digits.toLongOrNull() ?: 0L)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var originalIndex = 0
                var transformedIndex = 0
                
                // We're looking for the position in formattedText corresponding to 'offset' in digits
                // Digits in formattedText map 1:1 to digits in originalText (assuming input is only digits)
                // We skip non-digits in formattedText
                
                while (originalIndex < offset && transformedIndex < formattedText.length) {
                    if (formattedText[transformedIndex].isDigit()) {
                        originalIndex++
                    }
                    transformedIndex++
                }
                
                // If we stopped, we might be before a separator. 
                // In usual "typing forward" UX, we want to jump over the separator.
                while (transformedIndex < formattedText.length && !formattedText[transformedIndex].isDigit()) {
                    transformedIndex++
                }
                
                return transformedIndex.coerceAtMost(formattedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Map a position in formattedText back to originalText (digits only)
                // Just count how many digits appeared before 'offset'
                val clampedOffset = offset.coerceAtMost(formattedText.length)
                var digitsCount = 0
                for (i in 0 until clampedOffset) {
                    if (formattedText[i].isDigit()) {
                        digitsCount++
                    }
                }
                return digitsCount.coerceAtMost(originalText.length)
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}
