package org.anilmisirlioglu.keystroke.settings

import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.panels.HorizontalLayout
import com.intellij.util.ui.FormBuilder
import org.anilmisirlioglu.keystroke.MessageBundle
import java.awt.Dimension
import javax.swing.*

// Simple settings form
class SettingsComponent {

    private val settings = SettingsService.getInstance()

    /* Titles */
    private val generalTitledSeparator = TitledSeparator(MessageBundle.message("settings.separator.general"))
    private val settingsTitledSeparator = TitledSeparator("Settings")
    private val keyboardTitledSeparator = TitledSeparator(MessageBundle.message("settings.separator.keyboard"))

    /* Checkboxes */
    private val updateOnEveryKeystrokeCheckbox =
        JCheckBox(MessageBundle.message("settings.checkbox.updateAfterEveryKey"))
    private val allowFunctionKeysCheckbox = JCheckBox(MessageBundle.message("settings.checkbox.keys.function"))
    private val allowCursorControlKeysCheckbox = JCheckBox(MessageBundle.message("settings.checkbox.keys.cursor"))
    private val allowOtherKeysCheckbox = JCheckBox(MessageBundle.message("settings.checkbox.keys.other"))

    private val allowTypingKeysCheckbox = JCheckBox(
        MessageBundle.message("settings.checkbox.keys.typing"),
        true
    ).apply {
        isEnabled = false
    }

    /* Labels */
    private val dailyTargetLabel = JLabel(MessageBundle.message("settings.label.daily.target"))

    /* Inputs */
    private val dailyTargetSpinner = JSpinner(
        SpinnerNumberModel(settings.dailyTarget, 100, Int.MAX_VALUE, 50)
    ).apply {
        val dimension = Dimension(96, 30)

        maximumSize = dimension
        minimumSize = dimension
        preferredSize = dimension
    }

    /* Panels */
    private val dailyTargetPanel = JPanel(HorizontalLayout(12))
        .apply {
            add(dailyTargetLabel)
            add(dailyTargetSpinner)
        }

    val panel: JPanel = FormBuilder.createFormBuilder()
        .addComponent(generalTitledSeparator)
        .addComponentToRightColumn(updateOnEveryKeystrokeCheckbox)
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

    fun apply() {
        settings.updateOnEveryKeystroke = updateOnEveryKeystrokeCheckbox.isSelected
        settings.dailyTarget = dailyTargetSpinner.value as Int

        settings.isAllowedOtherKeys = allowOtherKeysCheckbox.isSelected
        settings.isAllowedCursorControlKeys = allowCursorControlKeysCheckbox.isSelected
        settings.isAllowedFunctionKeys = allowFunctionKeysCheckbox.isSelected
    }

    fun reset() {
        updateOnEveryKeystrokeCheckbox.isSelected = settings.updateOnEveryKeystroke
        dailyTargetSpinner.value = settings.dailyTarget

        allowOtherKeysCheckbox.isSelected = settings.isAllowedOtherKeys
        allowFunctionKeysCheckbox.isSelected = settings.isAllowedFunctionKeys
        allowCursorControlKeysCheckbox.isSelected = settings.isAllowedCursorControlKeys
    }

    fun isModified(): Boolean {
        return allowCursorControlKeysCheckbox.isSelected != settings.isAllowedCursorControlKeys ||
                allowOtherKeysCheckbox.isSelected != settings.isAllowedOtherKeys ||
                allowFunctionKeysCheckbox.isSelected != settings.isAllowedFunctionKeys ||
                dailyTargetSpinner.value != settings.dailyTarget ||
                updateOnEveryKeystrokeCheckbox.isSelected != settings.updateOnEveryKeystroke
    }
}