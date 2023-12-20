package com.tim.currencyexchanger.ui.composable

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

private const val OFFSET_DOT = 1

class AmountVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.contains('.') && originalText.length > 1) {
            // if end ###.
            if (originalText.last() == '.') {
                val formattedText = AmountValidation.formatAmount(
                    originalText.substring(0, originalText.length - OFFSET_DOT)
                )
                return TransformedText(
                    text = AnnotatedString("$formattedText."),
                    offsetMapping = object : OffsetMapping {
                        override fun originalToTransformed(offset: Int): Int =
                            formattedText.length + OFFSET_DOT

                        override fun transformedToOriginal(offset: Int): Int =
                            formattedText.length + OFFSET_DOT
                    }
                )
            }

            val amountSplitDot = originalText.split(Regex("[.]"))
            val formattedText = AmountValidation.formatAmount(amountSplitDot.first())
            val lengthAfterDot = amountSplitDot.last().length
            return TransformedText(
                text = AnnotatedString("$formattedText.${amountSplitDot.last()}"),
                offsetMapping = AmountOffsetMapping(
                    formattedText = formattedText,
                    originalText = originalText,
                    dotOffset = OFFSET_DOT,
                    lengthAfterDot = lengthAfterDot
                )
            )
        }

        val formattedText = AmountValidation.formatAmount(originalText)
        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = AmountOffsetMapping(
                formattedText = formattedText,
                originalText = originalText
            )
        )
    }
}