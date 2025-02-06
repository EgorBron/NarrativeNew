package net.blusutils.narrative.samples.browser.oversnow.handling

import net.blusutils.narrative.foundation.label.LabelElement
import org.w3c.dom.HTMLDivElement

fun interface LabelElementHandler<T: LabelElement> {
    suspend fun handle(element: T, container: HTMLDivElement)
}