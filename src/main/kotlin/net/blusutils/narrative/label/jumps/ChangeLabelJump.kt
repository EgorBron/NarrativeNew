package net.blusutils.narrative.label.jumps

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.blusutils.narrative.label.LabelBuilderScope

@SerialName("jump_to_label")
@Serializable
data class ChangeLabelJump(
    val targetLabel: String,
    @Contextual
    override val payload: Any? = null,
    override val tags: List<String> = listOf(),
) : Jump() {
    companion object {
        fun LabelBuilderScope.jumpTo(
            target: String,
            tags: List<String> = listOf(),
            payload: @Contextual Any? = null
        ) {
            jump(
                ChangeLabelJump(
                    targetLabel = target,
                    tags = tags,
                    payload = payload
                ),
                tags = tags,
                dynamicMeta = payload
            )
        }
    }
}
