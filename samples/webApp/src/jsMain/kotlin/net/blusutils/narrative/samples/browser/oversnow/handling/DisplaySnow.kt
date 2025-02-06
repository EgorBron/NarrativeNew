package net.blusutils.narrative.samples.browser.oversnow.handling

import kotlinx.browser.document
import kotlinx.coroutines.delay
import net.blusutils.narrative.samples.browser.oversnow.createDiv
import net.blusutils.narrative.samples.browser.oversnow.runTimeoutPromise
import org.w3c.dom.HTMLDivElement

suspend fun displaySnow(seconds: Double, container: HTMLDivElement) {
    // create div for snowflakes
    val snowContainer = document.createDiv()

    repeat(200) {
        // width and height of snowflake
        val wh = "${(1..5).random()}px"

        val snowflake = document.createDiv {
            classList.add("snowflake")
            style.apply {
                left = "${(0..99).random()}vw"
                transition = "all ${seconds / 4}s ease"
                position = "absolute"
                top = "${(1..85).random()}vh"
                width = wh
                height = wh
                background = "white"
                borderRadius = "50%"
            }
        }

        snowContainer.append(snowflake)
        runTimeoutPromise(100) {
            // point of this line:
            snowflake.style.top = (
                    // get the previous value of height
                    snowflake.style.top.takeWhile { it != 'v' }.toInt()
                            // and then increase it by 10
                            + 10
                    ) // and then convert it back to string
                .toString() + "vh"
        }
    }

    container.append(snowContainer)
    delay((seconds * 1.5 * 1000).toLong())
    snowContainer.remove()
}