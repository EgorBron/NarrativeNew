package net.blusutils.narrative.label

import kotlinx.serialization.Contextual
import kotlinx.serialization.json.JsonObject
import net.blusutils.narrative.actor.Actor
import net.blusutils.narrative.label.jumps.Jump
import net.blusutils.narrative.label.signals.Signal
import net.blusutils.narrative.stringentity.*

/**
 * The scope for building labels' contents.
 * @param label the label to build
 */
class LabelBuilderScope(val label: Label) {

//    /**
//     * Adds a jump to another label.
//     * @param labelId the id of the label to jump to
//     * @param tags the tags passed to the processor when it reaches this element
//     */
//    fun jump(labelId: String, tags: List<String> = listOf()) {
//        label.elements.add(LabelJump(labelId, tags))
//    }

    /**
     * Adds a jump action to the label.
     * @param jumps the jumps to add
     * @param tags the tags passed to the processor when it reaches this element
     * @param dynamicMeta the dynamic metadata of the label
     */
    fun jump(
        vararg jumps: Jump,
        tags: List<String> = listOf(),
        dynamicMeta: @Contextual Any? = null
    ) {
        label.elements.add(LabelJump(jumps.toList(), tags, dynamicMeta))
    }

    /**
     * Adds a signal to the label.
     * @param signals the signals to add
     * @param tags the tags passed to the processor when it reaches this element
     * @param dynamicMeta the dynamic metadata of the label
     */
    fun signal(
        vararg signals: Signal,
        tags: List<String> = listOf(),
        dynamicMeta: @Contextual Any? = null
    ) {
        label.elements.add(LabelSignal(signals.toList(), tags, dynamicMeta))
    }

    /**
     * Adds a text phrase to the label.
     * @param actor the actor who is speaking this phrase
     * @param phrase the phrase to add
     * @param tags the tags passed to the processor when it reaches this element
     * @param dynamicMeta the dynamic metadata of the label
     */
    fun text(
        actor: Actor? = null,
        phrase: List<StringEntity> = emptyList(),
        tags: List<String> = listOf(),
        dynamicMeta: @Contextual Any? = null
    ) {
       label.elements.add(
           LabelText(
               actor = actor,
               phrase = phrase.toMutableList(),
               tags = tags,
               dynamicMeta = dynamicMeta
           )
       )
    }

    /**
     * A shortcut for adding a single-string text phrase to the label.
     * @param text the text to add
     * @see [LabelBuilderScope.text]
     */
    fun text(text: String) {
        text(null, text.toStringEntitySingleList())
    }

    /**
     * A shortcut for adding a single-string text phrase with an actor to the label.
     * @param actor the actor who is speaking this phrase
     * @param text the text to add
     * @see [LabelBuilderScope.text]
     */
    fun text(actor: Actor, text: String) {
        text(actor, text.toStringEntitySingleList())
    }

    /**
     * Builds an entity-based text phrase and adds it to the label.
     * @param createT a function that creates the [StringEntityWrapper]
     * @param actor the actor who is speaking this phrase
     * @param block the block to build the [StringEntity]s
     */
    fun<T: StringEntityWrapper> text(
        createT: () -> T,
        actor: Actor? = null,
        block: EntityStringBuilder<T>.() -> Unit
    ) {
        text(null, EntityStringBuilder(createT)
            .apply(block)
            .getEntities())
    }
}