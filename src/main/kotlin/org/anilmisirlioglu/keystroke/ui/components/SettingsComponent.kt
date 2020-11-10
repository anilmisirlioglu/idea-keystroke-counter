package org.anilmisirlioglu.keystroke.ui.components

import com.intellij.ui.TitledSeparator
import com.intellij.util.ui.FormBuilder
import org.anilmisirlioglu.keystroke.services.SettingsService
import javax.swing.JCheckBox
import javax.swing.JPanel

// Simple settings form
class SettingsComponent{

    private val settings = SettingsService.instance

    /* Titles */
    private val generalTitledSeparator = TitledSeparator("General")
    private val keyboardTitledSeparator = TitledSeparator("Keyboard")

    // TODO::Translate english
    /* Checkboxes */
    private val countOnlyWorkspaceCheckbox = JCheckBox("Count keystrokes pressed only in the workspace")
    private val allowFunctionKeysCheckbox = JCheckBox("Fonksiyon tuşlarının sayımına izin ver")
    private val allowCursorControlKeysCheckbox = JCheckBox("İmleç kontrol tuşlarının sayımına izin ver")
    private val allowOtherKeysCheckbox = JCheckBox("Diğer tuşların sayımına izin ver")
    private val allowTypingKeysCheckbox = JCheckBox("Yazma tuşlarına izin ver", true).apply{
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