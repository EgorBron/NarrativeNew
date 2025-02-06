package net.blusutils.narrative.samples.browser.oversnow.handling

import net.blusutils.narrative.foundation.label.LabelSignal
import net.blusutils.narrative.samples.browser.oversnow.*

fun handleSignalsElement() =
    LabelElementHandler<LabelSignal> { elem, container ->
        if (elem.signals.isNullOrEmpty()) {
            FAIL(container, "Expected signals in element, but got empty list")
            return@LabelElementHandler
        }

        for (signal in elem.signals!!) {
            when (signal) {
                is SnowSignal -> displaySnow(signal.seconds, container)
                is InputSignal -> requestInput(signal, container)
                is SicSignal -> container.append(renderStringEntity(signal.sicText, storyDB))
                else -> {
                    FAIL(container, "Received unknown signal: $signal")
                }
            }
        }
    }