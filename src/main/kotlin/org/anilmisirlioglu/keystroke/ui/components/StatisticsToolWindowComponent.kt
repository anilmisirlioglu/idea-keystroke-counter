package org.anilmisirlioglu.keystroke.ui.components

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl
import com.intellij.ui.TitledSeparator
import com.intellij.util.ui.FormBuilder
import com.intellij.xml.util.XmlStringUtil
import org.anilmisirlioglu.keystroke.MessageBundle
import org.anilmisirlioglu.keystroke.actions.StatisticsResetAction
import org.anilmisirlioglu.keystroke.rebuild.toDecimal
import org.anilmisirlioglu.keystroke.services.StatisticsService
import org.anilmisirlioglu.keystroke.ui.utils.Chart
import org.anilmisirlioglu.keystroke.utils.DateTimeUtils
import javax.swing.JLabel
import javax.swing.JPanel

class StatisticsToolWindowComponent : Disposable{

    val panel: JPanel

    private val statistics = StatisticsService.instance

    private val toolbar: ActionToolbarImpl = run{
        val group = DefaultActionGroup("main", true).apply{
            add(StatisticsResetAction())
        }

        ActionManager
            .getInstance()
            .createActionToolbar(ActionPlaces.TOOLBAR, group, true) as ActionToolbarImpl
    }

    private val overview: FormBuilder = run{
        val overviewTitledSeparator = TitledSeparator(
            MessageBundle.message("toolbar.separator.overview")
        )

        val statisticsResetDateLabel = JLabel(
            toHTML(
                MessageBundle.message("toolbar.label.overview.statistics.reset.date"),
                DateTimeUtils.parse(statistics.state.startAt)
            )
        )
        val numberOfKeystrokeTodayLabel = JLabel(
            toHTML(
                MessageBundle.message("toolbar.label.overview.today.keystroke.count"),
                statistics.numberOfKeystrokeToday.toDecimal()
            )
        )

        val totalKeystrokeCountLabel = JLabel(
            toHTML(
                MessageBundle.message("toolbar.label.overview.total.keystroke.count"),
                statistics.totalKeystrokeCount.toDecimal()
            )
        )

        val mostKeystrokeDateLabel = JLabel(
            toHTML(
                MessageBundle.message("toolbar.label.overview.most.keystroke.date"),
                statistics.mostKeystrokeDay?.let {
                    DateTimeUtils.parse(it)
                } ?: MessageBundle.message("toolbar.label.overview.insufficient.data")
            )
        )

        val maxKeystrokeCountLabel = JLabel(
            toHTML(
                MessageBundle.message("toolbar.label.overview.max.keystroke.count"),
                statistics.maxKeystrokeCount.toDecimal()
            )
        )

        FormBuilder
            .createFormBuilder()
            .addComponent(overviewTitledSeparator)
            .addComponentToRightColumn(numberOfKeystrokeTodayLabel)
            .addComponentToRightColumn(statisticsResetDateLabel)
            .addComponentToRightColumn(totalKeystrokeCountLabel)
            .addComponentToRightColumn(mostKeystrokeDateLabel)
            .addComponentToRightColumn(maxKeystrokeCountLabel)
    }

    private val analysis: FormBuilder = run{
        val datasets = statistics.datasets

        val analysisTitledSeparator = TitledSeparator(
            MessageBundle.message("toolbar.separator.analysis")
        )

        val rangeOfDays = 1..7
        val weeklyStatisticsChart = Chart.buildXYChart(
            MessageBundle.message("toolbar.chart.statistics.weekly"),
            MessageBundle.message("toolbar.chart.statistics.days"),
            MessageBundle.message("toolbar.chart.statistics.strokes.count"),
            rangeOfDays.toList(),
            datasets.weekly
        ).apply{
            val days = MessageBundle.message("days").split(',')
            val xMarkMap = mutableMapOf<Any, Any>()
            rangeOfDays.forEach{ xMarkMap[it] = days[it - 1] }

            chart.setCustomXAxisTickLabelsMap(xMarkMap)
        }

        val monthlyStatisticsChart = Chart.buildCategoryChart(
            MessageBundle.message("toolbar.chart.statistics.monthly"),
            MessageBundle.message("toolbar.chart.statistics.days"),
            MessageBundle.message("toolbar.chart.statistics.strokes.count"),
            (1..datasets.monthly.size).toList(),
            datasets.monthly
        )

        val rangeOfMonths = 1..12
        val yearlyStatisticsChart = Chart.buildXYChart(
            MessageBundle.message("toolbar.chart.statistics.yearly"),
            MessageBundle.message("toolbar.chart.statistics.months"),
            MessageBundle.message("toolbar.chart.statistics.strokes.count"),
            rangeOfMonths.toList(),
            datasets.yearly
        ).apply{
            val months = MessageBundle.message("months").split(',')
            val xMarkMap = mutableMapOf<Any, Any>()
            rangeOfMonths.forEach{ xMarkMap[it] = months[it - 1] }

            chart.setCustomXAxisTickLabelsMap(xMarkMap)
        }

        FormBuilder
            .createFormBuilder()
            .addComponent(analysisTitledSeparator)
            .addComponent(weeklyStatisticsChart)
            .addComponent(monthlyStatisticsChart)
            .addComponent(yearlyStatisticsChart)
    }

    init{
        panel = FormBuilder
            .createFormBuilder()
            .addComponent(toolbar)
            .addComponentToRightColumn(overview.panel)
            .addVerticalGap(3)
            .addComponentToRightColumn(analysis.panel)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    private fun toHTML(text: String, value: Any): String = XmlStringUtil.wrapInHtml("<b>$text:</b> $value")

    override fun dispose(){}

}