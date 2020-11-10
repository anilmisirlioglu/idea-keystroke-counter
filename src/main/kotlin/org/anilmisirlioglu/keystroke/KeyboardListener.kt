package org.anilmisirlioglu.keystroke

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.impl.EditorComponentImpl
import org.anilmisirlioglu.keystroke.services.SettingsService
import org.anilmisirlioglu.keystroke.services.StatisticsService
import java.awt.AWTEvent
import java.awt.Event
import java.awt.Toolkit
import java.awt.event.AWTEventListener
import java.awt.event.KeyEvent

class KeyboardListener : AWTEventListener, Disposable{

    private val statistics = StatisticsService.instance
    private val settings = SettingsService.instance

    init{
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK)
    }

    /**
     * TODO
     *
     * Ayarlar ekle
     * CTLR, ESC gibi tuşlar sayılsın mı ayarı ekle
     * Sadece kod yazarken ki component içinde mi saysın ayarı ekle
     * Tuş kombinasyonları ile yapılan eventleri fazla stroke yazma
     * IDE açıldığından beri olan keystroke verisi için cache yap
     * Statistics 'i impl. et
     */
    override fun eventDispatched(event: AWTEvent?){
        if(
            event is KeyEvent &&
            event.id == Event.KEY_PRESS
        ){
            val option = true // TODO::Add customizable
            if(option || event.component is EditorComponentImpl){
                println("Worked P1")
                //statistics.inc()
            }

            println("Event: $event")
        }
    }

    override fun dispose() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this)
    }

}