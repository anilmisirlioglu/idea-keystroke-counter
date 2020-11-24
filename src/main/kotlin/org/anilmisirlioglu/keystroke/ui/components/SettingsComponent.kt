package org.anilmisirlioglu.keystroke.ui.components

import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.panels.HorizontalLayout
import com.intellij.util.ui.FormBuilder
import org.anilmisirlioglu.keystroke.MessageBundle
import org.anilmisirlioglu.keystroke.services.SettingsService
import java.awt.Dimension
import javax.swing.*


// Simple settings form
class SettingsComponent{

    private val settings = SettingsService.instance

    /* Titles */
    private val generalTitledSeparator = TitledSeparator(
        MessageBundle.message("settings.separator.general")
    )
    private val settingsTitledSeparator = TitledSeparator("Settings")
    private val keyboardTitledSeparator = TitledSeparator(
        MessageBundle.message("settings.separator.keyboard")
    )

    /* Checkboxes */
    private val countOnlyWorkspaceCheckbox = JCheckBox(
        MessageBundle.message("settings.checkbox.workspace")
    )
    private val allowFunctionKeysCheckbox = JCheckBox(
        MessageBundle.message("settings.checkbox.keys.function")
    )
    private val allowCursorControlKeysCheckbox = JCheckBox(
        MessageBundle.message("settings.checkbox.keys.cursor")
    )
    private val allowOtherKeysCheckbox = JCheckBox(
        MessageBundle.message("settings.checkbox.keys.other")
    )
    private val allowTypingKeysCheckbox = JCheckBox(
        MessageBundle.message("settings.checkbox.keys.typing"),
        true
    ).apply{
        isEnabled = false
    }

    /* Labels */
    private val dailyTargetLabel = JLabel(
        MessageBundle.message("settings.label.daily.target")
    )

    /* Inputs */
    private val dailyTargetSpinner = JSpinner(
        SpinnerNumberModel(settings.dailyTarget, 100, Int.MAX_VALUE, 50)
    ).apply{
        val dimension = Dimension(96, 30)

        maximumSize = dimension
        minimumSize = dimension
        preferredSize = dimension
    }

    /* Panels */
    private val dailyTargetPanel = JPanel(HorizontalLayout(12)).apply{
        add(dailyTargetLabel)
        add(dailyTargetSpinner)
    }

    val panel: JPanel

    init{
        panel = FormBuilder.createFormBuilder()
            .addComponent(generalTitledSeparator)
            .addComponentToRightColumn(countOnlyWorkspaceCheckbox)
            .addVerticalGap(10)
            .addComponent(keyboardTitledSeparator)
            .addComponentToRightColumn(allowTypingKeysCheckbox)
            .addComponentToRightColumn(allowFunctionKeysCheckbox)
            .addComponentToRightColumn(allowCursorControlKeysCheckbox)
            .addComponentToRightColumn(allowOtherKeysCheckbox)
            .addVerticalGap(10)
            .addComponent(settingsTitledSeparator)
            .addComponentToRightColumn(dailyTargetPanel)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun apply(){
        settings.isCountOnlyWorkspace = countOnlyWorkspaceCheckbox.isSelected
        settings.dailyTarget = dailyTargetSpinner.value as Int

        settings.isAllowedOtherKeys = allowOtherKeysCheckbox.isSelected
        settings.isAllowedCursorControlKeys = allowCursorControlKeysCheckbox.isSelected
        settings.isAllowedFunctionKeys = allowFunctionKeysCheckbox.isSelected
    }

    fun reset(){
        countOnlyWorkspaceCheckbox.isSelected = settings.isCountOnlyWorkspace
        dailyTargetSpinner.value = settings.dailyTarget

        allowOtherKeysCheckbox.isSelected = settings.isAllowedOtherKeys
        allowFunctionKeysCheckbox.isSelected = settings.isAllowedFunctionKeys
        allowCursorControlKeysCheckbox.isSelected = settings.isAllowedCursorControlKeys
    }

    fun isModified(): Boolean{
        return countOnlyWorkspaceCheckbox.isSelected != settings.isCountOnlyWorkspace ||
                allowOtherKeysCheckbox.isSelected != settings.isAllowedOtherKeys ||
                allowFunctionKeysCheckbox.isSelected != settings.isAllowedFunctionKeys ||
                allowCursorControlKeysCheckbox.isSelected != settings.isAllowedCursorControlKeys ||
                dailyTargetSpinner.value != settings.dailyTarget

    }

}