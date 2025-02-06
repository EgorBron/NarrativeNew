package net.blusutils.narrative.samples.browser.oversnow.handling

import net.blusutils.narrative.foundation.label.LabelJump
import net.blusutils.narrative.foundation.label.jumps.ChangeLabelJump
import net.blusutils.narrative.foundation.story.Story
import net.blusutils.narrative.samples.browser.oversnow.FAIL

fun handleJumpsElement(story: Story) =
    LabelElementHandler<LabelJump> { elem, container ->
        if (elem.jumps.isEmpty()) {
            FAIL(container, "Expected list of jumps in element, but got empty list")
            return@LabelElementHandler
        }

        for (jump in elem.jumps) {
            when (jump) {
                is ChangeLabelJump -> {
                    changeLabel(story, jump.targetLabel, container)
                }

                else -> {
                    FAIL(container, "Received unknown jump: $jump")
                }
            }
        }
    }