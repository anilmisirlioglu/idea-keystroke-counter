package org.anilmisirlioglu.keystroke.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import org.anilmisirlioglu.keystroke.models.Statistics
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.util.*

@State(
    name = "KeystrokeStatistics",
    storages = [
        Storage("keystroke.statistics.xml")
    ]
)
class StatisticsService : PersistentStateComponent<Statistics>{

    companion object{
        val instance: StatisticsService = ServiceManager.getService(StatisticsService::class.java)
    }

    private var state: Statistics = Statistics()

    override fun initializeComponent(){
        val year = Calendar.getInstance().get(Calendar.YEAR)
        if(!state.years.containsKey(year) || state.years[year] === null){
            state.years[year] = hashMapOf()
        }

        // If the IDE is open at the start of the new year
        if(!state.years.containsKey(year + 1) || state.years[year + 1] === null){
            state.years[year + 1] = hashMapOf()
        }
    }

    override fun getState(): Statistics = state

    override fun loadState(state: Statistics){
        this.state = state
    }

    fun inc(){
        synchronized(state){
            val calendar = Calendar.getInstance()

            state.years[calendar.get(Calendar.YEAR)]?.run{
                val day = calendar.get(Calendar.DAY_OF_YEAR)
                when(val count = this[day]){
                    null -> this[day] = 1
                    else -> this[day] = count + 1
                }
            }
        }
    }

    fun reset(){
        synchronized(state){
            state.years = hashMapOf()
            state.startAt = LocalDateTime.now()
        }
    }

    val numberOfKeystrokeToday: Int
        get(){
            val calendar = Calendar.getInstance()

            return state.years[calendar.get(Calendar.YEAR)]?.get(calendar.get(Calendar.DAY_OF_YEAR)) ?: 0
        }

    val totalKeystrokeCount: Int
        get() = state.years.values.map{ it.values.toList().sum() }.sum()

    val maxKeystrokeCount: Int
        get() = state.years.values.map{ it.values.max() ?: 0 }.max() ?: 0

    val mostKeystrokeDay: LocalDate?
        get(){
            var localDate: LocalDate? = null
            for((year, days) in state.years){
                val keys = days.filterValues{ it == maxKeystrokeCount }.keys

                if(keys.isNotEmpty()){
                    localDate = Year.of(year).atDay(keys.first())
                }
            }

            return localDate
        }

    val datasets: Statistics.Datasets
        get(){
            val calendar = Calendar.getInstance()
            val year = state.years[calendar.get(Calendar.YEAR)] ?: hashMapOf()

            val intervalOfWeek = run{
                val cWeek = calendar.clone() as Calendar

                cWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                val startOfWeek = cWeek.get(Calendar.DAY_OF_YEAR)

                cWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                val finishOfWeek = cWeek.get(Calendar.DAY_OF_YEAR)

                startOfWeek..finishOfWeek
            }

            val intervalOfMonth = run{
                val cMonth = calendar.clone() as Calendar

                cMonth.set(Calendar.DAY_OF_MONTH, 1)
                val startOfMonth = cMonth.get(Calendar.DAY_OF_YEAR)

                cMonth.set(Calendar.DAY_OF_MONTH, cMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
                val finishOfMonth = cMonth.get(Calendar.DAY_OF_YEAR)

                startOfMonth..finishOfMonth
            }

            val weekly = MutableList(7){ 0 }
            val monthly = MutableList(intervalOfMonth.count()){ 0 }
            val yearly = MutableList(12){ 0 }
            for((day, value) in year){
                if(day in intervalOfWeek) weekly[intervalOfWeek.indexOf(day)] = value
                if(day in intervalOfMonth) monthly[intervalOfMonth.indexOf(day)] = value

                calendar.set(Calendar.DAY_OF_YEAR, day)
                yearly[calendar.get(Calendar.MONTH)] += value
            }

            return Statistics.Datasets(weekly, monthly, yearly)
        }

}