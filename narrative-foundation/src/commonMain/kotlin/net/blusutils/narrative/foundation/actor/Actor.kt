package net.blusutils.narrative.foundation.actor

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.blusutils.narrative.foundation.DynamicMetaContainer
import net.blusutils.narrative.foundation.Referable
import net.blusutils.narrative.foundation.Taggable
import net.blusutils.narrative.foundation.stringentity.StringEntity

/**
 * The interface for the actors.
 *
 * The actor is the logical participant of the story.
 * Actors are supposed to "say" phrases.
 *
 * @param name The name of the actor.
 *
 * @since Narrative "ver": 1
 */
@Serializable
open class Actor(
    open val name: List<StringEntity>,
    override val ref: String?,
    override val tags: List<String> = emptyList(),
    @SerialName("dynamic_meta")
    @Contextual
    override val dynamicMeta: Any? = null,
) : Referable, Taggable, DynamicMetaContainer