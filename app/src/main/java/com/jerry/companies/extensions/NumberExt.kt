package com.jerry.companies.extensions

import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10

val Float.prettyCount: String? get() {
    val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
    val numValue = toLong()
    val value = floor(log10(numValue.toDouble())).toInt()
    val base = value / 3
    return when (value >= 3 && base < suffix.size) {
        true -> DecimalFormat("#0.0").format(
            numValue / Math.pow(
                10.0,
                (base * 3).toDouble()
            )
        ) + suffix[base]
        else -> DecimalFormat("#,##0").format(numValue)
    }
}