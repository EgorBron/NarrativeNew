package net.blusutils.narrative.foundation.label.jumps

import kotlinx.serialization.Serializable
import net.blusutils.narrative.foundation.DynamicMetaContainer
import net.blusutils.narrative.foundation.Taggable

/**
 * The base class for all jumps.
 *
 * Jumps are the special label elements that can be used to
 * mutate the state or flow of the story environment.
 * Jumps are more powerful than signals due to their ability to do that.
 *
 * @since Narrative "ver": 1
 */
@Serializable
abstract class Jump : Taggable, DynamicMetaContainer