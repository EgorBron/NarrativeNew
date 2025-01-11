package net.blusutils

import com.varabyte.kotter.foundation.input.*
import net.blusutils.narrative.*
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.Color
import com.varabyte.kotter.foundation.text.ColorLayer
import com.varabyte.kotter.foundation.text.bold
import com.varabyte.kotter.foundation.text.color
import com.varabyte.kotter.foundation.text.cyan
import com.varabyte.kotter.foundation.text.red
import com.varabyte.kotter.foundation.text.text
import com.varabyte.kotter.foundation.text.textLine
import com.varabyte.kotter.foundation.text.underline
import com.varabyte.kotter.foundation.text.white
import com.varabyte.kotter.foundation.text.yellow
import com.varabyte.kotter.runtime.MainRenderScope
import com.varabyte.kotter.runtime.RunScope
import com.varabyte.kotter.runtime.Section
import com.varabyte.kotter.runtime.Session
import com.varabyte.kotter.runtime.concurrent.createKey
import com.varabyte.kotter.runtime.render.RenderScope
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.blusutils.Log.err
import net.blusutils.narrative.actor.Actor
import net.blusutils.narrative.label.Label
import net.blusutils.narrative.label.LabelJump
import net.blusutils.narrative.label.LabelSignal
import net.blusutils.narrative.label.LabelText
import net.blusutils.narrative.label.jumps.ChangeLabelJump
import net.blusutils.narrative.label.jumps.ChangeLabelJump.Companion.jumpTo
import net.blusutils.narrative.label.signals.Signal
import net.blusutils.narrative.story.Story
import net.blusutils.narrative.story.buildStory
import net.blusutils.narrative.stringentity.BasicStringEntityWrapper.Companion.ref
import net.blusutils.narrative.stringentity.StringEntity
import net.blusutils.narrative.stringentity.buildEntityString

/**
 * Simple logging utility
 */
object Log {
    /**
     * Log severity levels for [Log]
     */
    enum class LogSeverity {
        Debug,
        Info,
        Warn,
        Error
    }

    /**
     * Writes a log message to the Kotter's [RenderScope]
     * @param msg The message to log
     * @param severity The severity of the log, defines the color of the message
     */
    fun RenderScope.log(msg: String, severity: LogSeverity) {
        val colorScope: (ColorLayer, Boolean, RenderScope.() -> Unit) -> Unit =
            when (severity) {
                LogSeverity.Debug -> ::white
                LogSeverity.Info -> ::cyan
                LogSeverity.Warn -> ::yellow
                LogSeverity.Error -> ::red
            }
        colorScope(ColorLayer.FG, true) {
            textLine(msg)
        }
    }

    fun Session.log(msg: String, severity: LogSeverity) = section { log(msg, severity) }

    /**
     * Logs a debug message to the Kotter's [RenderScope] (white color)
     * @see [Log.log]
     */
    fun RenderScope.dbg(msg: String) = log(msg, LogSeverity.Debug)
    fun Session.dbg(msg: String) = section { dbg(msg) }

    /**
     * Logs an info message to the Kotter's [RenderScope] (cyan color)
     * @see [Log.log]
     */
    fun RenderScope.info(msg: String) = log(msg, LogSeverity.Info)
    fun Session.info(msg: String) = section { info(msg) }

    /**
     * Logs a warning message to the Kotter's [RenderScope] (yellow color)
     * @see [Log.log]
     */
    fun RenderScope.warn(msg: String) = log(msg, LogSeverity.Warn)
    fun Session.warn(msg: String) = section { warn(msg) }

    /**
     * Logs an error message to the Kotter's [RenderScope] (red color)
     * @see [Log.log]
     */
    fun RenderScope.err(msg: String) = log(msg, LogSeverity.Error)
    fun Session.err(msg: String) = section { err(msg) }
}

/**
 * State key for [String]s
 */
private val StringStateKey = Section.Lifecycle.createKey<String>()

/**
 * State key for [List] of [String]s
 */
private val ListStringStateKey = Section.Lifecycle.createKey<List<String>>()

