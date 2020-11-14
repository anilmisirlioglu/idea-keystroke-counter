package org.anilmisirlioglu.keystroke.models

import com.intellij.util.xmlb.annotations.*
import org.anilmisirlioglu.keystroke.converters.LocalDateTimeConverter
import java.time.LocalDateTime
import kotlin.collections.HashMap

data class Statistics(
    @Tag("version")
    val version: Int = 1,
    @Tag("startAt")
    @OptionTag(converter = LocalDateTimeConverter::class)
    val startAt: LocalDateTime = LocalDateTime.now(),
    @Tag("years")
    var years: HashMap<Int, HashMap<Int, Int>> = hashMapOf()
)