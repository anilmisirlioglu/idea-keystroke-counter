package org.anilmisirlioglu.keystroke.statistics

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerAdapter
import com.intellij.openapi.wm.ex.ToolWindowManagerEx
import com.intellij.ui.content.ContentFactory

class StatisticsToolWindowFactory : ToolWindowFactory, DumbAware{

    companion object{
        private const val WINDOW_ID = "Key Stroke Statistics"
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow){
        StatisticsToolWindowComponent().run{
            val toolWindowContent = buildContent()
            val contentFactory = ContentFactory.SERVICE.getInstance()
            val content = contentFactory.createContent(toolWindowContent, "", false).apply{
                preferredFocusableComponent = toolWindowContent
                setDisposer(this)
            }

            toolWindow.contentManager.addContent(content)

            val toolWindowManagerEx = ToolWindowManagerEx.getInstanceEx(project)
            toolWindowManagerEx.addToolWindowManagerListener(object : ToolWindowManagerAdapter(){

                override fun stateChanged(toolWindowManager: ToolWindowManager){
                    val window = toolWindowManager.getToolWindow(WINDOW_ID)
                    if (window != null){
                        if (!window.isVisible){
                            val newToolWindowContent = buildContent()

                            window
                                .contentManager
                                .findContent("")
                                .preferredFocusableComponent = newToolWindowContent

                            window.component.run{
                                removeAll()
                                add(newToolWindowContent)
                                revalidate()
                                repaint()
                                updateUI()
                            }
                        }
                    }
                }

            })
        }
    }

}