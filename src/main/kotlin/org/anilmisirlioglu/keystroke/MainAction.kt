package org.anilmisirlioglu.keystroke

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import org.anilmisirlioglu.keystroke.statistics.KeystrokeStatistics

class MainAction : AnAction(){

    override fun actionPerformed(e: AnActionEvent){
        Messages.showMessageDialog(
            e.project,
            "Hello World",
            "Hello World",
            Messages.getInformationIcon()
        )
    }

}