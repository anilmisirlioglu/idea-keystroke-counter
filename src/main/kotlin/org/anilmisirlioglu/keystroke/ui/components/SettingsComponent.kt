package org.anilmisirlioglu.keystroke.ui.components

import com.intellij.ui.TitledSeparator
import com.intellij.util.ui.FormBuilder
import org.anilmisirlioglu.keystroke.MessageBundle
import org.anilmisirlioglu.keystroke.services.SettingsService
import javax.swing.JCheckBox
import javax.swing.JPanel

// Simple settings form
class SettingsComponent{

    private val settings = SettingsService.instance

    /* Titles */
    private val generalTitledSeparator = TitledSeparator(
        MessageBundle.message("settings.separator.general")
    )
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

    val panel: JPanel

    init{
        panel = FormBuilder.createFormBuilder()
            .addComponent(generalTitledSeparator)
            .addComponentToRightColumn(countOnlyWorkspaceCheckbox)
            .addComponent(keyboardTitledSeparator)
            .addComponentToRightColumn(allowTypingKeysCheckbox)
            .addComponentToRightColumn(allowFunctionKeysCheckbox)
            .addComponentToRightColumn(allowCursorControlKeysCheckbox)
            .addComponentToRightColumn(allowOtherKeysCheckbox)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun apply(){
        settings.isCountOnlyWorkspace = countOnlyWorkspaceCheckbox.isSelected

        settings.isAllowedOtherKeys = allowOtherKeysCheckbox.isSelected
        settings.isAllowedCursorControlKeys = allowCursorControlKeysCheckbox.isSelected
        settings.isAllowedFunctionKeys = allowFunctionKeysCheckbox.isSelected
    }

    fun reset(){
        countOnlyWorkspaceCheckbox.isSelected = settings.isCountOnlyWorkspace

        allowOtherKeysCheckbox.isSelected = settings.isAllowedOtherKeys
        allowFunctionKeysCheckbox.isSelected = settings.isAllowedFunctionKeys
        allowCursorControlKeysCheckbox.isSelected = settings.isAllowedCursorControlKeys
    }

    fun isModified(): Boolean{
        return countOnlyWorkspaceCheckbox.isSelected != settings.isCountOnlyWorkspace ||
                allowOtherKeysCheckbox.isSelected != settings.isAllowedOtherKeys ||
                allowFunctionKeysCheckbox.isSelected != settings.isAllowedFunctionKeys ||
                allowCursorControlKeysCheckbox.isSelected != settings.isAllowedCursorControlKeys

    }

}