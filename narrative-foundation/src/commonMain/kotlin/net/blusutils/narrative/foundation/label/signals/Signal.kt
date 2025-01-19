@file:Suppress("unused")
package net.blusutils.narrative.foundation.label.signals

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * The base class for all signals.
 *
 * Signals are the special label elements that can be used to
 * send an event to the story processor from the regular
 * story flow. It is different from a regular element with
 * tags and dynamic metadata as it is standalone elements
 * that guarantees event delivery with an extendable and type-safe model.
 * Signals <b>**must never**</b> mutate the state of the story environment.
 *
 * @since Narrative "ver": 1
 */
@Serializable
abstract class Signal {
    /**
     * The payload of the signal.
     */
    @Contextual
    abstract val payload: Any?
}