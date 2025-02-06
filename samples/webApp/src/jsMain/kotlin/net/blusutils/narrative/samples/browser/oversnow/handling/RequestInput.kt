package net.blusutils.narrative.samples.browser.oversnow.handling

import kotlinx.browser.document
import kotlinx.coroutines.suspendCancellableCoroutine
import net.blusutils.narrative.foundation.stringentity.toStringEntitySingleList
import net.blusutils.narrative.samples.browser.oversnow.*
import org.w3c.dom.HTMLDivElement
import kotlin.coroutines.resume

suspend fun requestInput(
    inputSignal: InputSignal,
    container: HTMLDivElement
) = suspendCancellableCoroutine { continuation ->
    // first create input field
    val input = document.createInput {
        id = "currentInput"
        type = "text"
        placeholder = "Enter your answer here"
        style.width = "100%"
    }
    // todo styles
    val label = document.createLabel {
        htmlFor = "currentInput"
        append(renderStringEntity(inputSignal.prompt, storyDB))
    }

    container.append(label)
    container.append(input)


    var buff = ""
    input.oninput = {
        buff = input.value
        0
    }
    input.onkeydown = { // block until user passed the input
        if (it.key == "Enter") {
            input.remove()
            storyDB[inputSignal.factId] = buff
            label.append(renderStringEntity(buff.toStringEntitySingleList(), storyDB))
            continuation.resume(Unit)
        }
    }

    continuation.invokeOnCancellation {
        input.remove()
    }
}