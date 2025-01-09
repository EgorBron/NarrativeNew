package net.blusutils.narrative.label.signals.defaultsignals

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import net.blusutils.narrative.NonStandardNarrativeApi
import net.blusutils.narrative.label.LabelBuilderScope
import net.blusutils.narrative.label.signals.Signal

/**
 * Signal model to stop current label processing.
 *
 * This signal is not part of the base Narrative API.
 */
@NonStandardNarrativeApi
@Serializable
@SerialName("stop")
data object StopSignal : Signal() {
    /**
     * Additional payload for the signal.
     */
    @Contextual
    override val payload: Any? = null

    /**
     * An extension to [LabelBuilderScope] to put a [StopSignal] into the label.
     * @param tags Additional tags for the signal.
     * @param dynamicMeta Dynamic metadata for the signal.
     */
    @NonStandardNarrativeApi
    fun LabelBuilderScope.stopSignal(
        tags: MutableList<String> = mutableListOf(),
        dynamicMeta: Any? = null
    ) {
        signal(StopSignal, tags = tags, dynamicMeta = dynamicMeta)
    }
}