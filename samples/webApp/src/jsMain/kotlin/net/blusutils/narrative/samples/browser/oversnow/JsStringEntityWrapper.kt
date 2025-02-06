package net.blusutils.narrative.samples.browser.oversnow

import net.blusutils.narrative.foundation.actor.Actor
import net.blusutils.narrative.foundation.label.LabelBuilderScope
import net.blusutils.narrative.foundation.stringentity.EntityStringBuilder
import net.blusutils.narrative.foundation.stringentity.StringEntity
import net.blusutils.narrative.foundation.stringentity.StringEntityWrapper

class JsStringEntityWrapper : StringEntityWrapper {

    private var entity = StringEntity("")

    override fun beginPut(str: String) = apply { entity = StringEntity(str) }

    override fun toStringEntity() = entity

    /**
     * Makes the string entity bold.
     */
    fun bold(): JsStringEntityWrapper {
        entity.entitySpecs.add(StringEntity.EntitySpec(StringEntity.EntitySpec.BOLD, ""))
        return this
    }

    /**
     * Makes the string entity italic.
     */
    fun italic(): JsStringEntityWrapper {
        entity.entitySpecs.add(StringEntity.EntitySpec(StringEntity.EntitySpec.ITALIC, ""))
        return this
    }

    /**
     * Color the string entity with the given color.
     */
    fun color(color: Long): JsStringEntityWrapper {
        entity.entitySpecs.add(StringEntity.EntitySpec(StringEntity.EntitySpec.COLORED, color.toHexColorString()))
        return this
    }

    /**
     * Adds a reference to the resource location of the string entity.
     */
    fun ref(reference: String): JsStringEntityWrapper {
        entity = entity.copy(ref = reference)
        return this
    }

    fun link(target: String): JsStringEntityWrapper {
        entity.entitySpecs.add(StringEntity.EntitySpec(StringEntity.EntitySpec.LINK, target))
        return this
    }

    fun underline(): JsStringEntityWrapper {
        entity.entitySpecs.add(StringEntity.EntitySpec(StringEntity.EntitySpec.UNDERLINE, ""))
        return this
    }

    fun mono(): JsStringEntityWrapper {
        entity.entitySpecs.add(StringEntity.EntitySpec(StringEntity.EntitySpec.MONOSPACE, ""))
        return this
    }

    fun sized(size: String): JsStringEntityWrapper {
        entity.entitySpecs.add(StringEntity.EntitySpec("font_size", size))
        return this
    }

    companion object {

        fun buildEntityString(
            block: EntityStringBuilder<JsStringEntityWrapper>.()->Unit
        ): List<StringEntity> {
            return net.blusutils.narrative.foundation.stringentity.buildEntityString(
                createT = { JsStringEntityWrapper() }, block = block
            )
        }

        fun EntityStringBuilder<JsStringEntityWrapper>.ref(reference: String) =
            put("#").ref(reference)

        fun LabelBuilderScope.text(
            actor: Actor? = null,
            block: EntityStringBuilder<JsStringEntityWrapper>.() -> Unit
        ) {
            text({ JsStringEntityWrapper() }, actor, block)
        }

        fun LabelBuilderScope.text(
            block: EntityStringBuilder<JsStringEntityWrapper>.() -> Unit
        ) {
            text({ JsStringEntityWrapper() }, null, block)
        }
    }
}