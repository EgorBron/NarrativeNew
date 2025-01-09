package net.blusutils.narrative.label.signals.defaultsignals

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import net.blusutils.narrative.NonStandardNarrativeApi
import net.blusutils.narrative.label.LabelBuilderScope
import net.blusutils.narrative.label.signals.Signal

/**
 * A signal to store a piece of data called "fact" in some sort of persistent storage.
 *
 * This signal is not part of the base Narrative API.
 * @param payload The payload of the signal.
 */
@NonStandardNarrativeApi
@Serializable
@SerialName("store_fact")
data class StoreFactSignal(
    override val payload: @Contextual Any?
) : Signal() {
    companion object {
        /**
         * An extension to [LabelBuilderScope] to put a [StoreFactSignal] into the label.
         * @param obj The payload of the signal.
         * @param tags Additional tags for the signal.
         */
        fun LabelBuilderScope.storeFact(
            obj: @Contextual Any?,
            tags: MutableList<String> = mutableListOf()
        ) {
            signal(StoreFactSignal(obj), tags = tags, dynamicMeta = null)
        }
    }
}
