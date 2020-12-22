package com.improvefuture.blt.backlog.utility

import java.text.NumberFormat
import java.util.*

object NumberUtility {
    fun format(number: Number, locale: Locale = Locale.US): String {
        return NumberFormat.getNumberInstance(locale).format(number)
    }
}