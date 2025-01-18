package net.blusutils.narrative

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonObject

/**
 * The interface indicating that the implementing class has a dynamically typed metadata field.
 * @property dynamicMeta The metadata field
 * @since Narrative "ver": 1
 */
interface DynamicMetaContainer {
    @SerialName("dynamic_meta")
    val dynamicMeta: Any?
}