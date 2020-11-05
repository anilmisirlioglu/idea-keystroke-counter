package org.anilmisirlioglu.keystroke.statistics.models

import com.intellij.util.xmlb.annotations.Tag

@Tag("day")
data class DayStatistic(
    @Tag("date")
    val date: Int = 0,
    @Tag("counter")
    val counter: Int = 0
)