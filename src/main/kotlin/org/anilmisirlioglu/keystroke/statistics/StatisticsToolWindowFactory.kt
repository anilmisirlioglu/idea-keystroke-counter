package org.anilmisirlioglu.keystroke.statistics

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class StatisticsToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowContent = StatisticsToolWindowPanel()

        val contentFactory = ContentFactory.getInstance()

        val content = contentFactory.createContent(toolWindowContent, "", false).apply {
            preferredFocusableComponent = toolWindowContent
            setDisposer(this)
        }

        toolWindow.contentManager.addContent(content)
    }

}