/**
 * Wraps the given block ([RenderScope])
 * with characters if the given [condition] is true.
 * This is useful in the selection menus.
 * @param condition The condition to check
 * @param before The character to print before the block
 * @param after The character to print after the block
 * @param block The block to wrap
 */
private fun RenderScope.wrapIf(condition: Boolean, before: Char, after: Char, block: RenderScope.() -> Unit) {
    text(if (condition) before else ' ')
    scopedState {
        block()
    }
    text(if (condition) after else ' ')
}

/**
 * Renders a choice menu into a [MainRenderScope]
 * @param choice The selected element of the choice list
 * @param choices The list of choices
 */
fun MainRenderScope.choice(choice: String, choices: List<String>) {
    data[StringStateKey] = choice
    data[ListStringStateKey] = choices
    for (ch in choices) {
        wrapIf(choice == ch, '[', ']') {
            text("$ch")
        }
        text(' ')
    }
    textLine()
}

/**
 * State of the choice menu
 * @param choosen The currently selected choice
 * @param list The list of choices
 * @param shouldAccept Whether the callback should accept this state
 */
class ChoiceScope(
    val choosen: String,
    val list: List<String>,
    val shouldAccept: Boolean = false
)

// NOTE: This registers onKeyPressed meaning you can't use this AND onKeyPressed in your own code.
// Pass null into `valueOnCancel` to disable cancelling.
/**
 * Handles the input for choice menu
 * @param valueOnCancel The value to return if the user cancels the choice (by pressing ESC or Q)
 * @param block The block to run when the choice is changed
 */
fun RunScope.onChoiceChanged(valueOnCancel: String? = "", block: ChoiceScope.() -> Unit) {
    // this gets current selection
    fun getKey() = data[StringStateKey] ?: ""
    // and this the full list of choices
    fun getList() = data[ListStringStateKey] ?: emptyList()
    // with this function, we can safely loop through the choices
    fun getOn(index: Int) =
        when {
            index < 0 -> getList().first()
            index >= getList().size -> getList().last()
            else -> getList()[index]
        }
    // and this is the current index
    fun getCurrent() = getList().indexOf(getKey())

    // handling the input
    onKeyPressed {
        val choiceScope = when (key) {
            Keys.LEFT -> ChoiceScope(getOn(getCurrent()-1), getList())
            Keys.RIGHT -> ChoiceScope(getOn(getCurrent()+1), getList())
            Keys.HOME -> ChoiceScope(getList().first(), getList())
            Keys.END -> ChoiceScope(getList().last(), getList())

            Keys.ESC, Keys.Q, Keys.Q_UPPER ->
                if (valueOnCancel != null)
                    ChoiceScope(valueOnCancel, getList(), shouldAccept = true)
                else
                    null

            Keys.ENTER -> ChoiceScope(getKey(), getList(), shouldAccept = true)

            else -> null
        }

        // callback
        choiceScope?.block()
    }
}

/**
 * An actor with a name and a color
 */
@Serializable
data class ColoredActor(
    override val name: List<StringEntity>,
    @Contextual override val dynamicMeta: Any? = null,
    override val ref: String? = null,
    override val tags: List<String> = emptyList(),
) : Actor

// the first actor is "me"
@OptIn(NonStandardNarrativeApi::class)
val actorMe = ColoredActor(buildEntityString {
    + put("Me").color(Color.YELLOW.ordinal.toLong())
})

// and the second one is... "you"?
@OptIn(NonStandardNarrativeApi::class)
val actorYou = ColoredActor(buildEntityString {
    +put("#").color(Color.CYAN.ordinal.toLong()).ref("#name")
})

/**
 * A custom signal containing a list of options to select from
 * @param options The list of options, where the first element
 * is the key and the second is the choice value;
 * the empty list means bare input
 * @param subject The prompt to show before the options
 * @param payload The payload of the signal
 */
@Serializable
data class InputSignal(
    val options: List<Pair<String, String>>,
    val subject: String? = null,
    @Contextual
    override val payload: Any? = null,
) : Signal()

