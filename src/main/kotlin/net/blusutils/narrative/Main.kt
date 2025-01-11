/**
 * This file is used just for testing purposes.
 * No one really knows why it is included in the project.
 */

package net.blusutils.narrative

import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer
import net.blusutils.narrative.actor.Actor
import net.blusutils.narrative.actor.actorSerializers
import net.blusutils.narrative.label.jumps.ChangeLabelJump.Companion.jumpTo
import net.blusutils.narrative.label.jumps.defaultjumps.changeLabelJumpSerializer
import net.blusutils.narrative.label.signals.defaultsignals.StopSignal.stopSignal
import net.blusutils.narrative.label.signals.defaultsignals.StoreFactSignal.Companion.storeFact
import net.blusutils.narrative.label.signals.defaultsignals.sampleSignalsSerializers
import net.blusutils.narrative.serialization.AnySerializer
import net.blusutils.narrative.serialization.JsonAnySerializer
import net.blusutils.narrative.story.Story
import net.blusutils.narrative.story.StoryEnvironments
import net.blusutils.narrative.story.buildStory
import net.blusutils.narrative.stringentity.BasicStringEntityWrapper.Companion.ref
import net.blusutils.narrative.stringentity.BasicStringEntityWrapper.Companion.text
import net.blusutils.narrative.stringentity.StringEntity
import net.blusutils.narrative.stringentity.buildEntityString
import net.blusutils.narrative.stringentity.toStringEntitySingleList

@Serializable
@SerialName("some_actor")
internal data class SomeActor(
    override val name: List<StringEntity>,
    override val tags: List<String> = listOf(),
    override val ref: String? = null,
    @SerialName("dynamic_meta")
    @Contextual
    override val dynamicMeta: Any? = null
) : Actor

@Serializable
@SerialName("other_actor")
internal data class OtherActor(
    override val name: List<StringEntity>,
    override val tags: List<String> = listOf(),
    override val ref: String? = null,
    @SerialName("dynamic_meta")
    @Contextual
    override val dynamicMeta: Any? = null
) : Actor

@Serializable
internal data class LocalRes(
    val actors: List<Actor>,
    val test: String
)

@Serializable
internal data class CustomMeta(
    val test: String,
    val some: Int
)

@OptIn(NonStandardNarrativeApi::class, ExperimentalSerializationApi::class)
internal val j = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
    encodeDefaults = true
    serializersModule =
        SerializersModule {

        } +
        actorSerializers {
            subclass(SomeActor::class)
            subclass(OtherActor::class)
        } +
        sampleSignalsSerializers() +
        changeLabelJumpSerializer() +
        JsonAnySerializer.serializersModule
}

@OptIn(NonStandardNarrativeApi::class)
internal val theStory = buildStory {
    meta {
        name = "new_cool_story"
        environment = StoryEnvironments.AnyEnv

        title = "Cool story bruh".toStringEntitySingleList()/*buildEntityString { // array of entities
            + "My new cool "
            + put("Story").italic().color(0xFF0000)
            + "for Narrative v"
//            + env(Env.VERSION)
        }*/
        authors = mutableListOf("EgorBron")

        dynamicMeta = CustomMeta(
            "Test", 42
        )
    }

    resources(LocalRes(listOf(), "")) { it.copy(
        defineResSet("@/actors/BaseActors"),
        defineRes("narrative/strings/SomeString", key = "test") ?: ""
    ) }

//    dynamicResources<???> {
//        // TODO custom serializers for dyn res like factdb
//    }

    labels {
        main { // an alias to label("main")
            jumpTo("greetings")
        }
        label("greetings") {
            text(
                SomeActor(
                "Some".toStringEntitySingleList()
            ), buildEntityString {
                + ref("#test")
            })
            storeFact(mapOf("value" to 42))
            stopSignal()

//            + "Some text"
//            - "Some text" tagged("reakt")
//            "Actor string" - "Text"
//            actorVar * "Text"
//            aref("#actors") - tref("#test")
//            - {
//                + "Some "
//                + put("beautiful").bold()
//                + " text"
//            }
//            ! buildJsonObject { put("value", true) } // desired as StoreFact
//            ! StopSignal
        }
    }
//
//    tasks {
//
//    }
}

internal const val src = """
{
  "ver": 1,
  "meta": {
    "name": "new_cool_story",
    "environment": "AnyEnv",
    "title": [
      {
        "text": "Cool story bruh",
        "entity_specs": [],
        "ref": null
      }
    ],
    "authors": [
      "EgorBron"
    ],
    "dynamic_meta": {
      "test": "Test",
      "some": 42
    }
  },
  "resources": {
    "BaseActors": "@/actors/BaseActors",
    "test": "narrative/strings/SomeString"
  },
  "labels": [
    {
      "id": "main",
      "tags": ["main_label"],
      "elements": [
        {
          "type": "jump",
          "jumps": [{
            "type": "jump_to_label",
            "targetLabel": "greetings",
            "payload": null,
            "tags": []
          }],
          "tags": ["first_jump"]
        }
      ]
    },
    {
      "id": "greetings",
      "elements": [
        {
          "type": "text",
          "actor": {
            "type": "some_actor",
            "name": [
              {
                "text": "Some",
                "entity_specs": [],
                "ref": null
              }
            ]
          },
          "phrase": [
            {
              "text": "#",
              "entity_specs": [],
              "ref": "#test/test_text"
            }
          ]
        }
      ]
    }
  ]
}
"""

@OptIn(NonStandardNarrativeApi::class)
internal val javaStory = buildStory {
    meta {
        name = "mystory"
    }
    labels {
        main {
            text("Some text")
            text(
                phrase = buildEntityString {
                    + put("Some text")
                        .bold()
                        .toStringEntity()
                }
            )
        }
    }
}

internal fun getJavaStorySerialized() = j.encodeToString(javaStory)

internal fun main() {
    val str = j.encodeToString(
        theStory
    )
    println(str)

    val serialized = j.decodeFromString<Story>(src)
    println(serialized)
    val deserialized = j.encodeToString(serialized)
    println(deserialized)

    val m1 = j.encodeToString(theStory.meta)
    println(m1)
    val m2 = j.encodeToString(serialized.meta)
    println(m2)
    println(m1 == m2)
    println(
        theStory.meta == serialized.meta
    )
}