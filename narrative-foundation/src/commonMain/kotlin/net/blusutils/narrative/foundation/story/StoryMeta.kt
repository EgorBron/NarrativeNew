package net.blusutils.narrative.foundation.story

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.blusutils.narrative.foundation.DynamicMetaContainer
import net.blusutils.narrative.foundation.stringentity.StringEntity

/**
 * The metadata for a story.
 * @property name The unique name of the story
 * @property environment The runtime environment the story can be run in
 * @property title The display title of the story
 * @property authors List of who authored the story
 * @property description The description of the story
 * @property dynamicMeta A JSON object containing dynamically typed metadata about the story
 *
 * @since Narrative "ver": 1
 */
@Serializable
data class StoryMeta(
    var name: String = "unnamed_story",
    var environment: StoryEnvironments = StoryEnvironments.AnyEnv,
    var title: List<StringEntity> = listOf(),
    var authors: List<String> = listOf(),
    var description: List<StringEntity> = listOf(),

    @SerialName("dynamic_meta")
    @Contextual
    override var dynamicMeta: Any? = null
) : DynamicMetaContainer, StoryScope