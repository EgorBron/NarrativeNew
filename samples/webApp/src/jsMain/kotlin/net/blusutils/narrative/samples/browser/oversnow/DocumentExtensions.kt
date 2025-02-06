package net.blusutils.narrative.samples.browser.oversnow

import kotlinx.browser.window
import org.w3c.dom.*
import kotlin.js.Promise


inline fun<reified T> HTMLElement.querySelectorAs(
    query: String
): T? {
    return this.querySelector(query) as? T
}

inline fun<reified T> HTMLElement.querySelectorAllAs(
    query: String
): List<T?> {
    val q = this.querySelectorAll(query)
    return List(q.length) { q[it] as T }
}

inline fun<reified T: HTMLElement> Document.createElementAs(
    type: String,
    crossinline init: T.() -> Unit = {}
): T {
    val element = createElement(type) as T
    element.init()
    return element
}

inline fun Document.createInput(
    crossinline init: HTMLInputElement.() -> Unit = {}
) = createElementAs<HTMLInputElement>("input", init)

inline fun Document.createButton(
    crossinline init: HTMLButtonElement.() -> Unit = {}
) = createElementAs<HTMLButtonElement>("button", init)

inline fun Document.createSpan(
    crossinline init: HTMLSpanElement.() -> Unit = {}
) = createElementAs<HTMLSpanElement>("span", init)

inline fun Document.createParagraph(
    crossinline init: HTMLParagraphElement.() -> Unit = {}
) = createElementAs<HTMLParagraphElement>("p", init)

inline fun Document.createLabel(
    crossinline init: HTMLLabelElement.() -> Unit = {}
) = createElementAs<HTMLLabelElement>("label", init)

inline fun Document.createDiv(
    crossinline init: HTMLDivElement.() -> Unit = {}
) = createElementAs<HTMLDivElement>("div", init)

inline fun Document.createAnchor(
    crossinline init: HTMLAnchorElement.() -> Unit = {}
) = createElementAs<HTMLAnchorElement>("a", init)

inline fun runTimeoutJS(
    delay: Int,
    crossinline func: () -> Unit
) {
    window.setTimeout({ func() }, delay)
}

inline fun runTimeoutPromise(
    delay: Int,
    crossinline func: () -> Unit
) = Promise { resolve, _ ->
        window.setTimeout({
            func()
            resolve(Unit)
        }, delay)
    }