// story time!
@OptIn(NonStandardNarrativeApi::class)
val story = buildStory {
    version = 1
    labels {
        main {
            text("The sample story for Kotter enviroment starts here.")
            text(actorMe, "Hello there! What is your name?")
            signal(
                InputSignal(
                    listOf(),
                    subject = "name"
                )
            )
            text(actorYou, buildEntityString {
                + "Hello! My name is "
                + ref("#name")
                + "!"
            })
            text(actorMe, "Want to see some colors?")
            signal(
                InputSignal(
                    listOf(
                        "yes" to "Yes",
                        "no" to "No",
                        "more" to "Yes, and even more!"
                    ),
                    subject = "colors"
                )
            )
            text(actorYou, buildEntityString {
                +ref("#colors")
            })
            text(actorMe, "Okay then.")
            jumpTo("another")
        }

        label("another") {
            text("This is another label.")
            jumpTo("aaa")
        }
    }
}

/**
 * Renders a list of [StringEntity].
 * Only underlined, colored and bold formatting is supported.
 * References are supported too.
 */
fun MainRenderScope.displayStringEntity(entities: List<StringEntity>) {
    for (entity in entities) {
        for (spec in entity.entitySpecs) {
            when (spec.name) {
                StringEntity.EntitySpec.BOLD -> {
                    bold()
                }

                StringEntity.EntitySpec.UNDERLINE -> {
                    underline()
                }

                StringEntity.EntitySpec.COLORED -> {
                    color(Color.entries[spec.payload.toInt()])
                }

                else -> {}
            }
        }
        if (entity.ref != null) {
            text(db[entity.ref].toString())
        } else {
            text(entity.text.toString())
        }
    }
}

/**
 * Give the control of the story to the given label and then render it
 * @param story The parent story of the label, used only to perform nested jumps
 * @param label The label to render
 */
fun Session.doJump(story: Story, label: Label) {
    for (elem in label.elements) {
        when (elem) {
            // this element is a text phrase
            is LabelText -> {
                println(db) // #[DEBUG]
                section {
                    // display the actor's name and the phrase
                    elem.actor?.name?.let(::displayStringEntity)
                    text(": ")
                    // of course, we need to render the entities
                    displayStringEntity(elem.phrase)
                    textLine()
                }.run()
            }
            // this element is a signal
            is LabelSignal -> {
                // TODO document this mess
                elem.signals?.forEach { t ->
                    if (t is InputSignal) {
                        var i by liveVarOf<String?>(null)
                        println("ee: ${t.options.isNotEmpty()}") // #[DEBUG]
                        if (t.options.isNotEmpty()) {
                            section {
                                choice(i ?: t.options.first().first, t.options.map{ it.first })
                            }.runUntilSignal {
                                onChoiceChanged(t.options.first().first) {
                                    i = choosen
                                    if (shouldAccept) {
                                        db["#${t.subject}"] = t.options.find { it.first == choosen }!!.second
                                        signal()
                                    }
                                }
                            }
                        } else {
                            section {
                                if (i == null) {
                                    text("Input: ")
                                    input()
                                } else {
                                    println("Input: $i for ${t.subject}") // #[DEBUG]
                                    db["#${t.subject}"] = i.toString()
                                    println(db)
                                }
                            }.runUntilInputEntered {
                                onInputEntered {
                                    i = input
                                }
                            }
                        }
                    }
                }
            }
            // this element is a jump to another label
            is LabelJump -> {
                if (elem.jumps.isNotEmpty()) {
                    for (jump in elem.jumps) {
                        when (jump) {
                            is ChangeLabelJump -> {
                                val newLabel = story.labels.find { it.id == jump.targetLabel }
                                println("jumping to $newLabel") // #[DEBUG]
                                section {
                                    if (newLabel != null) {
                                        text("Jump to ${jump.targetLabel}")
                                    } else {
                                        err("No such label: ${jump.targetLabel}")
                                    }
                                }.run()
                                newLabel?.let {
                                    doJump(story, it)
                                }
                            }
                            else -> {
                                err("unknown jump type: ${jump::class.simpleName}").run()
                            }
                        }
                    }
                }
            }
        }
    }
}

// the "database" of all references and so on
val db = mutableMapOf<String, String>()

fun main() = session {
    val main = story.labels.find { it.id == "main" }

    if (main == null) {
        err("no main label").run()
        return@session
    }

    doJump(story, main)
}
