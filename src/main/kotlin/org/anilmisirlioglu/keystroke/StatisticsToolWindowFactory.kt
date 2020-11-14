package org.anilmisirlioglu.keystroke

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import org.anilmisirlioglu.keystroke.ui.components.StatisticsToolWindowComponent
import javax.swing.ScrollPaneConstants

class StatisticsToolWindowFactory : ToolWindowFactory, DumbAware{

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow){
        StatisticsToolWindowComponent().run{
            val toolWindowContent = JBScrollPane(panel).apply{
                verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            }

            val contentFactory = ContentFactory.SERVICE.getInstance()
            val content = contentFactory.createContent(toolWindowContent, null, false).apply{
                setPreferredFocusedComponent{
                    toolWindowContent
                }
                setDisposer(this)
            }

            toolWindow.contentManager.addContent(content)
        }
    }

}