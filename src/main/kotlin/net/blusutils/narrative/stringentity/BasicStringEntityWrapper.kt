package net.blusutils.narrative.stringentity

import net.blusutils.narrative.NonStandardNarrativeApi
import net.blusutils.narrative.actor.Actor
import net.blusutils.narrative.label.LabelBuilderScope

/**
 * The basic implementation of the [StringEntityWrapper].
 *
 * This implementation does not depend on any runtime processors,
 * and thus not part of the official Narrative API.
 */
@NonStandardNarrativeApi
class BasicStringEntityWrapper : StringEntityWrapper {

    /** The string entity that is being built. */
    private var entity = StringEntity("")

    /**
     * Begins the building of a new string entity.
     * @param str The string to begin building the entity from
     * @return The current instance of the wrapper
     */
    override fun beginPut(str: String): BasicStringEntityWrapper {
        entity = StringEntity(str)
        return this
    }

    /**
     * Makes the string entity bold.
     */
    fun bold(): BasicStringEntityWrapper {
        entity.entitySpecs.add(StringEntity.EntitySpec(StringEntity.EntitySpec.BOLD, ""))
        return this
    }

    /**
     * Makes the string entity italic.
     */
    fun italic(): BasicStringEntityWrapper {
        entity.entitySpecs.add(StringEntity.EntitySpec(StringEntity.EntitySpec.ITALIC, ""))
        return this
    }

    /**
     * Color the string entity with the given color.
     */
    fun color(color: Long): BasicStringEntityWrapper {
        entity.entitySpecs.add(StringEntity.EntitySpec(StringEntity.EntitySpec.COLORED, color.toString()))
        return this
    }

    /**
     * Adds a reference to the resource location of the string entity.
     */
    fun ref(reference: String): BasicStringEntityWrapper {
        entity = entity.copy(ref = reference)
        return this
    }

    /**
     * Returns the built string entity.
     * @return The built string entity
     */
    override fun toStringEntity(): StringEntity {
        return entity
    }

    companion object {
        /**
         * Extension to the [EntityStringBuilder] to add a string entity with only the reference to resource.
         */
        @NonStandardNarrativeApi
        fun EntityStringBuilder<BasicStringEntityWrapper>.ref(reference: String) =
            put("#").ref(reference)

        /**
         * Extension to the [LabelBuilderScope] to build a string entity and add it to the label.
         * This uses the [BasicStringEntityWrapper] as default implementation of the [StringEntityWrapper].
         */
        @NonStandardNarrativeApi
        fun LabelBuilderScope.text(
            actor: Actor? = null,
            block: EntityStringBuilder<BasicStringEntityWrapper>.() -> Unit
        ) {
            text({ BasicStringEntityWrapper() }, actor, block)
        }
    }
}