package net.blusutils.narrative.samples.browser.oversnow

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import net.blusutils.narrative.foundation.story.Story
import net.blusutils.narrative.samples.browser.oversnow.handling.*
import org.w3c.dom.*


val storyDB = mutableMapOf<String, Any>()

object Settings {
    var failsStopsExecution = false

    object Session {
        var isRunning = true
    }
}

fun main() {
    try {
        val mainBlock = document.body?.querySelectorAs<HTMLDivElement>("#main")
        renderNarrativeStory(story, mainBlock ?: throw Exception())
    } catch (e: Exception) {
        window.alert("Error: $e")
        throw e
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun renderNarrativeStory(
    story: Story,
    container: HTMLDivElement,
) {
    GlobalScope.launch {
        changeLabel(story, "main", container)
    }
}

