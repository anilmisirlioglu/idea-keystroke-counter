package org.anilmisirlioglu.keystroke.statistics

import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener

class StatisticsWindowListener : ToolWindowManagerListener {

    private val statistics = StatisticsService.getInstance()

    override fun stateChanged(
        toolWindowManager: ToolWindowManager,
        changeType: ToolWindowManagerListener.ToolWindowManagerEventType
    ) {
        super.stateChanged(toolWindowManager, changeType)
        if (toolWindowManager.getToolWindow("Keystroke Statistics")?.isVisible!!) {
            statistics.invokeCallbacks()
        }
    }
}