package net.blusutils.narrative.samples.browser.oversnow

import net.blusutils.narrative.foundation.label.LabelBuilderScope
import net.blusutils.narrative.foundation.label.signals.Signal
import net.blusutils.narrative.foundation.stringentity.StringEntity
import net.blusutils.narrative.foundation.stringentity.toStringEntitySingleList

data class SnowSignal(
    val seconds: Double,
    override val payload: Any? = null
) : Signal()

data class InputSignal(
    val factId: String,
    val prompt: List<StringEntity>,
    override val payload: Any? = null
) : Signal()

data class DelaySignal(
    val ms: Long,
    override val payload: Any? = null
) : Signal()

data class SicSignal(
    val sicText: List<StringEntity> = JsStringEntityWrapper.buildEntityString {
        +put(
            """
            Notice: This feature is missing — yes,
            you read that right, the creator
            was simply too lazy to implement it.
            So we have a SIC here — Stub In Clause
            (not a Latin adverb "sic").
            """
                .trimIndent()
                .replace("\n", " ")
        )
            .color(0x00FFFF00)
            .italic()
            .sized("8pt")
    },
    override val payload: Any? = null
) : Signal()
