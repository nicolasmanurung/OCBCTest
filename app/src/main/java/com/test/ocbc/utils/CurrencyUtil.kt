package com.test.ocbc.utils

import java.text.DecimalFormat

object CurrencyUtil {
    fun Double.toDollar(): String {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(this)
    }
}