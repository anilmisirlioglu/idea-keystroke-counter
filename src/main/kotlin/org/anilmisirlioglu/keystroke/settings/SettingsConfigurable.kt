package org.anilmisirlioglu.keystroke.settings

import com.intellij.openapi.options.SearchableConfigurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class SettingsConfigurable : SearchableConfigurable{

    private val component: SettingsComponent = SettingsComponent()

    override fun getId(): String = "KeyStrokeConfiguration"

    override fun createComponent(): JComponent = component.panel

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String = "Key Stroke"

    override fun enableSearch(option: String?): Runnable? = null

    override fun getHelpTopic(): String? = null

    override fun apply() = component.apply()

    override fun reset() = component.reset()

    override fun isModified(): Boolean = component.isModified()

}