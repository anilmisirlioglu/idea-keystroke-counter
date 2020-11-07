package org.anilmisirlioglu.keystroke

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import org.anilmisirlioglu.keystroke.services.StatisticsService

class MainAction : AnAction(){

    var statistics = StatisticsService.instance

    override fun actionPerformed(e: AnActionEvent){
        statistics.inc()

        Messages.showMessageDialog(
            e.project,
            "Hello World",
            "Hello World",
            Messages.getInformationIcon()
        )
    }

}