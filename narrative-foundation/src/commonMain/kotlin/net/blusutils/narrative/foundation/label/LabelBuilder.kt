@file:Suppress("unused")
package net.blusutils.narrative.foundation.label

import net.blusutils.narrative.foundation.story.StoryScope

/**
 * A helper class to build labels.
 * @param labelsList The list of labels built by this builder
 */
class LabelBuilder(val labelsList: MutableList<Label>) : StoryScope {

    /**
     * Main label of the story.
     *
     * This is an alias for the [label] function with the id "main".
     *
     * @param block The block of code to execute in the scope of the main label
     */
    fun main(block: LabelBuilderScope.() -> Unit) {
        label("main", block = block)
    }

    /**
     * Creates a new [Label] with the given id and tags.
     * @param id The id of the label
     * @param tags The tags of the label
     * @param block The block of code to execute in the scope of the label
     */
    fun label(id: String, vararg tags: String, block: LabelBuilderScope.() -> Unit) {
        val label = Label(id, tags = tags.toList())
        LabelBuilderScope(label).block()
        labelsList.add(label)
    }

    // TODO: maybe create jumpLabel that performs an action and then jumps to another label?
}