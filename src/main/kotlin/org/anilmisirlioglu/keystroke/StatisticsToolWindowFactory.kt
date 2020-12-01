package org.anilmisirlioglu.keystroke

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerAdapter
import com.intellij.openapi.wm.ex.ToolWindowManagerEx
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import org.anilmisirlioglu.keystroke.ui.components.StatisticsToolWindowComponent
import javax.swing.ScrollPaneConstants

class StatisticsToolWindowFactory : ToolWindowFactory, DumbAware{

    companion object{
        private const val WINDOW_ID = "Key Stroke Statistics"
    }

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

            val toolWindowManagerEx = ToolWindowManagerEx.getInstanceEx(project)
            toolWindowManagerEx.addToolWindowManagerListener(object : ToolWindowManagerAdapter(){

                override fun stateChanged(toolWindowManager: ToolWindowManager){
                    val window = toolWindowManager.getToolWindow(WINDOW_ID)
                    if(window != null){
                        if(!window.isVisible){
                            window.component.run{
                                val panel = JBScrollPane(buildPanel()).apply{
                                    verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
                                }

                                removeAll()
                                add(panel)
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