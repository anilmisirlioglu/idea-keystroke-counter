package org.anilmisirlioglu.keystroke.statistics.models

import com.intellij.util.xmlb.annotations.*
import org.anilmisirlioglu.keystroke.statistics.converters.LocalDateTimeConverter
import java.time.LocalDateTime

data class Statistics(
    @Tag("version")
    val version: Int = 1,
    @Tag("startAt")
    @OptionTag(converter = LocalDateTimeConverter::class)
    val startAt: LocalDateTime = LocalDateTime.now(),
    @MapAnnotation(
        surroundKeyWithTag = false,
        surroundValueWithTag = false,
        surroundWithTag = false,
        entryTagName = "year",
        keyAttributeName = "date"
    )
    val years: HashMap<Int, ArrayList<DayStatistic>> = hashMapOf(),
)