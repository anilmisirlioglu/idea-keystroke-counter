package org.anilmisirlioglu.keystroke.rebuild

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Int.toDecimal(): String {
    val decimalFormatSymbols = DecimalFormatSymbols().apply {
        decimalSeparator = '.'
    }

    return DecimalFormat("#,###.##", decimalFormatSymbols).run {
        format(this@toDecimal)
    }
}