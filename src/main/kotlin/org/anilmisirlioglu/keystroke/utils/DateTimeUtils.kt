package org.anilmisirlioglu.keystroke.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils{

    fun parse(value: LocalDateTime): String = value.format(
        DateTimeFormatter.ofPattern("dd MMMM yyyy EEEE, H:m:s")
    )

    fun parse(value: LocalDate): String = value.format(
        DateTimeFormatter.ofPattern("dd MMMM yyyy EEEE")
    )

}