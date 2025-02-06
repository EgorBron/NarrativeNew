package net.blusutils.narrative.samples.browser.oversnow.handling

import kotlinx.browser.document
import kotlinx.browser.window
import net.blusutils.narrative.foundation.label.*
import net.blusutils.narrative.foundation.story.Story
import net.blusutils.narrative.samples.browser.oversnow.FAIL
import net.blusutils.narrative.samples.browser.oversnow.Settings
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.SMOOTH
import org.w3c.dom.ScrollBehavior
import org.w3c.dom.ScrollToOptions

suspend fun changeLabel(
    story: Story,
    labelID: String,
    container: HTMLDivElement
) {
    val label = story.labels.find { it.id == labelID }
    if (label == null) {
        FAIL(container, "Label `$labelID` is not found. Please check the story.")
        return
    }
    changeLabel(story, label, container)
}

@Suppress("unchecked_cast")
suspend fun changeLabel(
    story: Story,
    label: Label,
    container: HTMLDivElement,
) {
    for (elem in label.elements) {

        if (!Settings.Session.isRunning)
            break

        val func = when (elem) {
            is LabelText -> {
                val delay = 2000L
                handleTextElement(delay)
            }
            is LabelSignal -> {
                handleSignalsElement()
            }
            is LabelJump -> {
                handleJumpsElement(story)
            }
        } as LabelElementHandler<LabelElement>

        func.handle(elem, container)

        window.scrollTo(
            ScrollToOptions(
                left = .0,
                top = document.body!!.scrollHeight.toDouble(),
                behavior = ScrollBehavior.SMOOTH
            )
        )
    }
}