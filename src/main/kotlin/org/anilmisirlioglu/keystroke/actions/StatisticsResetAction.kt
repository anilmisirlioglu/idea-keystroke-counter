package org.anilmisirlioglu.keystroke.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import org.anilmisirlioglu.keystroke.MessageBundle
import org.anilmisirlioglu.keystroke.services.StatisticsService

class StatisticsResetAction : AnAction(
    MessageBundle.message("toolbar.action.overview.reset.statistics.title"),
    MessageBundle.message("toolbar.action.overview.reset.statistics.description"),
    AllIcons.General.Reset
){

    private val statistics = StatisticsService.instance

    override fun actionPerformed(e: AnActionEvent){
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