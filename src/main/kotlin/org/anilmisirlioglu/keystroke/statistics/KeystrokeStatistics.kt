package org.anilmisirlioglu.keystroke.statistics

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Transient
import org.anilmisirlioglu.keystroke.statistics.models.DayStatistic
import org.anilmisirlioglu.keystroke.statistics.models.Statistics
import java.util.*

@State(
    name = "KeystrokeStatistics",
    storages = [
        Storage("keystrokes.xml")
    ]
)
class KeystrokeStatistics : PersistentStateComponent<Statistics>{

    companion object{
        val instance: KeystrokeStatistics = ServiceManager.getService(KeystrokeStatistics::class.java)
    }

    private val state: Statistics = Statistics()

    override fun getState(): Statistics = state

    override fun loadState(state: Statistics) = XmlSerializerUtil.copyBean(state, this)

    // TODO::Remove
    @Transient
    fun run(){
        val calendar = Calendar.getInstance()

        this.state.years[calendar.get(Calendar.YEAR)] = arrayListOf(
            DayStatistic(
                calendar.get(Calendar.DAY_OF_YEAR)
            ),
            DayStatistic(
                calendar.get(Calendar.DAY_OF_YEAR + 1),
                10
            )
        )
    }

    // TODO::Not implemented
    fun reset(){
        synchronized(state){}
    }


}