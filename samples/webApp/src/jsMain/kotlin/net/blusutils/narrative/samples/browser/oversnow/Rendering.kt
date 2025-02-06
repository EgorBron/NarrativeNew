package net.blusutils.narrative.samples.browser.oversnow

import kotlinx.browser.document
import net.blusutils.narrative.foundation.stringentity.StringEntity
import org.w3c.dom.HTMLDivElement

fun Long.toHexColorString() =
    "#${(this and 0xFFFFFF).toString(16).padStart(6, '0').uppercase()}"

fun Int.toHexColorString() = toLong().toHexColorString()

fun renderStringEntity(
    entities: List<StringEntity>,
    refDatabase: Map<String, Any?>
): HTMLDivElement {
    val elem = document.createDiv()
    entities.forEach { entity ->
        val subSpan = document.createSpan {
            innerText = entity.text ?: ""
            if (entity.ref != null) {
                innerText = refDatabase[entity.ref]?.toString() ?: ""
            }
            for (spec in entity.entitySpecs.distinctBy { it.name }) {
                when (spec.name.lowercase()) {
                    "bold" -> {
                        style.fontWeight = "bold"
                    }
                    "italic" -> {
                        style.fontStyle = "italic"
                    }
                    "color" -> {
                        style.color = spec.payload
                        println(spec.payload)
                    }
                    "font_size" -> {
                        style.fontSize = spec.payload
                    }
                    "underline" -> {
                        style.textDecoration = "underline"
                    }
                    "monospace" -> {
                        style.fontFamily = "monospace"
                    }
                    "link" -> {
                        val link = document.createAnchor { 
                            href = spec.payload
                            innerText = this@createSpan.innerText
                        }
                        replaceWith(link)
                    }
                    else -> {
                        console.error("Got unknown spec: $spec")
                    }
                }
            }
        }
        elem.append(subSpan)
    }
    return elem
}

// epic fail!
@Suppress("FunctionName")
fun FAIL(container: HTMLDivElement, msg: String) {
    container.append(errorElement(msg))

    if (Settings.failsStopsExecution) {
        Settings.Session.isRunning = false
    }
}

fun errorElement(message: String) =
    createNarrationMessage(message).apply {
        style.apply {
            color = "red"
            fontWeight = "bold"
            textDecoration = "underline"
            fontStyle = "italic"
        }
    }

fun createNarrationMessage(message: String): HTMLDivElement {
    return document.createDiv { 
        classList.add("narration-message", "fade-in-text")
        textContent = message
        style.padding = "2rem"
    }
}