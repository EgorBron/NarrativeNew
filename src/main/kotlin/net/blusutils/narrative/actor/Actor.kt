package net.blusutils.narrative.actor

import net.blusutils.narrative.DynamicMetaContainer
import net.blusutils.narrative.Referable
import net.blusutils.narrative.Taggable
import net.blusutils.narrative.stringentity.StringEntity

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