package net.blusutils.narrative.samples.browser.oversnow

import net.blusutils.narrative.samples.browser.oversnow.JsStringEntityWrapper.Companion.ref
import net.blusutils.narrative.foundation.actor.Actor
import net.blusutils.narrative.foundation.stringentity.StringEntity
import net.blusutils.narrative.samples.browser.oversnow.JsStringEntityWrapper.Companion.buildEntityString

data class JsActor(
    override val name: List<StringEntity>,
    override val ref: String? = "",
    override val tags: List<String> = listOf(),
    override val dynamicMeta: Any? = null
) : Actor(name, ref, tags, dynamicMeta)

val me = JsActor(
    buildEntityString {
        +ref("#me_name")
    },
    "#me_name"
)

val bill = JsActor(
    buildEntityString {
        + put("Bill Carter")
            .bold()
            .mono()
    }
)