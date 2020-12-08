package org.anilmisirlioglu.keystroke.statistics

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl
import com.intellij.ui.CaptionPanel
import com.intellij.ui.TitledSeparator
import com.intellij.ui.border.CustomLineBorder
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.FormBuilder
import com.intellij.xml.util.XmlStringUtil
import org.anilmisirlioglu.keystroke.MessageBundle
import org.anilmisirlioglu.keystroke.rebuild.toDecimal
import org.anilmisirlioglu.keystroke.settings.SettingsService
import org.anilmisirlioglu.keystroke.ui.Chart
import org.anilmisirlioglu.keystroke.utils.DateTimeUtils
import javax.swing.*

class StatisticsToolWindowComponent : Disposable{

    private val statistics = StatisticsService.instance

    private val settings = SettingsService.instance

    private val toolbar: ActionToolbarImpl
        get(){
            val group = DefaultActionGroup("main", true).apply{
                add(StatisticsResetAction())
            }

            val toolbar = ActionManager
                .getInstance()
                .createActionToolbar(ActionPlaces.TOOLBAR, group, true) as ActionToolbarImpl

            return toolbar.apply{
                border = CustomLineBorder(CaptionPanel.CNT_ACTIVE_BORDER_COLOR, 0, 0, 1, 0)
            }
        }

    private val overview: FormBuilder
        get(){
            val overviewTitledSeparator = TitledSeparator(
                MessageBundle.message("toolbar.separator.overview")
            )

            val statisticsResetDateLabel = JLabel(
                toHTML(
                    MessageBundle.message("toolbar.label.overview.statistics.reset.date"),
                    DateTimeUtils.parse(statistics.state.startAt)
                )
            )

            val dailyTargetLabel = JLabel(
                toHTML(
                    MessageBundle.message("toolbar.separator.target"),
                    settings.dailyTarget.toDecimal()
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
                    statistics.mostKeystrokeDay?.let{
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

            return FormBuilder
                .createFormBuilder()
                .addComponent(overviewTitledSeparator)
                .addComponentToRightColumn(numberOfKeystrokeTodayLabel)
                .addComponentToRightColumn(dailyTargetLabel)
                .addComponentToRightColumn(statisticsResetDateLabel)
                .addComponentToRightColumn(totalKeystrokeCountLabel)
                .addComponentToRightColumn(mostKeystrokeDateLabel)
                .addComponentToRightColumn(maxKeystrokeCountLabel)
        }

    private val target: FormBuilder
        get(){
            val targetTitledSeparator = TitledSeparator(
                MessageBundle.message("toolbar.separator.target")
            )

            var percentageOfDailyTarget = (statistics.numberOfKeystrokeToday.toDouble() * 100 / settings.dailyTarget) / 100
            if (percentageOfDailyTarget > 1) percentageOfDailyTarget = 1.0

            val targetChart = Chart.buildDialChart(
                MessageBundle.message("toolbar.separator.target"),
                percentageOfDailyTarget
            )

            return FormBuilder
                .createFormBuilder()
                .addComponent(targetTitledSeparator)
                .addComponent(targetChart)
        }

    private val analysis: FormBuilder
        get(){
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

            return FormBuilder
                .createFormBuilder()
                .addComponent(analysisTitledSeparator)
                .addComponent(weeklyStatisticsChart)
                .addComponent(monthlyStatisticsChart)
                .addComponent(yearlyStatisticsChart)
        }

    private val panel: JPanel
        get() = FormBuilder
            .createFormBuilder()
            .addComponentToRightColumn(overview.panel)
            .addVerticalGap(3)
            .addComponentToRightColumn(target.panel)
            .addVerticalGap(3)
            .addComponentToRightColumn(analysis.panel)
            .addComponentFillVertically(JPanel(), 0)
            .panel

    fun buildContent(): JPanel{
        return JPanel().apply{
            layout = BoxLayout(this, BoxLayout.Y_AXIS)

            val scrollPane = JBScrollPane(panel).apply{
                verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
                horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
            }

            add(toolbar)
            add(scrollPane)
        }
    }

    private fun toHTML(text: String, value: Any): String = XmlStringUtil.wrapInHtml("<b>$text:</b> $value")

    override fun dispose(){}

}