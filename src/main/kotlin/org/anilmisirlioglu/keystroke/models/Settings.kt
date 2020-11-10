package org.anilmisirlioglu.keystroke.models

import org.anilmisirlioglu.keystroke.utils.BitMask

data class Settings(
    var countOnlyWorkspace: Boolean = true,
    var keyboardFlags: Int = KeyboardFlags.TYPING_KEYS,
){

    object KeyboardFlags{
        const val TYPING_KEYS = 1 shl 0 // Letters & numbers
        const val FUNCTIONAL_KEYS = 1 shl 1 // F1-F12
        const val CURSOR_CONTROL_KEYS = 1 shl 2 // ->, <- etc.
        const val OTHER_KEYS = 1 shl 3 // ESC, END, HOME etc.

        val ALL_KEYS = BitMask.toInt(
            listOf(
                TYPING_KEYS,
                FUNCTIONAL_KEYS,
                CURSOR_CONTROL_KEYS,
                OTHER_KEYS
            )
        )
    }

}
