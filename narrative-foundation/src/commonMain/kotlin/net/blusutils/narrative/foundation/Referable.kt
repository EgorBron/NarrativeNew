package net.blusutils.narrative.foundation

/**
 * The interface indicating that the implementing class uses a reference to some resource.
 * @property ref The path reference to the resource
 *
 * @since Narrative "ver": 1
 */
interface Referable {
    val ref: String?
}
