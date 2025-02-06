package net.blusutils.narrative.samples.browser.oversnow.handling

import kotlinx.browser.document
import kotlinx.coroutines.delay
import net.blusutils.narrative.foundation.label.LabelText
import net.blusutils.narrative.samples.browser.oversnow.createParagraph
import net.blusutils.narrative.samples.browser.oversnow.renderStringEntity
import net.blusutils.narrative.samples.browser.oversnow.storyDB

fun handleTextElement(delay: Long) =
    LabelElementHandler<LabelText> { elem, container ->
        val subDiv = document.createParagraph {
            classList.add("fade-in-text")
        }
        if (elem.actor != null) {
//        var actorSubSpan = ???
//        if (elem.actor?.ref != null) {
//            actorSubSpan = renderActorFromResource(elem.actor!!.ref)
//        }
            val actorSubSpan = renderStringEntity(elem.actor!!.name, storyDB)
            actorSubSpan.style.paddingLeft = "10px"
            subDiv.append(actorSubSpan)
        }
        val textSpan = renderStringEntity(elem.phrase, storyDB)
        subDiv.append(textSpan)
        container.append(subDiv)
        delay(delay)
    }