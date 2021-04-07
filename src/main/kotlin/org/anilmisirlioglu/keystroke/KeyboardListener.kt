package org.anilmisirlioglu.keystroke

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.impl.EditorComponentImpl
import org.anilmisirlioglu.keystroke.settings.SettingsService
import org.anilmisirlioglu.keystroke.statistics.StatisticsService
import java.awt.AWTEvent
import java.awt.Toolkit
import java.awt.event.AWTEventListener
import java.awt.event.KeyEvent

class KeyboardListener : AWTEventListener, Disposable{

    private val statistics = StatisticsService.instance
    private val settings = SettingsService.instance

    init{
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK)
    }

    override fun eventDispatched(event: AWTEvent?){
        if(event is KeyEvent && event.id == KeyEvent.KEY_RELEASED){
            if(!settings.isCountOnlyWorkspace || event.component is EditorComponentImpl){
                when{
                    event.isTypingKey -> statistics.inc()
                    settings.isAllowedFunctionKeys && event.isFunctionKey -> statistics.inc()
                    settings.isAllowedCursorControlKeys && event.isCursorControlKey -> statistics.inc()
                    settings.isAllowedOtherKeys -> statistics.inc()
                }
            }
        }
    }

    override fun dispose(){
        Toolkit.getDefaultToolkit().removeAWTEventListener(this)
    }

    private val KeyEvent.isTypingKey: Boolean
        get() = when(keyCode){
            KeyEvent.VK_UNDEFINED,
            KeyEvent.VK_ESCAPE,
            KeyEvent.VK_TAB,
            KeyEvent.VK_SPACE,
            KeyEvent.VK_DELETE,
            KeyEvent.VK_BACK_SPACE,
            KeyEvent.VK_ENTER -> false
            else -> keyChar != KeyEvent.CHAR_UNDEFINED
        }

    private val KeyEvent.isCursorControlKey: Boolean
        get() = when(keyCode){
            KeyEvent.VK_RIGHT,
            KeyEvent.VK_LEFT,
            KeyEvent.VK_UP,
            KeyEvent.VK_DOWN -> true
            else -> false
        }

    private val KeyEvent.isFunctionKey: Boolean
        get() = when(keyCode){
            KeyEvent.VK_F1,
            KeyEvent.VK_F2,
            KeyEvent.VK_F3,
            KeyEvent.VK_F4,
            KeyEvent.VK_F5,
            KeyEvent.VK_F6,
            KeyEvent.VK_F7,
            KeyEvent.VK_F8,
            KeyEvent.VK_F9,
            KeyEvent.VK_F10,
            KeyEvent.VK_F11,
            KeyEvent.VK_F12,
            KeyEvent.VK_F13,
            KeyEvent.VK_F14,
            KeyEvent.VK_F15,
            KeyEvent.VK_F16,
            KeyEvent.VK_F17,
            KeyEvent.VK_F18,
            KeyEvent.VK_F19,
            KeyEvent.VK_F20,
            KeyEvent.VK_F21,
            KeyEvent.VK_F22,
            KeyEvent.VK_F23,
            KeyEvent.VK_F24 -> true
            else -> false
        }

}