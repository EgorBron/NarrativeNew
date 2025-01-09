package net.blusutils.narrative

/**
 * The interface indicating that the implementing class can be tagged and thus can occur in events.
 * @property tags The list of tags that object has
 *
 * @since Narrative "ver": 1
 */
interface Taggable {
    val tags: List<String>
}