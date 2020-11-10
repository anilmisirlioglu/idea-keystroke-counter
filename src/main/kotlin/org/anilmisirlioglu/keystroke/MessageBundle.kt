package org.anilmisirlioglu.keystroke

import com.intellij.AbstractBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

@NonNls
private const val BUNDLE = "messages.MessageBundle"

object MessageBundle : AbstractBundle(BUNDLE){

    @JvmStatic
    fun message(
        @PropertyKey(resourceBundle = BUNDLE) key: String,
        vararg params: Any
    ): String = getMessage(key, *params)

    @JvmStatic
    fun messagePointer(
        @PropertyKey(resourceBundle = BUNDLE) key: String,
        vararg params: Any
    ) = run{
        message(key, *params)
    }

}