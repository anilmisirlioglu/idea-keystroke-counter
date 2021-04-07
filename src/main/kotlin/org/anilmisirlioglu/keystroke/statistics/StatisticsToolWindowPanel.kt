package org.anilmisirlioglu.keystroke.statistics

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import com.intellij.openapi.ui.Messages
import com.intellij.ui.CaptionPanel
import com.intellij.ui.TitledSeparator
import com.intellij.ui.border.CustomLineBorder
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.FormBuilder
import com.intellij.xml.util.XmlStringUtil
import org.anilmisirlioglu.keystroke.MessageBundle
import org.anilmisirlioglu.keystroke.rebuild.toDecimal
import org.anilmisirlioglu.keystroke.settings.SettingsService
import org.anilmisirlioglu.keystroke.ui.Chart
import org.anilmisirlioglu.keystroke.utils.DateTimeUtils
import java.awt.BorderLayout
import javax.swing.*

class StatisticsToolWindowPanel : Disposable, JPanel(){

    private val jPanel = JBPanel<JBPanel<*>>().apply{
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
    }

    init{
        this.layout = BorderLayout()
        this.add(jPanel, BorderLayout.CENTER)

        this.run()
    }

    private val statistics = StatisticsService.instance

    private val settings = SettingsService.instance

    private val toolbar: JComponent
        get(){
            val refreshAction = object : AnAction(
                MessageBundle.message("toolbar.action.overview.reload.statistics.title"),
                MessageBundle.message("toolbar.action.overview.reload.statistics.description"),
                AllIcons.Actions.Refresh
            ){
                override fun actionPerformed(e: AnActionEvent){
                    e.project.run{ this@StatisticsToolWindowPanel.run() }
                }

                override fun displayTextInToolbar(): Boolean = false
            }

            val resetAction = object : AnAction(
                MessageBundle.message("toolbar.action.overview.reset.statistics.title"),
                MessageBundle.message("toolbar.action.overview.reset.statistics.description"),
                AllIcons.General.Reset
            ){

                override fun actionPerformed(e: AnActionEvent) {
                    val dialog = Messages.showYesNoDialog(
                        MessageBundle.message("toolbar.action.dialog.statistics.title"),
                        MessageBundle.message("toolbar.action.dialog.statistics.text"),
                        Messages.getQuestionIcon()
                    )

                    if(dialog == Messages.YES){
                        statistics.reset()
                    }
                }

            }

            val group = DefaultActionGroup("main", true).apply{
                add(refreshAction)
                addSeparator()
                add(resetAction)
            }

            return ActionManager
                .getInstance()
                .createActionToolbar(ActionPlaces.TOOLBAR, group, true).apply{
                    border = CustomLineBorder(CaptionPanel.CNT_ACTIVE_BORDER_COLOR, 0, 0, 1, 0)
                }
                .component
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

    private fun run(){
        val task = object : Task.Backgroundable(
            null,
            MessageBundle.message("toolbar.tasks.reload.title")
        ){

            override fun run(indicator: ProgressIndicator){
                ApplicationManager.getApplication().runReadAction{
                    SwingUtilities.invokeLater{
                        this@StatisticsToolWindowPanel.deActive()
                    }

                    SwingUtilities.invokeLater{
                        this@StatisticsToolWindowPanel.active()
                    }
                }
            }

        }

        ProgressManager.getInstance().runProcessWithProgressAsynchronously(
            task,
            BackgroundableProcessIndicator(task)
        )
    }

    private fun toHTML(text: String, value: Any): String = XmlStringUtil.wrapInHtml("<b>$text:</b> $value")

    private fun buildContent(): JBScrollPane{
        add(toolbar, BorderLayout.NORTH)

        return JBScrollPane(panel).apply{
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        }
    }

    private fun active(){
        this.jPanel.add(buildContent(), BorderLayout.CENTER)
    }

    private fun deActive(){
        this.jPanel.removeAll()
    }

    override fun dispose(){}

}