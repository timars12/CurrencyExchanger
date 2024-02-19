package com.tim.currencyexchanger.ui.currencyExchang.composable

import androidx.compose.ui.text.input.OffsetMapping

private const val OFFSET_COMMA = 1

class AmountOffsetMapping(
    private val formattedText: String,
    private val originalText: String,
    private val dotOffset: Int = 0,
    private val lengthAfterDot: Int = 0
) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        val comma = formattedText.count { it == ',' }
        val calculationOffset = when {
            offset == originalText.length -> formattedText.length + dotOffset + lengthAfterDot
            formattedText.contains(',') && formattedText.length - comma >= offset -> {
                val substring = formattedText.substring(0, offset)
                val commaSubstring = substring.count { it == ',' }
                val substringWithOffsetComma = formattedText.substring(0, offset + commaSubstring)
                val commaSecondSubstring = substringWithOffsetComma.count { it == ',' }
                if (substringWithOffsetComma.isNotBlank() && substringWithOffsetComma.last() == ',' ||
                    commaSecondSubstring > commaSubstring
                ) {
                    substringWithOffsetComma.length + OFFSET_COMMA
                } else {
                    substringWithOffsetComma.length
                }
            }
            else -> offset + comma
        }
        return when {
            calculationOffset > formattedText.length + dotOffset + lengthAfterDot -> offset
            else -> calculationOffset
        }
    }

    override fun transformedToOriginal(offset: Int): Int {
        val calculationOffset: Int
        var commas = 0
        if (formattedText.contains(',')) {
            if (formattedText.length >= offset) {
                val subString = formattedText.substring(0, offset)
                commas = subString.count { it == ',' }
                if (subString.isNotBlank() && subString.last() == ',') commas -= 1
            } else {
                commas = formattedText.count { it == ',' }
            }
        }
        calculationOffset = offset - commas
        return if (calculationOffset >= 1) calculationOffset else offset
    }
}