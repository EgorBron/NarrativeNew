package net.blusutils.narrative.foundation.actor

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
 * @since Narrative "ver": 1
 */
interface Actor : Referable, Taggable, DynamicMetaContainer {
    /**
     * The name of the actor.
     */
    val name: List<StringEntity>
}