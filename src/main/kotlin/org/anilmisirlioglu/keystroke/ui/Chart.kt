package org.anilmisirlioglu.keystroke.ui

import com.intellij.ui.JBColor
import org.knowm.xchart.*
import java.awt.Color

object Chart {

    fun buildXYChart(
        title: String,
        xAxisTitle: String = "X",
        yAxisTitle: String = "Y",
        xData: List<Any>,
        yData: List<Number>
    ): XChartPanel<XYChart> {
        // Create Chart
        val chart: XYChart = XYChartBuilder()
            .width(400)
            .height(200)
            .title(title)
            .xAxisTitle(xAxisTitle)
            .yAxisTitle(yAxisTitle)
            .build()

        with(chart) {
            // Customize Chart
            with(styler) {
                defaultSeriesRenderStyle = XYSeries.XYSeriesRenderStyle.Line
                chartBackgroundColor = JBColor.PanelBackground
                chartFontColor = JBColor.black
                axisTickLabelsColor = JBColor.black
                plotBackgroundColor = JBColor.white
                seriesColors = arrayOf(JBColor.black)

                xAxisLabelRotation = 45

                isXAxisTitleVisible = false
                isLegendVisible = false
            }

            // Series
            addSeries(".", xData, yData)
        }

        return XChartPanel(chart)
    }

    fun buildCategoryChart(
        title: String,
        xAxisTitle: String = "X",
        yAxisTitle: String = "Y",
        xData: List<Any>,
        yData: List<Number>
    ): XChartPanel<CategoryChart> {
        // Create Chart
        val chart: CategoryChart = CategoryChartBuilder()
            .width(400)
            .height(200)
            .title(title)
            .xAxisTitle(xAxisTitle)
            .yAxisTitle(yAxisTitle)
            .build()

        with(chart) {
            // Customize Chart
            with(styler) {
                chartBackgroundColor = JBColor.PanelBackground
                chartFontColor = JBColor.black
                axisTickLabelsColor = JBColor.black
                plotBackgroundColor = JBColor.white
                seriesColors = arrayOf(JBColor.black)

                xAxisLabelRotation = 90

                isXAxisTitleVisible = false
                isLegendVisible = false
            }

            // Series
            addSeries(".", xData, yData)
        }

        return XChartPanel(chart)
    }

    fun buildDialChart(
        title: String,
        value: Double
    ): XChartPanel<DialChart> {
        val chart: DialChart = DialChartBuilder()
            .width(400)
            .height(200)
            .title(title)
            .build()

        with(chart) {
            with(styler) {
                chartBackgroundColor = JBColor.PanelBackground
                chartFontColor = JBColor.black
                plotBackgroundColor = JBColor.white
                plotBorderColor = JBColor.PanelBackground

                isChartTitleVisible = false
                isLegendVisible = false

                lowerTo = 0.3
                lowerColor = Color.RED

                upperFrom = 0.7
                upperColor = Color.GREEN
            }

            addSeries(".", value)
        }

        return XChartPanel(chart)
    }

}