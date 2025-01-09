package net.blusutils.narrative.stringentity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.blusutils.narrative.Referable

/**
 * A class representing a formatted part of display string.
 * @property text The text content of the entity
 * @property entitySpecs The list of entity formatting specifications
 * @property ref The path reference to the resource used as text content
 *
 * @since Narrative "ver": 1
 */
@Serializable
data class StringEntity(
    var text: String?,
    @SerialName("entity_specs")
    val entitySpecs: MutableList<EntitySpec> = mutableListOf(),
    override val ref: String? = null,
) : Referable {
    /**
     * A class representing a formatting specification for a string entity.
     * @property name The name of the formatting specification
     * @property payload The payload of the formatting specification
     * @sample EntitySpecSample.linkSpec
     * @sample EntitySpecSample.colorSpec
     *
     * @since Narrative "ver": 1
     */
    @Serializable
    data class EntitySpec(
        val name: String,
        val payload: String,
    ) {
        companion object {
            /** The **bold** text spec */
            const val BOLD = "bold"
            /** The _italic_ text spec */
            const val ITALIC = "italic"
            /** The ~strikethrough~ text spec */
            const val STRIKE = "strike"
            /** The \_\_underline__ text spec */
            const val UNDERLINE = "underline"
            /** The `monospace` text spec */
            const val MONOSPACE = "monospace"
            /** The superscript (¹²³) text spec */
            const val SUPERSCRIPT = "superscript"
            /** The subscript (₁₂₃) text spec */
            const val SUBSCRIPT = "subscript"
            /** The colored text spec */
            const val COLORED = "color"
            /** The hidden text spec (spoiler, obfuscated, etc.) */
            const val HIDDEN = "hidden"
            /** The [link](https://example.com/) text spec */
            const val LINK = "link"
        }
    }

    private object EntitySpecSample {
        val linkSpec = EntitySpec(EntitySpec.LINK, "https://www.example.com")
        val colorSpec = EntitySpec(EntitySpec.COLORED, "16711680")
    }
}