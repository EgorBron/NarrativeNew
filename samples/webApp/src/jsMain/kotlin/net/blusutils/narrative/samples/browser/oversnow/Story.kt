package net.blusutils.narrative.samples.browser.oversnow

import net.blusutils.narrative.foundation.label.LabelBuilderScope
import net.blusutils.narrative.foundation.story.StoryEnvironments
import net.blusutils.narrative.foundation.story.buildStory
import net.blusutils.narrative.samples.browser.oversnow.JsStringEntityWrapper.Companion.buildEntityString

val sic = SicSignal()
@JsName("sic_excl")
fun LabelBuilderScope.`sic!`() {
    signal(sic)
}

val story = buildStory {
    meta {
        name = "oversnow"
        title = buildEntityString {
            +put("Oversnow").bold()
        }
        description = buildEntityString {
            +"Sample story running on Kotlin/JS."
        }
        authors = listOf("Egor Bron")
        environment = StoryEnvironments.KotlinJsWebIR
    }
    labels {
        main {

            text("...")

            `sic!`() // signal(SnowSignal(3.5))

            signal(
                InputSignal(
                    "#me_name",
                    buildEntityString {
                        +put("Wait, what is your name?").italic()
                    }
                )
            )

            `sic!`() // signal(SnowSignal(3.5))
            text(me, "Ahh, great.")
        }
    }
}