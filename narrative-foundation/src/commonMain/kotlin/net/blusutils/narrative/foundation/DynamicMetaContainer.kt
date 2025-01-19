package net.blusutils.narrative.foundation

import kotlinx.serialization.SerialName

/**
 * The interface indicating that the implementing class has a dynamically typed metadata field.
 * @property dynamicMeta The metadata field
 * @since Narrative "ver": 1
 */
interface DynamicMetaContainer {
    @SerialName("dynamic_meta")
    val dynamicMeta: Any?
}