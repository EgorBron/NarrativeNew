package net.blusutils.narrative.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.blusutils.narrative.label.Label
import net.blusutils.narrative.label.LabelBuilder
import net.blusutils.narrative.resource.ResourcesBuilder

/**
 * A builder function for a story.
 * @param block A block of code to be executed to build the story
 * @return A new instance of a story builder
 */
fun buildStory(block: Story.() -> Unit) =
    Story().apply(block)

@DslMarker
annotation class StoryBuilderDsl

/**
 * A DSL marker for the story builder
 */
@StoryBuilderDsl
internal interface StoryScope

/**
 * Main class representing a story structure with metadata, resources and labels.
 *
 * @property version Version number of the Narrative format, defaults to 1
 * @property meta Story metadata
 * @property resources Map of resource identifiers to their paths in the resources system
 * @property labels List of story labels/nodes that make up the narrative flow
 *
 * @since Narrative "ver": 1
 */
@Serializable
class Story(
    @SerialName("ver")
    var version: Int = 1,
    var meta: StoryMeta = StoryMeta(),
    var resources: MutableMap<String, String> = mutableMapOf(),
    val labels: MutableList<Label> = mutableListOf()
) : StoryScope {
    /** Holds the deserialized resource instance */
    @Transient
    private var resourceSerialized: Any? = null

    /** Builder for managing story resources */
    @Transient
    private val resourcesBuilder = ResourcesBuilder<Any>(resources)

    /** Builder for managing story labels */
    @Transient
    private val labelBuilder = LabelBuilder(labels)

    /**
     * Configures story metadata using the provided block
     * @param block Configuration block for StoryMeta
     * @see StoryMeta
     */
    fun meta(block: StoryMeta.() -> Unit) { meta.block() }

    /**
     * Configures story resources using the provided instance and block
     * @param instance Resource instance to configure
     * @param block Configuration block for resources
     * @see ResourcesBuilder
     */
    fun<T> resources(instance: T, block: ResourcesBuilder<Any>.(T) -> T) {
        resourceSerialized = resourcesBuilder.block(instance)
    }

    /**
     * Configures story labels using the provided block
     * @param block Configuration block for labels
     * @see LabelBuilder
     */
    fun labels(block: LabelBuilder.() -> Unit) {
        labelBuilder.block()
    }
}