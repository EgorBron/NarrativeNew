package net.blusutils.narrative.foundation.label

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.blusutils.narrative.foundation.actor.Actor
import net.blusutils.narrative.foundation.DynamicMetaContainer
import net.blusutils.narrative.foundation.Taggable
import net.blusutils.narrative.foundation.label.jumps.ChangeLabelJump
import net.blusutils.narrative.foundation.label.jumps.Jump
import net.blusutils.narrative.foundation.label.signals.Signal
import net.blusutils.narrative.foundation.stringentity.StringEntity

/**
 * The label element class.
 *
 * Label elements, or, more generally, the story elements
 * are the pieces of the story that are used to build the story flow contents.
 * For instance, it can be a phrase or a jump to another label.
 * Sealed for the purpose of serialization.
 *
 * @since Narrative "ver": 1
 */
@Serializable
sealed class LabelElement : Taggable, DynamicMetaContainer

/**
 * The class for the "perform a jump" element.
 * @param jumps The list of jumps to perform
 * @param tags The tags of the element; they will be passed to the story processor when it reaches the element
 * @param dynamicMeta The dynamic metadata of the element
 *
 * @since Narrative "ver": 1
 */
@Serializable
@SerialName("jump")
data class LabelJump(
    val jumps: List<Jump>,
    override val tags: List<String> = listOf(),
    override val dynamicMeta: @Contextual Any? = null,
) : LabelElement(), DynamicMetaContainer {

    /**
     * The class for the "change label" jump.
     * @param labelId The id of the label to jump to
     * @param tags The tags of the element; they will be passed to the story processor when it reaches the element
     * @see net.blusutils.narrative.foundation.label.LabelJump
     * @see net.blusutils.narrative.foundation.label.jumps.ChangeLabelJump
     */
    @Suppress("unused")
    constructor(labelId: String, tags: List<String> = listOf())
            : this(listOf(ChangeLabelJump(labelId)), tags)
}

/**
 * The class for the "text phrase" element.
 * @param actor The actor who is speaking this phrase
 * @param phrase The phrase to display
 * @param tags The tags of the element; they will be passed to the story processor when it reaches the element
 * @param dynamicMeta The dynamic metadata of the element
 *
 * @since Narrative "ver": 1
 */
@Serializable
@SerialName("text")
data class LabelText(
    val actor: Actor? = null,
    val phrase: MutableList<StringEntity>,
    override val tags: List<String> = listOf(),
    @SerialName("dynamic_meta")
    @Contextual
    override val dynamicMeta: Any? = null
) : LabelElement(), DynamicMetaContainer

/**
 * The class for the "call a signal" element.
 * @param signals The signals to call
 * @param tags The tags of the element; they will be passed to the story processor when it reaches the element
 * @param dynamicMeta The dynamic metadata of the element
 *
 * @since Narrative "ver": 1
 */
@Serializable
@SerialName("signal")
data class LabelSignal(
    val signals: List<Signal>?,
    override val tags: List<String> = listOf(),
    @SerialName("dynamic_meta")
    @Contextual
    override val dynamicMeta: Any? = null
) : LabelElement(), DynamicMetaContainer
