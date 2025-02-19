@file:Suppress("unused")
package net.blusutils.narrative.foundation.label.jumps

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.blusutils.narrative.foundation.label.LabelBuilderScope

/**
 * A standard jump that changes the current label to other
 *
 * @property targetLabel the label to set as current
 */
@SerialName("jump_to_label")
@Serializable
data class ChangeLabelJump(
    val targetLabel: String,
    override val dynamicMeta: @Contextual Any? = null,
    override val tags: List<String> = listOf(),
) : Jump() {
    companion object {
        fun LabelBuilderScope.jumpTo(
            target: String,
            tags: List<String> = listOf(),
            dynamicMeta: @Contextual Any? = null
        ) {
            jump(
                ChangeLabelJump(
                    targetLabel = target,
                    tags = tags,
                    dynamicMeta = dynamicMeta
                ),
                tags = tags,
                dynamicMeta = dynamicMeta
            )
        }
    }
}
