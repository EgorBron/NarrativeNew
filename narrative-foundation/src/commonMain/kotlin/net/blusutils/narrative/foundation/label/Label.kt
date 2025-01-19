package net.blusutils.narrative.foundation.label

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.blusutils.narrative.foundation.DynamicMetaContainer
import net.blusutils.narrative.foundation.Taggable

/**
 * The label class.
 *
 * Labels are the building blocks of the story.
 * You can group your story elements into labels and
 * then use them in the story flow in any way.
 * @param id The id of the label
 * @param elements The elements of the label
 * @param tags The tags of the label
 * @param dynamicMeta The dynamic metadata of the label
 *
 * @since Narrative "ver": 1
 */
@Serializable
data class Label(
    val id: String,
    val elements: MutableList<LabelElement> = mutableListOf(),
    override val tags: List<String> = listOf(),
    @SerialName("dynamic_meta")
    @Contextual
    override val dynamicMeta: Any? = null
) : Taggable, DynamicMetaContainer